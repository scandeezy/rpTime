package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.users.User;
import com.roosterpark.rptime.SheetServlet;
import com.roosterpark.rptime.model.Sheet;

public class SheetService {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	// @Override
	// public Sheet get(String key) throws EntityNotFoundException {
	// Long id = Long.parseLong(key);
	// return get(id);
	// }

	// public Sheet get(Long id) throws EntityNotFoundException {
	// Sheet result = ofy().load().type(Sheet.class).filterKey(id).first().now();
	// if (result == null) {
	// result = new Sheet();
	// ofy().save().entity(result).now();
	// }
	// return result;
	// }

	// @Override
	public List<Sheet> getPage(int count, int offset) {
		return ofy().load().type(Sheet.class).limit(count).offset(offset).list();
	}

	// @Override
	public void set(Sheet item) {
		ofy().save().entity(item).now();
	}

	public List<Sheet> getRecent(User user, int week, int limit) {
		LOGGER.debug("querying Sheets for user={},week={}", user, week);
		List<Sheet> result = ofy()//
				.load()//
				.type(Sheet.class)//
				.filter("userId", user.getUserId())//
				.filter(SheetServlet.WEEK_ATTRIBUTE_KEY + " <=", week)//
				.limit(limit)//
				// .order("-date")//Descending date sort
				.list();
		if (CollectionUtils.isEmpty(result)) {
			LOGGER.debug("creating new Sheet for user={},week={}", user, week);
			result = new ArrayList<Sheet>(1);
			Sheet s = new Sheet(user, week);
			set(s); // persist;
			LOGGER.debug("adding sheet to list: sheet={}", s);
			result.add(s);
		}
		LOGGER.debug("returning List<Sheet>={}", result);
		return result;
	}

}
