package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityNotFoundException;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.ObjectifyService;
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

	public ContractService() {
		LOGGER.trace("registering Contract class with ObjectifyService");
		ObjectifyService.register(Contract.class);
		LOGGER.trace("registered Contract");
	}

	public List<Contract> getContractsForWorker(Long worker) {
		return ofy().load().type(Contract.class).filter(Contract.WORKER_KEY, worker).list();
	}

	public List<Contract> getActiveContractsForWorker(Long worker) {
		List<Contract> contracts = getContractsForWorker(worker);
		List<Contract> active = new LinkedList<Contract>();
		LocalDate now = new LocalDate();
		for (Contract contract : contracts) {
			if (contract.getStart().compareTo(now) < 1 && contract.getEnd().compareTo(now) > -1)
				active.add(contract);
		}

		return active;
	}

	public List<Contract> getContractsForClient(Long client) {
		return ofy().load().type(Contract.class).filter(Contract.CLIENT_KEY, client).list();
	}

	public List<Contract> getActiveContractsForClient(Long client) {
		List<Contract> contracts = getContractsForClient(client);
		List<Contract> active = new LinkedList<Contract>();
		LocalDate now = new LocalDate();
		for (Contract contract : contracts) {
			if (contract.getStart().compareTo(now) < 1 && contract.getEnd().compareTo(now) > -1)
				active.add(contract);
		}

		return active;
	}

	public Contract getById(Long id) {
		LOGGER.warn("Getting Contract with key " + id);
		return ofy().load().type(Contract.class).id(id).now();
	}

	public List<Contract> getAll() {
		return ofy().load().type(Contract.class).list();
	}

	public List<Contract> getPage(int count, int offset) {
		LOGGER.warn("Getting contract page with count " + count + " and offset " + offset);
		return ofy().load().type(Contract.class).limit(count).offset(offset).list();
	}

	public void set(Contract item) {
		// Verify data given
		Worker worker = workerService.getById(item.getWorker());
		Client client = clientService.getById(item.getClient());

		if (worker == null)
			throw new EntityNotFoundException("No worker with id " + item.getWorker());
		if (client == null)
			throw new EntityNotFoundException("No client with id " + item.getClient());

                if(item.getStart() != null && item.getEnd() != null && item.getStart().compareTo(item.getEnd()) > -1)
                    throw new IllegalArgumentException("Start must be before end.");
                
		LOGGER.warn("Saving contract " + item);
		ofy().save().entity(item).now();
	}
}
