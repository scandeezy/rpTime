package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.roosterpark.rptime.model.Client;

@Singleton
public class ClientService implements Service<Client> {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public Client get(String sKey) throws EntityNotFoundException {
		LOGGER.warn("Getting company with key " + sKey);
		Long key = Long.valueOf(sKey);
		return ofy().load().type(Client.class).id(key).now();
	}

	@Override
	public List<Client> getPage(int count, int offset) {
		LOGGER.warn("Getting company page with count " + count + " and offset " + offset);
		return ofy().load().type(Client.class).limit(count).offset(offset).list();
	}

	@Override
	public void set(Client item) {
		LOGGER.warn("Saving company " + item);
		ofy().save().entity(item).now();
	}
}
