package com.roosterpark.rptime.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Map;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.roosterpark.rptime.model.report.HoursForClientInRange;
import com.roosterpark.rptime.model.report.TimeSheetsPerWorkerByMonthForClientReport;
import com.roosterpark.rptime.model.report.TotalHoursPerWorkerPerMonthReport;
import com.roosterpark.rptime.model.report.WorkersWithFewerThanFortyHoursPerWeekReport;
import com.roosterpark.rptime.service.ReportService;

/**
 * {@link Controller} responsible for {@link ReportService}-provided MVC endpoints.
 * <p>
 * Per {@code web.xml} Administrator-only endpoint {@link RequestMapping RequestMappings} are secured by having {@code /admin/} in their URL
 * path.
 * 
 * @author jjzabkar
 */
@Controller
@RequestMapping(value = "/admin/report", method = GET)
public class ReportController {

	@Inject
	ReportService reportService;

	/**
	 * Report Total Hours Per Worker Per Month (across clients)
	 */
	@RequestMapping(value = "/total-hours-per-worker-per-month")
	@ResponseBody
	public TotalHoursPerWorkerPerMonthReport getTotalHoursPerWorkerPerMonth() {
		return reportService.getTotalHoursPerWorkerPerMonthReport(new LocalDate());
	}

	@RequestMapping(value = "/total-hours-per-worker-per-month/{forMonth}")
	@ResponseBody
	public TotalHoursPerWorkerPerMonthReport getTotalHoursPerWorkerPerMonthForMonth(@PathVariable("forMonth") String forMonth) {
		final LocalDate reportDate = new LocalDate(forMonth);
		return reportService.getTotalHoursPerWorkerPerMonthReport(reportDate);
	}

	/**
	 * Report Workers with fewer than 40 hours per week.
	 * <p>
	 * TODO: make a "notification"
	 */
	@RequestMapping(value = "/workers-with-fewer-than-forty-hours-per-week")
	@ResponseBody
	public WorkersWithFewerThanFortyHoursPerWeekReport getWorkersWithFewerThanFortyHoursPerWeek() {
		return reportService.getWorkersWithFewerThanFortyHoursPerWeekReport(new LocalDate());
	}

	// @RequestMapping(value = "/workers-with-fewer-than-forty-hours-per-week/{forMonth}")
	// @ResponseBody
	// public WorkersWithFewerThanFortyHoursPerWeekReport getWorkersWithFewerThanFortyHoursPerWeek(@PathVariable String forMonth) {
	// final LocalDate reportDate = new LocalDate(forMonth);
	// return reportService.getWorkersWithFewerThanFortyHoursPerWeekReport(reportDate);
	// }

	/**
	 * Per-client view: Timecards per user
	 */
	@RequestMapping(value = "/timecards-per-user-for-client/{clientId}")
	@ResponseBody
	public Map<String, Object> timecardsPerUserForClient(@PathVariable Long clientId) {
		return null;
	}

	@RequestMapping(value = "/timesheets-per-worker-by-month-for-client/{clientId}")
	@ResponseBody
	public TimeSheetsPerWorkerByMonthForClientReport timeSheetsPerWorkerByMonthForClient(@PathVariable Long clientId) {
		return reportService.getTimeSheetsPerWorkerByMonthForClientReport(clientId, new LocalDate());
	}

	@RequestMapping(value = "/timesheets-per-worker-by-month-for-client/{clientId}/{forMonth}")
	@ResponseBody
	public TimeSheetsPerWorkerByMonthForClientReport timeSheetsPerWorkerByMonthForClientMonth(@PathVariable("clientId") Long clientId,
			@PathVariable("forMonth") String forMonth) {
		final LocalDate reportDate = new LocalDate(forMonth);
		return reportService.getTimeSheetsPerWorkerByMonthForClientReport(clientId, reportDate);
	}

	@RequestMapping(value = "/hours-recorded/{clientId}/{startDate}/{endDate}")
	@ResponseBody
	public HoursForClientInRange hoursRecordedForClientPerYear(@PathVariable Long clientId, @PathVariable String startDate,
			@PathVariable String endDate) {
		LocalDate start = new LocalDate(startDate);
		LocalDate end = new LocalDate(endDate);
		return reportService.getHoursForClientInRange(clientId, start, end);
	}
}
