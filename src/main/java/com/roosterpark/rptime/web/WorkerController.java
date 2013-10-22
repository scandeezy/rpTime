package com.roosterpark.rptime.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.roosterpark.rptime.model.Worker;
import com.roosterpark.rptime.service.WorkerService;

@Controller
public class WorkerController {

	@Inject
	WorkerService workerService;

	@RequestMapping(value = "/workers", method = GET)
	public Worker getWorker() {

		Worker result = null;
		try {
			result = workerService.get("abc");
		} catch (Exception e) {
		}

		if (result == null) {
			result = new Worker();
			result.setFirstName("test");
			workerService.set(result);
		}
		return result;
	}
}
