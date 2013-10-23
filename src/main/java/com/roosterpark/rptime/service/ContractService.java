package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.roosterpark.rptime.model.Client;
import com.roosterpark.rptime.model.Contract;
import com.roosterpark.rptime.model.Worker;

@Named
public class ContractService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Inject
	WorkerService workerService;
	@Inject
	ClientService clientService;
//	@Inject
//	DatastoreService datastore;

	public List<Contract> getContractsForWorker(Long worker)
	{
//		Filter workerFilter = new FilterPredicate(Contract.WORKER_KEY, FilterOperator.EQUAL, worker);
//		Filter dateMinFilter = new FilterPredicate(Contract.START_KEY, FilterOperator.LESS_THAN_OR_EQUAL, date);
//		Filter dateMaxFilter = new FilterPredicate(Contract.END_KEY, FilterOperator.GREATER_THAN_OR_EQUAL, date);
//		Filter compositeFilter = CompositeFilterOperator.and(workerFilter, dateMinFilter, dateMaxFilter);
//		
//		Query q = new Query(Contract.class.getSimpleName()).setFilter(compositeFilter);
//		
//		Iterable<Entity> items = datastore.prepare(q).asIterable();
		
		return ofy().load().type(Contract.class)
			.filter(Contract.WORKER_KEY, worker)
			.list();
	}
	
	public List<Contract> getActiveContractsForWorker(Long worker)
	{
		List<Contract> contracts = getContractsForWorker(worker);
		List<Contract> active = new LinkedList<Contract>();
		Date now = new Date();
		for(Contract contract : contracts)
		{
			if(contract.getStart().compareTo(now) < 1 && contract.getEnd().compareTo(now) > -1)
				active.add(contract);
		}
		
		return active;
	}
	
	public Contract get(String sKey) throws EntityNotFoundException {
		LOGGER.warn("Getting Contract with key " + sKey);
		Long key = Long.valueOf(sKey);
		return ofy().load().type(Contract.class).id(key).now();
	}

	public List<Contract> getPage(int count, int offset) {
		LOGGER.warn("Getting contract page with count " + count + " and offset " + offset);
		return ofy().load().type(Contract.class).limit(count).offset(offset).list();
	}

	public void set(Contract item) {
		// Verify data given
		Worker worker = workerService.getById(item.getWorker());
		Client client = clientService.getById(item.getClient());
		
		if(worker == null)
			throw new IllegalArgumentException("No worker with id " + item.getWorker());
		if(client == null)
			throw new IllegalArgumentException("No client with id " + item.getClient());
		
		LOGGER.warn("Saving contract " + item);
		ofy().save().entity(item).now();
	}
}
