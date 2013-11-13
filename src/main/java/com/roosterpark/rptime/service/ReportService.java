package com.roosterpark.rptime.service;

import com.google.appengine.repackaged.org.joda.time.Hours;
import com.roosterpark.rptime.model.Client;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
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
	TimeSheetService timeSheetService;
	@Inject
	WorkerService workerService;
        @Inject
        PaidTimeOffService ptoService;

	private String yearMonthDateTimeFormatter = "MMMM YYYY";

	protected Map<Long, Long> getWorkerIdToHoursMapForMonth(final List<Worker> workers, final List<TimeSheetView> timeSheets,
			final LocalDate d) {
		final Map<Long, Long> result = new LinkedHashMap<Long, Long>();
		LocalDate date1 = new LocalDate(d.getYear(), d.getMonthOfYear(), 1);
		LocalDate date2 = new LocalDate(date1.plusMonths(1));

		LOGGER.debug("building report for {} workers, {} timeSheets with dates betwen [{},{}]",
				new Object[] { workers.size(), timeSheets.size(), date1, date2 });
		for (Worker w : workers) {
			Long workerId = w.getId();
			LOGGER.trace("\t found {} timesheets for worker {}", timeSheets.size(), workerId);
			Long hours = new Long(0L);
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
								long entryMillis = (long) (end - start); // millis
								long entryHours = entryMillis / (60L * 60L * 1000L);
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
		final Map<String, Object> map = new HashMap<String, Object>();
		final LocalDate d = new LocalDate();
		final List<Worker> workers = workerService.getAll();
		final List<TimeSheetView> timeSheets = timeSheetService.getAllAdmin();
		map.put("workerList", workers);
		map.put("workerIdToHoursMap", getWorkerIdToHoursMapForMonth(workers, timeSheets, d));
		map.put("reportDate", d.toString(yearMonthDateTimeFormatter));
		map.put("updateDate", new LocalDateTime());
		return map;
	}

	public Map<String, Object> getTimeSheetsPerWorkerByWeekForClientReport(final Long clientId, final LocalDate date) {
		Validate.notNull(clientId);
		LOGGER.debug("clientId={}", clientId);
		final Map<String, Object> map = new HashMap<String, Object>();
		final LocalDate d = new LocalDate();
		final List<Worker> workers = workerService.getAll();
		final List<TimeSheetView> timeSheets = timeSheetService.getAllForClientWeek(clientId, date);

		Map<Long, Map<Object, Object>> reportMap = new LinkedHashMap<Long, Map<Object, Object>>();
		for (Worker worker : workers) {
			reportMap.put(worker.getId(), new LinkedHashMap<Object, Object>());
		}

		for (TimeSheetView timeSheet : timeSheets) {
			List<TimeSheetDay> days = timeSheet.getDays();
			for (TimeSheetDay day : days) {
				List<TimeCardLogEntry> entries = day.getEntries();
				for (TimeCardLogEntry entry : entries) {
					if (clientId.equals(entry.getClientId())) {
						if (entry.getStartTime() != null && entry.getEndTime() != null) {
							final Period p = new Period(entry.getStartTime(), entry.getEndTime(), PeriodType.minutes());
							final long hours = ((long) p.getMinutes()) / 60L;
							final Map<Object, Object> workerDateToHoursMap = reportMap.get(entry.getWorkerId());
							Long h = (Long) ObjectUtils.defaultIfNull(workerDateToHoursMap.get(entry.getDate()), 0L);
							h = h + hours;
							workerDateToHoursMap.put(entry.getDate(), h);
						}
					}
				}
			}
		}

		map.put("reportMap", reportMap);
		map.put("workerList", workers);
		map.put("reportDate", d.toString(yearMonthDateTimeFormatter));
		map.put("updateDate", new LocalDateTime());
		return map;
	}
        
        public Map<Long, Integer> getNumberOfHoursForClientInRange(final Long clientId, LocalDate startDate, LocalDate endDate) {
            Map<Long, Integer> hourMap = new LinkedHashMap<>();
            
            LOGGER.debug("Generating report of hours logged between {} and {} for client {}", startDate, endDate, clientId);
            List<TimeSheetView> views = timeSheetService.getAllForClientRange(clientId, startDate, endDate);
            for(TimeSheetView view : views) {
                // Initialize
                int current = 0;
                // Load
                if(hourMap.containsKey(view.getWorkerId())) {
                    current = hourMap.get(view.getWorkerId());
                }
                // Populate
                for(TimeSheetDay day : view.getDays()) {
                    for(TimeCardLogEntry entry : day.getEntries()) {
                        LOGGER.debug("Inspecting Log entry {}", entry);
                        if(entry.getClientId().equals(clientId)) {
                            LOGGER.debug("FOUND hours to add...");
                            current += entry.getEndTime().getHourOfDay() - entry.getStartTime().getHourOfDay();
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
}
