package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.datanucleus.util.StringUtils;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roosterpark.rptime.model.Client;
import com.roosterpark.rptime.model.Contract;
import com.roosterpark.rptime.model.TimeCardLogEntry;
import com.roosterpark.rptime.model.TimeSheetDay;

@Named
public class ClientService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	ContractService contractService;

	private Client ptoClient;

	public ClientService() {
	}

	@Inject
	public void setPtoClientId(PaidTimeOffService ptoService) {
		ptoClient = ptoService.getPtoClient();
	}

	public Client getById(Long id) {
		LOGGER.debug("Getting Client with key {}", id);
		return ofy().load().type(Client.class).id(id).now();
	}

	public List<Client> getPage(int limit, int offset) {
		LOGGER.debug("Getting Client page with limit {} and offset {}", limit, offset);
		return ofy().load().type(Client.class).limit(limit).offset(offset).list();
	}

	public List<Client> getAll() {
		LOGGER.debug("getAll Clients");
		// TODO: Cache better so we don't have to do this. Costly!
		ofy().clear();
		// ^ Argh

		return ofy().load().type(Client.class).list();
	}

	public List<Client> getAllForIdList(final List<Long> ids) {
		LOGGER.debug("getAll Clients in [{}]", ids);
		final Map<Long, Client> loaded = ofy().load().type(Client.class)//
				.ids(ids);
		return new LinkedList<Client>(loaded.values());
	}

	public void set(Client item) {
		if (StringUtils.isEmpty(item.getName()))
			throw new IllegalArgumentException("Client name required.");
		if (null == item.getStartDayOfWeek())
			throw new IllegalArgumentException("Start Day Of Week required.");
		if (null == item.getLunchRequired())
			item.setLunchRequired(true);

		LOGGER.debug("Saving Client {}", item);
		ofy().save().entity(item).now();
		LOGGER.debug("Saved Client={}", item);

		// all = getAll();
		// LOGGER.debug("all.size = {}", all.size());

	}

	public void delete(Long id) {
		LOGGER.debug("Delete Client {}", id);
		Client c = getById(id);
		ofy().delete().entity(c).now();
	}

	/**
	 * {@code available} {@link Client Clients}.
	 * 
	 * @param days
	 *            - the {@link TimeSheetDay} whose {@code clientIds} to inspect.
	 * @return
	 */
	// @Cacheable(value = CacheConfiguration.CLIENT_SERVICE_GET_AVAILABLE_FOR_WORKER_INTERVAL_DAYS_CACHE_NAME, key =
	// "#p0.concat('-').concat(#p1).concat('-').concat(#p3)")
	public Set<Client> getAvailableForWorkerIntervalDays(final Long workerId, final Interval searchInterval, final List<TimeSheetDay> days) {
		LOGGER.debug("searching for available with worker={},searchInterval={},days={}", new Object[] { workerId, searchInterval, days });
		final List<Long> ids = new LinkedList<>();

		final List<Contract> contracts = contractService.getActiveContractsForWorkerInterval(workerId, searchInterval);
		for (Contract contract : contracts) {
			ids.add(contract.getClient());
		}

		ids.addAll(getSelectedIdsForTimeSheetDays(days));
		ids.add(ptoClient.getId());

		final Set<Client> result = new HashSet<>();
		result.addAll(getAllForIdList(ids));
		return result;
	}

	/**
	 * {@code selected} is a <b>subset</b> of {@code available} {@link Client Clients}.
	 * 
	 * @param days
	 *            - the {@link TimeSheetDay} whose {@code clientIds} to inspect.
	 * @return
	 */
	// @Cacheable(value = CacheConfiguration.CLIENT_SERVICE_GET_AVAILABLE_FOR_WORKER_INTERVAL_DAYS_CACHE_NAME, key = "#days.id")
	public Set<Long> getSelectedIdsForTimeSheetDays(final List<TimeSheetDay> days) {
		final Set<Long> result = new HashSet<>();
		for (TimeSheetDay day : days) {
			for (TimeCardLogEntry e : day.getEntries()) {
				result.add(e.getClientId());
			}
		}
		return result;
	}

	public SortedMap<Long, Client> getAllForWorker(Long workerId) {
		return getAllMap();
	}

	public SortedMap<Long, Client> getAllMap() {
		final List<Client> list = getAll();
		final SortedMap<Long, Client> map = new TreeMap<Long, Client>();
		for (Client obj : list) {
			map.put(obj.getId(), obj);
		}
		return map;
	}

}
