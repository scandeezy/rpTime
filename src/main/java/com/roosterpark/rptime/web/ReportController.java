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
	public Map<String, Object> getTotalHoursPerWorkerPerMonth() {
		return reportService.getTotalHoursPerWorkerPerMonthReport();
	}

	/**
	 * Report Workers with fewer than 40 hours per week.
	 * <p>
	 * TODO: make a "notification"
	 */
	@RequestMapping(value = "/workers-with-fewer-than-forty-hours-per-week")
	@ResponseBody
	public Map<String, Object> getWorkersWithFewerThanFortyHoursPerWeek() {
		return null;
	}

	/**
	 * Per-client view: Timecards per user
	 */
	@RequestMapping(value = "/timecards-per-user-for-client/{clientId}")
	@ResponseBody
	public Map<String, Object> timecardsPerUserForClient(@PathVariable Long clientId) {
		return null;
	}

	/**
	 * Per-client view: Timecards per user
	 */
	@RequestMapping(value = "/timesheets-per-worker-by-month-for-client/{clientId}")
	@ResponseBody
	public Map<String, Object> timeSheetsPerWorkerByMonthForClient(@PathVariable Long clientId) {
		return reportService.getTimeSheetsPerWorkerByMonthForClientReport(clientId, new LocalDate());
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
