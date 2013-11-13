/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roosterpark.rptime.service.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Named;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.Key;
import com.roosterpark.rptime.model.TimeSheet;

/**
 * 
 * @author scandeezy
 */
@Named
public class TimeSheetDao {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public void delete(Long id) {
		TimeSheet sheet = getById(id);

		ofy().delete().entity(sheet);
	}

	public List<TimeSheet> getAll(final Long workerId, final boolean isAdmin) {
		if (isAdmin) {
			return ofy().load().type(TimeSheet.class).list();
		} else {
			return ofy().load().type(TimeSheet.class).filter(TimeSheet.WORKER_KEY, workerId).list();
		}
	}

	public TimeSheet getById(Long id) {
		TimeSheet sheet = ofy().load().type(TimeSheet.class).id(id).now();

		return sheet;
	}

	public List<TimeSheet> getAllByWorker(final Long workerId) {
		LOGGER.debug("Searching for timesheet with workerId {}", workerId);
		return ofy().load().type(TimeSheet.class)//
				.filter(TimeSheet.WORKER_KEY, workerId)//
				.order(TimeSheet.START_DATE_KEY)//
				.list();
	}

	public TimeSheet getByWorkerWeekYear(Long workerId, int week, int year) {
		LOGGER.debug("Searching for timesheet with worker id {}, week {}, and year {}", workerId, week, year);
		return ofy().load().type(TimeSheet.class)//
				.filter(TimeSheet.WORKER_KEY, workerId)//
				.filter(TimeSheet.WEEK_KEY, week)//
				.filter(TimeSheet.YEAR_KEY, year)//
				.first().now();
	}

	public List<TimeSheet> getRecentForWorker(Long workerId, Date date, int limit) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		Integer week = cal.get(Calendar.WEEK_OF_YEAR);
		LOGGER.debug("querying Sheets for worker={},date={},week={}", workerId, date, week);
		List<TimeSheet> result = ofy()//
				.load()//
				.type(TimeSheet.class)//
				.filter(TimeSheet.WORKER_KEY, workerId)//
				.filter(TimeSheet.WEEK_KEY + " <=", week)//
				.limit(limit)//
				// .order("-date")//Descending date sort
				.list();
		LOGGER.debug("returning List<Sheet>={}", result);
		return result;
	}

	public List<TimeSheet> getAllForClient(final Long clientId) {
		List<TimeSheet> sheets = ofy().load().type(TimeSheet.class).filter(TimeSheet.CLIENT_KEY, clientId).order(TimeSheet.START_DATE_KEY)
				.list();

		return sheets;
	}

	public List<TimeSheet> getAllForClientRange(final Long clientId, final LocalDate start, final LocalDate end) {
		List<TimeSheet> sheets = ofy().load().type(TimeSheet.class).filter(TimeSheet.CLIENT_KEY, clientId).order(TimeSheet.START_DATE_KEY)
				.list();

		// Filter outside the query
		List<TimeSheet> retval = new LinkedList<>();
		for (TimeSheet sheet : sheets) {
			LOGGER.debug("Current sheet date {}", sheet.getStartDate());
			LOGGER.debug("Compare to start {}", sheet.getStartDate().compareTo(start));
			LOGGER.debug("Compare to end {}", sheet.getStartDate().compareTo(end));
			if (sheet.getStartDate().compareTo(start) >= 0 && sheet.getStartDate().compareTo(end) <= 0)
				retval.add(sheet);
		}

		return retval;
	}

	public List<TimeSheet> getAllForClientInInterval(final Long clientId, final Interval searchInterval) {
		final LocalDate startDate = searchInterval.getStart().toLocalDate();
		final LocalDate endDate = searchInterval.getEnd().toLocalDate();
		LOGGER.debug("searching for TimeSheets with clientId={}, betweeen {} and {}", new Object[] { clientId, startDate, endDate });
		final List<TimeSheet> sheets = ofy().load().type(TimeSheet.class)//
				.filter(TimeSheet.CLIENT_KEY, clientId) //
				.filter(TimeSheet.START_DATE_KEY + " >=", startDate) //
				.filter(TimeSheet.START_DATE_KEY + " <=", endDate) //
				.order(TimeSheet.START_DATE_KEY)//
				.list();
		return sheets;
	}

	public List<TimeSheet> getAllForClientYear(final Long clientId, final Integer year) {
		List<TimeSheet> sheets = ofy().load().type(TimeSheet.class).filter(TimeSheet.CLIENT_KEY, clientId).filter(TimeSheet.YEAR_KEY, year)
				.order(TimeSheet.START_DATE_KEY).list();

		return sheets;
	}

	public List<TimeSheet> getAllForClientWeekYear(final Long clientId, final Integer week, final Integer year) {
		List<TimeSheet> sheets = ofy().load().type(TimeSheet.class)//
				.filter(TimeSheet.CLIENT_KEY, clientId)//
				.filter(TimeSheet.WEEK_KEY, week)//
				.filter(TimeSheet.YEAR_KEY, year)//
				.order(TimeSheet.START_DATE_KEY)//
				.list();

		return sheets;
	}

	public List<TimeSheet> getSheetViewsForWorkerPage(Long workerId, Integer count, Integer offset) {
		List<TimeSheet> sheets = ofy().load().type(TimeSheet.class).filter(TimeSheet.WORKER_KEY, workerId).order(TimeSheet.START_DATE_KEY)
				.limit(count).offset(offset).list();
		return sheets;
	}

	public TimeSheet set(TimeSheet item) {
		LOGGER.debug("Saving timesheet {}", item);
		item.setUpdateTimestamp(new LocalDateTime());
		Key<TimeSheet> sheetKey = ofy().save().entity(item).now();
		LOGGER.debug("Got key {}", sheetKey);
		item = ofy().load().key(sheetKey).now();
		LOGGER.debug("Fetched back sheet {}", item);
		return item;
	}

}
