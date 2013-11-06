package com.roosterpark.rptime.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roosterpark.rptime.model.TimeCardLogEntry;
import com.roosterpark.rptime.model.TimeSheetDay;
import com.roosterpark.rptime.model.TimeSheetView;
import com.roosterpark.rptime.model.Worker;

/**
 * Business logic to create objects to be reported upon. Typically modeled as {@link Map Maps}.
 * 
 * @author jjzabkar
 */
@Named
public class ReportService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	TimeSheetService timeSheetService;
	@Inject
	WorkerService workerService;

	private String yearMonthDateTimeFormatter = "MMMM YYYY";

	protected Map<Long, Long> getWorkerIdToHoursMapForMonth(final List<Worker> workers, final List<TimeSheetView> timeSheets,
			final LocalDate d) {
		final Map<Long, Long> result = new LinkedHashMap<Long, Long>();
		LocalDate date1 = new LocalDate(d.getYear(), d.getMonthOfYear(), 1);
		LocalDate date2 = new LocalDate(date1.plusMonths(1));
		Interval i = new Interval(date1.toDateTimeAtStartOfDay(), date2.toDateTimeAtStartOfDay());

		LOGGER.debug("building report for {} workers, {} timeSheets", workers.size(), timeSheets.size());

		for (Worker w : workers) {
			Long workerId = w.getId();
			LOGGER.debug("\t found {} timesheets for worker {}", timeSheets.size(), workerId);

			Long hours = new Long(0L); // TODO: calc

			for (TimeSheetView t : timeSheets) {

				List<TimeSheetDay> days = t.getDays();
				LOGGER.debug("\t\t found {} days for timeSheet {}", days.size(), t.getId());

				List<TimeCardLogEntry> entries;
				for (TimeSheetDay day : days) {
					entries = day.getEntries();
					LOGGER.debug("\t\t found {} entries for timeSheet {}", entries.size(), t.getId());
					for (TimeCardLogEntry entry : entries) {
						LocalDate entryDate = entry.getDate();
						LOGGER.debug("\t\t\tchecking if {} < {} < {} ", new Object[] { date1, entryDate, date2 });
						if (date1.isBefore(entryDate) && date2.isAfter(entryDate)) {
							int start = entry.getStartTime().getMillisOfDay();
							int end = entry.getEndTime().getMillisOfDay();
							long entryMillis = (long) (end - start); // millis
							long entryHours = entryMillis / (60L * 60L * 1000L); // millis
							hours = hours + entryHours;
						}
					}

				}

			}

			LOGGER.debug("\t put {} hours at worker {}", hours, workerId);
			result.put(workerId, hours);
		}
		return result;
	}

	public Map<String, Object> getTotalHoursPerWorkerPerMonthReport() {
		final Map<String, Object> map = new HashMap<String, Object>();
		final LocalDate d = new LocalDate();
		List<Worker> workers = workerService.getAll();
		List<TimeSheetView> timeSheets = timeSheetService.getAll();
		map.put("workerList", workers);
		map.put("workerIdToHoursMap", getWorkerIdToHoursMapForMonth(workers, timeSheets, d));
		map.put("reportDate", d.toString(yearMonthDateTimeFormatter));
		return map;
	}

}
