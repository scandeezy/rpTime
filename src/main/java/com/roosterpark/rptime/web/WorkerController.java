package com.roosterpark.rptime.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.roosterpark.rptime.model.Worker;
import com.roosterpark.rptime.service.WorkerService;

@Controller
@RequestMapping(value = "/worker")
public class WorkerController {

	@Inject
	WorkerService service;

	@RequestMapping(method = GET)
	@ResponseBody
	public List<Worker> getAll() {
		return service.getAll();
	}

	@RequestMapping(value = "/idmap", method = GET)
	@ResponseBody
	public SortedMap<Long, Worker> getMap() {
		final List<Worker> list = service.getAll();
		final SortedMap<Long, Worker> map = new TreeMap<Long, Worker>();
		for (Worker obj : list) {
			map.put(obj.getId(), obj);
		}
		return map;
	}

	@RequestMapping(value = "/{id}", method = GET)
	@ResponseBody
	public Worker getById(@PathVariable Long id) {
		return service.getById(id);
	}

	@RequestMapping(method = POST)
	@ResponseBody
	public Worker post(@RequestBody Worker item) {
		service.set(item);
		return item;
	}

}
