package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.roosterpark.rptime.model.Contract;

@Singleton
public class ContractService implements Service<Contract> {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public Contract get(String sKey) throws EntityNotFoundException {
		LOGGER.warn("Getting Contract with key " + sKey);
		Long key = Long.valueOf(sKey);
		return ofy().load().type(Contract.class).id(key).now();
	}

	@Override
	public List<Contract> getPage(int count, int offset) {
		LOGGER.warn("Getting contract page with count " + count + " and offset " + offset);
		return ofy().load().type(Contract.class).limit(count).offset(offset).list();
	}

	@Override
	public void set(Contract item) {
		LOGGER.warn("Saving contract " + item);
		ofy().save().entity(item).now();
	}

}
