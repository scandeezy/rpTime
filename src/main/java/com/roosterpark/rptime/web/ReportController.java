package com.roosterpark.rptime.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/report", method = GET)
public class ReportController {

	/**
	 * Report Total Hours Per Worker Per Month (across clients)
	 */
	@RequestMapping(value = "/total-hours-per-worker-per-month")
	@ResponseBody
	public Object getTotalHoursPerWorkerPerMonth() {
		return "getTotalHoursPerWorkerPerMonth";
	}

	/**
	 * Report Workers with fewer than 40 hours per week.
	 * <p>
	 * TODO: make a "notification"
	 */
	@RequestMapping(value = "/workers-with-fewer-than-forty-hours-per-week")
	@ResponseBody
	public Object getWorkersWithFewerThanFortyHoursPerWeek() {
		return "getWorkersWithFewerThanFortyHoursPerWeek";
	}

	/**
	 * Per-client view: Timecards per user
	 */
	@RequestMapping(value = "/timecards-per-user-for-client/{clientId}")
	@ResponseBody
	public Object timecardsPerUser(@RequestParam Long clientId) {
		return null;
	}

	/**
	 * Per-client view: Timecards per user
	 */
	@RequestMapping(value = "/timecards-per-user-and-by-week-for-client/{clientId}")
	@ResponseBody
	public Object timecardsPerUserAndByWeek(@RequestParam Long clientId) {
		return null;
	}

}
