package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.datanucleus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roosterpark.rptime.model.Client;
import com.roosterpark.rptime.model.TimeCardLogEntry;
import com.roosterpark.rptime.model.TimeSheetDay;

@Named
public class ClientService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

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

	public Set<Long> getAvailableForTimeSheetDays(List<TimeSheetDay> days) {
		final Set<Long> result = new HashSet<>();
		for (TimeSheetDay day : days) {
			for (TimeCardLogEntry e : day.getEntries()) {
				result.add(e.getClientId());
			}
		}
		return result;
	}
}
