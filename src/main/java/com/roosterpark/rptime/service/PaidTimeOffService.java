package com.roosterpark.rptime.service;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.googlecode.objectify.ObjectifyFactory;
import com.roosterpark.rptime.model.Client;

/**
 * Service that instantiates (or loads) a paid time off {@link Client}.
 * 
 * @author jjzabkar
 */
@Named
public class PaidTimeOffService implements Serializable {

	private static final long serialVersionUID = -7015834490721203964L;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private ObjectifyFactory ofy;

	private Client ptoClient;

	/**
	 * @param ofactory
	 *            - required to ensure proper startup ordering. Objectify registers the DAOs.
	 */
	@Inject
	public PaidTimeOffService(ObjectifyFactory ofactory) {
		this.ofy = ofactory;
	}

	@PostConstruct
	public void init() {
		Client c = ofy.begin().load().type(Client.class).id(serialVersionUID).now();
		if (c == null) {
			c = new Client();
			c.setId(serialVersionUID);
			c.setName("Paid time off");
			c.setLunchRequired(false);
			c.setStartDayOfWeek(0);
			c.setEditable(false);
			ofy.begin().save().entity(c).now();
		}
		c.setEditable(false);
		ptoClient = c;
		LOGGER.info("init {}", this);
	}

	public Client getPtoClient() {
		return ptoClient;
	}

	public String toString() {
		return Objects.toStringHelper(this)//
				.add("ptoClient", this.ptoClient)//
				.toString();
	}

}
