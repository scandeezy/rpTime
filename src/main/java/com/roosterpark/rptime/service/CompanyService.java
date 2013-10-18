package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.roosterpark.rptime.model.Company;

@Singleton
public class CompanyService implements Service<Company> {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public Company get(String sKey) throws EntityNotFoundException {
		LOGGER.warn("Getting company with key " + sKey);
		Long key = Long.valueOf(sKey);
		return ofy().load().type(Company.class).id(key).now();
	}

	@Override
	public List<Company> getPage(int count, int offset) {
		LOGGER.warn("Getting company page with count " + count + " and offset " + offset);
		return ofy().load().type(Company.class).limit(count).offset(offset).list();
	}

	@Override
	public void set(Company item) {
		LOGGER.warn("Saving company " + item);
		ofy().save().entity(item).now();
	}
}
