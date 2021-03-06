package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;
import org.joda.time.Interval;
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
	 * @param searchInterval
	 *            - the {@link Interval} to compare to the {@link Contract Contract's} start/end date interval.
	 * @return the {@link List} of active {@link Contract Contracts}.
	 * @throws EntityNotFoundException
	 *             if no {@link Worker} found for {@code workerId}.
	 */
	public List<Contract> getActiveContractsForWorker(Long workerId, Interval searchInterval) throws EntityNotFoundException {
		Worker w = workerService.getById(workerId);
		if (w == null) {
			User u = userService.getCurrentUser();
			throw new EntityNotFoundException("Required 'Worker' not found for id '" + workerId
					+ "'.  Solution: create Worker on the /workers page: perhaps for user " + u);
		}
		final List<Contract> contracts = getContractsForWorker(workerId);
		LOGGER.debug("found {} contracts for worker {}.  Determine which are active.", CollectionUtils.size(contracts), workerId);
        if(searchInterval == null)
            searchInterval = new Interval(System.currentTimeMillis()-1, System.currentTimeMillis());
        
		return getActiveContractsInSearchInterval(contracts, searchInterval);
	}

	public List<Contract> getContractsForClient(Long client) {
		return ofy().load().type(Contract.class).filter(Contract.CLIENT_KEY, client).list();
	}

	public List<Contract> getActiveContractsForClient(final Long client, final LocalDate date) {
		Interval searchInterval = new Interval(date.toDateTimeAtStartOfDay(), new DateTime());
		return getActiveContractsForClientInInterval(client, searchInterval);
	}

	public List<Contract> getActiveContractsForClientInInterval(final Long clientId, final Interval searchInterval) {
		Validate.noNullElements(new Object[] { clientId, searchInterval });
		final List<Contract> contracts = getContractsForClient(clientId);
		return getActiveContractsInSearchInterval(contracts, searchInterval);
	}

	public List<Contract> getActiveContractsInSearchInterval(final List<Contract> contracts, final Interval searchInterval) {
		Validate.notNull(searchInterval, "search interval required");
		final List<Contract> active = new LinkedList<Contract>();
		if (CollectionUtils.isEmpty(contracts)) {
			return active;// empty
		}
		LOGGER.debug("searchInterval={}", searchInterval);

		for (Contract contract : contracts) {

			final LocalDate contractStartDate = contract.getStartDate();
			final LocalDate contractEndDate = contract.getEndDate();

			LOGGER.trace("\tcheck if searchInterval contains {} or {}", contractStartDate, contractEndDate);
			boolean startDateOk = searchInterval.contains(contractStartDate.toDate().getTime());
			if (searchInterval.isAfter(contractStartDate.toDateTimeAtStartOfDay())) {
				startDateOk = true;
			}

			boolean endDateOk = (contractEndDate == null) ? true : false;
			if (contractEndDate != null) {
				final Interval contractInterval = getIntervalForContract(contract);
				endDateOk = contractInterval.overlaps(searchInterval);
			}

			LOGGER.trace("\tstartDateOk={} && endDateOk={}", startDateOk, endDateOk);
			if (startDateOk && endDateOk) {
				active.add(contract);
			}
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

	public List<Worker> getWorkersWithActiveContractsInInterval(final Long clientId, final Interval searchInterval) {
		final List<Worker> result = new LinkedList<Worker>();
		final List<Contract> contracts = getActiveContractsForClientInInterval(clientId, searchInterval);
		for (Contract c : contracts) {
			Worker w = workerService.getById(c.getWorker());
			if (!result.contains(w)) {
				result.add(w);
			}
		}
		return result;
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

	public Interval getIntervalForContract(Contract c) {
		Validate.notNull(c, "Contract required");
		final LocalDate start = c.getStartDate();
		Validate.notNull(start, "Contract must have a startDate");
		final LocalDate end = c.getEndDate();
		if (end != null) {
			return new Interval(start.toDateTimeAtStartOfDay(), end.toDateTimeAtStartOfDay());
		} else { // end== null
			final DateTime d = new DateTime();
			LOGGER.debug("Contract {} does not have an end date; using current time {}", c.getId(), d);
			return new Interval(start.toDateTimeAtStartOfDay(), d);
		}
	}
}
