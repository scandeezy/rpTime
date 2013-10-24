package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roosterpark.rptime.model.Client;

@Named
public class ClientService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public Client getById(Long id) {
		LOGGER.debug("Getting Client with key {}", id);
		return ofy().load().type(Client.class).id(id).now();
	}

	public List<Client> getPage(int limit, int offset) {
		LOGGER.debug("Getting Client page with limit {} and offset {}", limit, offset);
		return ofy().load().type(Client.class).limit(limit).offset(offset).list();
	}

	public List<Client> getAll() {
		return ofy().load().type(Client.class).list();
	}

	public void set(Client item) {
		LOGGER.debug("Saving Client {}", item);
		ofy().save().entity(item).now();
	}

	public void delete(Long id) {
		LOGGER.debug("Delete Client {}", id);
		Client c = getById(id);
		ofy().delete().entity(c).now();
	}
}
