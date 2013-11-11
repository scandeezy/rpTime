package com.roosterpark.rptime.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.roosterpark.rptime.service.ReportService;

/**
 * MVC {@link RequestMapping Endpoints} for {@link ReportService}-provided reportable business objects.
 * 
 * @author jjzabkar
 */
@Controller
@RequestMapping(value = "/report", method = GET)
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
	@RequestMapping(value = "/timesheets-per-worker-by-week-for-client/{clientId}")
	@ResponseBody
	public Map<String, Object> timeSheetsPerWorkerByWeekForClient(@PathVariable Long clientId) {
		return reportService.getTimeSheetsPerWorkerByWeekForClientReport(clientId);
	}
        
        @RequestMapping(value = "/hours-recorded/{clientId}/{year}")
        @ResponseBody
        public Map<Long, Integer> hoursRecordedForClientPerYear(@PathVariable Long clientId, @PathVariable Integer year) {
                return reportService.getNumberOfPTOHoursInYear(year);
        }
}
