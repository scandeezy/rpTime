package com.roosterpark.rptime.web;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.SortedMap;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.roosterpark.rptime.model.Worker;
import com.roosterpark.rptime.service.WorkerService;

/**
 * {@link Controller} responsible for {@link Worker}-related MVC endpoints.
 * <p>
 * Per {@code web.xml} Administrator-only endpoint {@link RequestMapping RequestMappings} are secured by having {@code /admin/} in their URL
 * path.
 * 
 * @author jjzabkar
 */
@Controller
public class WorkerController {

	@Inject
	WorkerService service;

    public WorkerController(){}
    
	@RequestMapping(value = "/admin/worker", method = GET)
	@ResponseBody
	public List<Worker> getAll() {
		return service.getAll();
	}

	@RequestMapping(value = "/admin/worker/idmap", method = GET)
	@ResponseBody
	public SortedMap<Long, Worker> getMap() {
		return service.getMap();
	}

	@RequestMapping(value = "/admin/worker/{id}", method = GET)
	@ResponseBody
	public Worker getById(@PathVariable Long id) {
		return service.getById(id);
	}

	@RequestMapping(value = "/admin/worker", method = POST)
	@ResponseBody
	public Worker post(@RequestBody Worker item) {
		service.set(item);
		return item;
	}

	@RequestMapping(value = "/admin/worker/{id}", method = DELETE)
	@ResponseStatus(NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		service.delete(id);
	}

}
