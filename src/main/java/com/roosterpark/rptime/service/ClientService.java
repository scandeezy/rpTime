package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.ObjectifyService;
import com.roosterpark.rptime.model.Client;

@Named
public class ClientService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public ClientService() {
		LOGGER.trace("registering Client class with ObjectifyService");
		ObjectifyService.register(Client.class);
		LOGGER.trace("registered Client");
	}

	public Client getById(Long id) {
		LOGGER.warn("Getting Client with key {}", id);
		return ofy().load().type(Client.class).id(id).now();
	}

	public List<Client> getPage(int limit, int offset) {
		LOGGER.warn("Getting Client page with limit {} and offset {}", limit, offset);
		return ofy().load().type(Client.class).limit(limit).offset(offset).list();
	}

	public List<Client> getAll() {
		return ofy().load().type(Client.class).list();
	}

	public void set(Client item) {
		LOGGER.warn("Saving Client {}", item);
		ofy().save().entity(item).now();
	}
}
