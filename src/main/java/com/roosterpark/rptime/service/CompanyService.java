package com.roosterpark.rptime.service;

import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.roosterpark.rptime.model.Company;

public class CompanyService implements Service<Company> {
	public static final String COMPANY_BUCKET_KEY = "companies";
	public static final String COMPANY_NAME_KEY = "companyName";
	public static final String COMPANY_HEADER_KEY = "companyHeader";
	public static final String COMPANY_PHONE_KEY = "companyPhone";
	public static final String COMPANY_START_DAY_OF_WEEK_KEY = "companyStartDay";

	@Override
	public Company get(String sKey) throws EntityNotFoundException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey(COMPANY_BUCKET_KEY, sKey);
		Entity entity;
		entity = datastore.get(key);
		Company company = toCompany(entity);

		return company;
	}

	@Override
	public List<Company> getPage(int count, int offset) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query(COMPANY_BUCKET_KEY);
		List<Entity> entities = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(count).offset(offset));

		List<Company> companies = new LinkedList<Company>();
		for (Entity entity : entities) {
			companies.add(toCompany(entity));
		}

		return companies;
	}

	@Override
	public void set(Company item) {
		Entity company = toEntity(item);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
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
		Entity company = new Entity(COMPANY_BUCKET_KEY);

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
		company.setStartDayOfWeek((Integer) entity.getProperty(COMPANY_START_DAY_OF_WEEK_KEY));

		return company;
	}
}
