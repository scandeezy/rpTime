package com.roosterpark.rptime.web;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.roosterpark.rptime.model.TimeSheet;
import com.roosterpark.rptime.service.TimeSheetService;

@Controller
@RequestMapping(value = "/timesheet")
public class TimeSheetController {

	@Inject
	TimeSheetService service;

	@RequestMapping(value = "/new", method = POST)
	@ResponseBody
	public TimeSheet create() {
		return service.create();
	}

	@RequestMapping(method = GET)
	@ResponseBody
	public List<TimeSheet> getAll() {
		return service.getAll();
	}

	@RequestMapping(value = "/{id}", method = GET)
	@ResponseBody
	public TimeSheet getById(@PathVariable Long id) {
		return service.getById(id);
	}

	@RequestMapping(method = POST)
	@ResponseBody
	public TimeSheet post(@RequestBody TimeSheet item) {
		service.set(item);
		return item;
	}

	@RequestMapping(value = "/{id}", method = PUT)
	@ResponseStatus(ACCEPTED)
	@ResponseBody
	public TimeSheet put(@PathVariable("id") Long id, @RequestBody TimeSheet item) {
		service.set(item);
		return item;
	}

}
