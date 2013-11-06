package com.roosterpark.rptime.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roosterpark.rptime.model.TimeSheet;
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

	protected Map<Long, Long> getWorkerIdToHoursMapForMonth(final List<Worker> workers, final LocalDate d) {
		final Map<Long, Long> result = new LinkedHashMap<Long, Long>();

		LOGGER.debug("found {} workers for date {}", workers.size(), d);

		for (Worker w : workers) {
			Long workerId = w.getId();
			List<TimeSheet> timeSheets = timeSheetService.getAllForWorker(workerId);

			LOGGER.debug("\t found {} timesheets for worker {}", timeSheets.size(), workerId);

			Long hours = new Long(40L); // TODO: calc

			for (TimeSheet t : timeSheets) {
				List<Long> ids = t.getTimeCardIds();
				LOGGER.debug("\t\t found {} timeCardIds for timeSheet {}", ids.size(), t.getId());

				for (Long id : ids) {
					hours = hours + 1L; // TODO: FIXME
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
		map.put("workerList", workers);
		map.put("workerIdToHoursMap", getWorkerIdToHoursMapForMonth(workers, d));
		map.put("reportDate", d.toString(yearMonthDateTimeFormatter));
		return map;
	}

}
