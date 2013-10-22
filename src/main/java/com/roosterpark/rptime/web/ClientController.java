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

import com.roosterpark.rptime.model.Client;
import com.roosterpark.rptime.service.ClientService;

@Controller
@RequestMapping(value = "/client")
public class ClientController {

	@Inject
	ClientService service;

	@RequestMapping(method = GET)
	@ResponseBody
	public List<Client> getAll() {
		return service.getAll();
	}

	@RequestMapping(value = "/{id}", method = GET)
	@ResponseBody
	public Client getById(@PathVariable Long id) {
		return service.getById(id);
	}

	@RequestMapping(method = POST)
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

}
