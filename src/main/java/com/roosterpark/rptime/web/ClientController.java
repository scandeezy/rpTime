package com.roosterpark.rptime.web;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.roosterpark.rptime.model.Client;
import com.roosterpark.rptime.service.ClientService;

@Controller
@RequestMapping(value = "/client")
public class ClientController {

	@Inject
	ClientService service;

	@RequestMapping(method = GET)
	@ResponseBody
	public SortedMap<Long, Client> getAll() {
		final List<Client> list = service.getAll();
		final SortedMap<Long, Client> map = new TreeMap<Long, Client>();
		for (Client obj : list) {
			map.put(obj.getId(), obj);
		}
		return map;
	}

	@RequestMapping(value = "/{id}", method = GET)
	@ResponseBody
	public Client getById(@PathVariable Long id) {
		return service.getById(id);
	}

	@RequestMapping(method = POST)
	// @ResponseStatus(CREATED)
	@ResponseBody
	public Client post(@RequestBody Client item) {
		service.set(item);
		return item;
	}

	@RequestMapping(value = "/{id}", method = PUT)
	@ResponseStatus(ACCEPTED)
	@ResponseBody
	public Client put(@PathVariable("id") Long id, @RequestBody Client item) {
		service.set(item);
		return item;
	}

	@RequestMapping(value = "/{id}", method = DELETE)
	@ResponseStatus(NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		service.delete(id);
	}

}
