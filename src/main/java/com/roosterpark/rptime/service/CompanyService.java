package com.roosterpark.rptime.service;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.roosterpark.rptime.model.Company;

@Singleton
public class CompanyService implements Service<Company> {
	private static final Logger LOG = Logger.getLogger(CompanyService.class.getName());
	
	public static final String COMPANY_BUCKET_KEY = "companies";
	public static final String COMPANY_NAME_KEY = "companyName";
	public static final String COMPANY_HEADER_KEY = "companyHeader";
	public static final String COMPANY_PHONE_KEY = "companyPhone";
	public static final String COMPANY_START_DAY_OF_WEEK_KEY = "companyStartDay";

	@Inject
	DatastoreService datastore;

	@Override
	public Company get(String sKey) throws EntityNotFoundException {
		Key key = KeyFactory.createKey(COMPANY_BUCKET_KEY, sKey);
		Entity entity;
		entity = datastore.get(key);
		LOG.warning("Entity extracted: " + entity);
		Company company = toCompany(entity);
		LOG.warning("Turned into company: " + company);
		return company;
	}

	@Override
	public List<Company> getPage(int count, int offset) {
		Query query = new Query(COMPANY_BUCKET_KEY);
		List<Entity> entities = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(count).offset(offset));

		List<Company> companies = new LinkedList<Company>();
		for (Entity entity : entities) {
			LOG.warning("Entity extracted: " + entity);
			Company company = toCompany(entity);
			companies.add(company);
			LOG.warning("Turned into company: " + company);
		}

		return companies;
	}

	@Override
	public void set(Company item) {
		LOG.warning("Translating company to entity: " + item);
		Entity company = toEntity(item);
		LOG.warning("Setting entity: " + company);
		datastore.put(company);
	}

	/**
	 * Dumb mappers
	 * 
	 * @param input
	 * @return
	 */

	public Entity toEntity(Company input) {
		if (input == null)
			throw new IllegalArgumentException("No arguments may be null.");
		Entity company = new Entity(COMPANY_BUCKET_KEY, input.getName());

		company.setProperty(COMPANY_NAME_KEY, input.getName());
		company.setProperty(COMPANY_HEADER_KEY, input.getHeader());
		company.setProperty(COMPANY_PHONE_KEY, input.getPhone());
		company.setProperty(COMPANY_START_DAY_OF_WEEK_KEY, input.getStartDayOfWeek());

		return company;
	}

	public Company toCompany(Entity entity) {
		Company company = new Company();
		
		company.setName((String) entity.getProperty(COMPANY_NAME_KEY));
		company.setHeader((String) entity.getProperty(COMPANY_HEADER_KEY));
		company.setPhone((String) entity.getProperty(COMPANY_PHONE_KEY));
		company.setStartDayOfWeek(Integer.valueOf(entity.getProperty(COMPANY_START_DAY_OF_WEEK_KEY).toString()));
		
		return company;
	}
}
