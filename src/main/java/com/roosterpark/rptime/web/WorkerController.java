package com.roosterpark.rptime.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.roosterpark.rptime.model.Worker;
import com.roosterpark.rptime.service.WorkerService;

@Controller
public class WorkerController {

	@Inject
	WorkerService workerService;

	@RequestMapping(value = "/worker", method = GET)
	@ResponseBody
	public List<Worker> getAll() {
		return workerService.getAll();
	}

	@RequestMapping(value = "/worker/{id}", method = GET)
	@ResponseBody
	public Worker getById(@PathVariable Long id) {
		return workerService.getById(id);
	}
}
