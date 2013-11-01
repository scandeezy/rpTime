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

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.roosterpark.rptime.model.Client;
import com.roosterpark.rptime.model.Contract;
import com.roosterpark.rptime.model.Worker;

@Named
public class ContractService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	ClientService clientService;
	@Inject
	WorkerService workerService;
	@Inject
	UserService userService;

	public List<Contract> getContractsForWorker(Long worker) {
		return ofy().load().type(Contract.class).filter(Contract.WORKER_KEY, worker).list();
	}

	/**
	 * {@link Contract Contracts} with {@code date} between {@code startDate} and {@code endDate}.
	 * 
	 * @param workerId
	 * @param date
	 * @return the {@link List} of active {@link Contract Contracts}.
	 * @throws EntityNotFoundException
	 *             if no {@link Worker} found for {@code workerId}.
	 */
	public List<Contract> getActiveContractsForWorker(Long workerId, LocalDate date) throws EntityNotFoundException {
		Worker w = workerService.getById(workerId);
		if (w == null) {
			User u = userService.getCurrentUser();
			throw new EntityNotFoundException("Required 'Worker' not found for id '" + workerId
					+ "'.  Solution: create Worker on the /workers page: perhaps for user " + u);
		}
		List<Contract> contracts = getContractsForWorker(workerId);
		List<Contract> active = new LinkedList<Contract>();
		for (Contract contract : contracts) {
			if (contract.getStartDate().compareTo(date) < 1
					&& (contract.getEndDate() == null || contract.getEndDate().compareTo(date) > -1)) {
				active.add(contract);
			} else {
				LOGGER.debug("inactive contract found; date mismatch.  Expected: {} < {} < {}", new Object[] { contract.getStartDate(),
						date, contract.getEndDate() });
			}
		}

		return active;
	}

	public List<Contract> getContractsForClient(Long client) {
		return ofy().load().type(Contract.class).filter(Contract.CLIENT_KEY, client).list();
	}

	public List<Contract> getActiveContractsForClient(Long client, LocalDate date) {
		List<Contract> contracts = getContractsForClient(client);
		List<Contract> active = new LinkedList<Contract>();
		for (Contract contract : contracts) {
			if (contract.getStartDate().compareTo(date) < 1
					&& (contract.getEndDate() == null || contract.getEndDate().compareTo(date) > -1))
				active.add(contract);
		}

		return active;
	}

	public Contract getById(Long id) {
		LOGGER.warn("Getting Contract with key " + id);
		return ofy().load().type(Contract.class).id(id).now();
	}

	public List<Contract> getAll() {
		// TODO: Cache better so we don't have to do this. Costly!
		ofy().clear();
		// ^ Argh

		return ofy().load().type(Contract.class).list();
	}

	public List<Contract> getPage(int count, int offset) {
		LOGGER.warn("Getting contract page with count " + count + " and offset " + offset);
		return ofy().load().type(Contract.class).limit(count).offset(offset).list();
	}

	public void set(Contract item) {
		if (null == item.getWorker())
			throw new IllegalArgumentException("Worker ID required.");
		if (null == item.getClient())
			throw new IllegalArgumentException("Client ID required.");

		// Verify data given
		Worker worker = workerService.getById(item.getWorker());
		Client client = clientService.getById(item.getClient());

		if (worker == null)
			throw new EntityNotFoundException("No worker with id " + item.getWorker());
		if (client == null)
			throw new EntityNotFoundException("No client with id " + item.getClient());
		if (item.getStartDate() != null && item.getEndDate() != null && item.getStartDate().compareTo(item.getEndDate()) > -1)
			throw new IllegalArgumentException("Start must be before end.");

		if (item.getStartDate() == null)
			throw new IllegalArgumentException("Start date required for creating a new contract.");

		if (item.getEndDate() != null && item.getStartDate().compareTo(item.getEndDate()) > -1)
			throw new IllegalArgumentException("Start must be before end.");

		if (item.getOnSite() == null)
			item.setOnSite(Boolean.TRUE);

		// Fill from client
		item.setStartDayOfWeek(client.getStartDayOfWeek());
		item.setLunchRequired(client.getLunchRequired());

		LOGGER.warn("Saving contract " + item);
		ofy().save().entity(item).now();
	}

	public void delete(Long id) {
		Contract c = getById(id);
		ofy().delete().entity(c).now();
	}
}
