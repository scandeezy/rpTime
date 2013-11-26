package com.roosterpark.rptime.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roosterpark.rptime.model.TimeCardLogEntry;
import com.roosterpark.rptime.model.TimeSheetDay;
import com.roosterpark.rptime.model.TimeSheetView;
import com.roosterpark.rptime.model.Worker;
import com.roosterpark.rptime.model.report.HoursForClientInRange;
import com.roosterpark.rptime.model.report.TimeSheetsPerWorkerByMonthForClientReport;
import com.roosterpark.rptime.model.report.WorkersWithFewerThanFortyHoursPerWeekReport;

/**
 * Business logic to create objects to be reported upon. Typically modeled as {@link Map Maps}.
 * 
 * @author jjzabkar
 */
@Named
public class ReportService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	ClientService clientService;
	@Inject
	ContractService contractService;
	@Inject
	PaidTimeOffService ptoService;
	@Inject
	TimeSheetService timeSheetService;
	@Inject
	WorkerService workerService;

	private static final String DATE_TIME_FORMAT_YEAR_MONTH = "MMMM YYYY";
	private static final double MILLIS_TO_HOURS = 60.0 * 60.0 * 1000.0;

	protected Map<Long, Double> getWorkerIdToHoursMapForMonth(final List<Worker> workers, final List<TimeSheetView> timeSheets,
			final LocalDate d) {
		final Map<Long, Double> result = new LinkedHashMap<Long, Double>();
		LocalDate date1 = new LocalDate(d.getYear(), d.getMonthOfYear(), 1);
		LocalDate date2 = new LocalDate(date1.plusMonths(1));

		LOGGER.debug("building report for {} workers, {} timeSheets with dates betwen [{},{}]",
				new Object[] { workers.size(), timeSheets.size(), date1, date2 });
		for (Worker w : workers) {
			Long workerId = w.getId();
			LOGGER.trace("\t found {} timesheets for worker {}", timeSheets.size(), workerId);
			double hours = 0.0;
			for (TimeSheetView t : timeSheets) {
				if (workerId.equals(t.getWorkerId())) {
					List<TimeSheetDay> days = t.getDays();
					LOGGER.trace("\t\t found {} days for timeSheet {}", days.size(), t.getId());
					List<TimeCardLogEntry> entries;
					for (TimeSheetDay day : days) {
						entries = day.getEntries();
						LOGGER.trace("\t\t found {} entries for timeSheet {}", entries.size(), t.getId());
						for (TimeCardLogEntry entry : entries) {
							LocalDate entryDate = entry.getDate();
							LOGGER.trace("\t\t\tchecking if {} < {} < {} ", new Object[] { date1, entryDate, date2 });
							if (date1.isBefore(entryDate) //
									&& date2.isAfter(entryDate) //
									&& entry.getStartTime() != null //
									&& entry.getEndTime() != null) {
								int start = entry.getStartTime().getMillisOfDay();
								int end = entry.getEndTime().getMillisOfDay();
								double entryMillis = (double) (end - start); // millis
								double entryHours = entryMillis / (MILLIS_TO_HOURS);
								LOGGER.trace("\t\t\tentry {} is {} hours ({} millis)", new Object[] { entryDate, entryHours, entryMillis });
								hours = hours + entryHours;
							}
						}
					}
				}
			}
			LOGGER.trace("\t put {} hours at worker {}", hours, workerId);
			result.put(workerId, hours);
		}
		return result;
	}

	public Map<String, Object> getTotalHoursPerWorkerPerMonthReport() {
		final Map<String, Object> map = new LinkedHashMap<String, Object>();
		final LocalDate d = new LocalDate();
		final List<Worker> workers = workerService.getAll();
		final List<TimeSheetView> unfiltered = timeSheetService.getAllAdmin();
		final List<TimeSheetView> timeSheets = timeSheetService.getReportable(unfiltered);

		map.put("workerList", workers);
		map.put("workerIdToHoursMap", getWorkerIdToHoursMapForMonth(workers, timeSheets, d));
		map.put("reportDate", d.toString(DATE_TIME_FORMAT_YEAR_MONTH));
		map.put("updateDate", new LocalDateTime());
		return map;
	}

	/** url: <code>#/report/timesheets-per-worker-by-month-for-client</code> */
	public TimeSheetsPerWorkerByMonthForClientReport getTimeSheetsPerWorkerByMonthForClientReport(final Long clientId, final LocalDate date) {
		Validate.notNull(clientId);
		LOGGER.debug("clientId={}, date={}", clientId, date);
		final Interval searchInterval = getMonthSearchInterval(date);
		LOGGER.debug("searchInterval={}", searchInterval);
		final Map<Long, Map<LocalDate, Long>> workerToDateToTimeSheetMap = new LinkedHashMap<>();
		final List<Worker> workers = contractService.getWorkersWithActiveContractsInInterval(clientId, searchInterval);
		final List<TimeSheetView> unfiltered = timeSheetService.getAllForClientInInterval(clientId, searchInterval);
		final List<TimeSheetView> timeSheets = timeSheetService.getReportable(unfiltered);
		final Map<Long, Object> timeSheetMap = new LinkedHashMap<>();

		Map<Long, Map<LocalDate, Double>> reportMap = new LinkedHashMap<>();
		for (Worker worker : workers) {
			reportMap.put(worker.getId(), new LinkedHashMap<LocalDate, Double>());
			workerToDateToTimeSheetMap.put(worker.getId(), new LinkedHashMap<LocalDate, Long>());
		}

		for (TimeSheetView timeSheet : timeSheets) {
			List<TimeSheetDay> days = timeSheet.getDays();
			for (TimeSheetDay day : days) {
				List<TimeCardLogEntry> entries = day.getEntries();
				for (TimeCardLogEntry entry : entries) {
					final Long workerId = timeSheet.getWorkerId();
					Map<LocalDate, Long> dateToTimeSheetMap = workerToDateToTimeSheetMap.get(workerId);
					if (dateToTimeSheetMap == null) {
						dateToTimeSheetMap = new LinkedHashMap<>();
						workerToDateToTimeSheetMap.put(workerId, dateToTimeSheetMap);
					}
					if (clientId.equals(entry.getClientId())) {
						final LocalDate entryDate = entry.getDate();
						final boolean overlaps = searchInterval.overlaps(entryDate.toInterval());
						LOGGER.trace("check if entryDate={} and searchInterval overlaps={}", entryDate, overlaps);
						if (overlaps) {
							dateToTimeSheetMap.put(entryDate, timeSheet.getId());
							if (entry.getStartTime() != null && entry.getEndTime() != null) {
								final Period p = new Period(entry.getStartTime(), entry.getEndTime(), PeriodType.minutes());
								final double hours = ((double) p.getMinutes()) / 60.0;
								final Long key = entry.getWorkerId();
								Map<LocalDate, Double> workerDateToHoursMap = reportMap.get(key);
								if (workerDateToHoursMap == null) {
									workerDateToHoursMap = new LinkedHashMap<LocalDate, Double>();
									reportMap.put(key, workerDateToHoursMap);
								}
								Double h = (Double) ObjectUtils.defaultIfNull(workerDateToHoursMap.get(entry.getDate()), 0.0);
								h = h + hours;
								workerDateToHoursMap.put(entry.getDate(), h);
							}
						}
					}
				}
			}
			timeSheetMap.put(timeSheet.getId(), timeSheet);
		}

		final TimeSheetsPerWorkerByMonthForClientReport report = new TimeSheetsPerWorkerByMonthForClientReport(date);
		report.setReportMap(reportMap);
		report.setTimeSheetMap(timeSheetMap);
		report.setWorkerToDateToTimeSheetMap(workerToDateToTimeSheetMap);
		report.setWorkerList(workers);
		return report;
	}

	public Map<Long, Double> getNumberOfHoursForClientInRange(final Long clientId, LocalDate startDate, LocalDate endDate) {
		Map<Long, Double> hourMap = new LinkedHashMap<>();

		LOGGER.debug("Generating report of hours logged between {} and {} for client {}", startDate, endDate, clientId);
		List<TimeSheetView> unfiltered = timeSheetService.getAllForClientRange(clientId, startDate, endDate);
		final List<TimeSheetView> timeSheets = timeSheetService.getReportable(unfiltered);

		for (TimeSheetView view : timeSheets) {
			// Initialize
			double current = 0;
			// Load
			if (hourMap.containsKey(view.getWorkerId())) {
				current = hourMap.get(view.getWorkerId());
			}
			// Populate
			for (TimeSheetDay day : view.getDays()) {
				for (TimeCardLogEntry entry : day.getEntries()) {
					LOGGER.debug("Inspecting Log entry {}", entry);
					if (entry.getClientId().equals(clientId)) {
						LOGGER.debug("FOUND hours to add...");
						double millis = (double) (entry.getEndTime().getMillisOfDay() - entry.getStartTime().getMillisOfDay());
						current += (millis / (MILLIS_TO_HOURS));
					}
				}
			}
			// Set
			hourMap.put(view.getWorkerId(), current);
		}

		return hourMap;
	}

	public Map<Long, List<TimeCardLogEntry>> getMappedHoursForClientInRange(Long clientId, LocalDate start, LocalDate end) {
		Map<Long, List<TimeCardLogEntry>> entries = timeSheetService.getLogsForClientOverRange(clientId, start, end);

		return entries;
	}

	public HoursForClientInRange getHoursForClientInRange(Long clientId, LocalDate start, LocalDate end) {
		HoursForClientInRange report = new HoursForClientInRange();
		report.setClientId(clientId);
		report.setStartDate(start);
		report.setEndDate(end);
		report.setWorkerToTotalMap(getNumberOfHoursForClientInRange(clientId, start, end));
		report.setWorkerToTimeMap(getMappedHoursForClientInRange(clientId, start, end));

		return report;
	}

	public static Interval getMonthSearchInterval(final LocalDate date) {
		final LocalDate startDate = new LocalDate(date.getYear(), date.getMonthOfYear(), 1);
		final LocalDate endDate = startDate.plusMonths(1);
		return new Interval(startDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay());
	}

	public WorkersWithFewerThanFortyHoursPerWeekReport getWorkersWithFewerThanFortyHoursPerWeekReport(LocalDate date) {
		WorkersWithFewerThanFortyHoursPerWeekReport report = new WorkersWithFewerThanFortyHoursPerWeekReport(date);

		return report;
	}
}
