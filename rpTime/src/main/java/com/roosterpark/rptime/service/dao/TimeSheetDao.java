/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roosterpark.rptime.service.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.Key;
import com.roosterpark.rptime.model.TimeCardLogEntry;
import com.roosterpark.rptime.model.TimeSheet;
import com.roosterpark.rptime.model.TimeSheetDay;

/**
 * 
 * @author scandeezy
 */
@Named
public class TimeSheetDao {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	TimeSheetDayDao timeSheetDayDao;

	public void delete(Long id) {
		TimeSheet sheet = getById(id);

		ofy().delete().entity(sheet);
	}

	public List<TimeSheet> getAll(final Long workerId, final boolean isAdmin) {
		if (isAdmin) {
			LOGGER.debug("returning all timesheets for admin");
			return ofy().load().type(TimeSheet.class).list();
		} else {
			LOGGER.debug("returning all timesheets for worker={}", workerId);
			return ofy().load().type(TimeSheet.class)//
					.filter(TimeSheet.WORKER_ID_KEY, workerId)//
					.list();
		}
	}

	public TimeSheet getById(Long id) {
		TimeSheet sheet = ofy().load().type(TimeSheet.class).id(id).now();

		return sheet;
	}

	public List<TimeSheet> getAll() {
		return ofy().load().type(TimeSheet.class)//
				.list();
	}

	public List<TimeSheet> getAllByWorker(final Long workerId) {
		LOGGER.debug("Searching for timesheet with workerId {}", workerId);
		return ofy().load().type(TimeSheet.class)//
				.filter(TimeSheet.WORKER_ID_KEY, workerId)//
				// .order(TimeSheet.START_DATE_KEY)// throws DatastoreNeedIndexException
				.list();
	}

	public TimeSheet getByWorkerWeekYear(Long workerId, int week, int year) {
		LOGGER.debug("Searching for timesheet with worker id {}, week {}, and year {}", workerId, week, year);
		return ofy().load().type(TimeSheet.class)//
				.filter(TimeSheet.WORKER_ID_KEY, workerId)//
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
				.filter(TimeSheet.WORKER_ID_KEY, workerId)//
				.filter(TimeSheet.WEEK_KEY + " <=", week)//
				.limit(limit)//
				// .order("-date")//Descending date sort
				.list();
		LOGGER.debug("returning List<Sheet>={}", result);
		return result;
	}

	public List<TimeSheet> getAllForClient(final Long clientId) {
		List<TimeSheet> sheets = ofy().load().type(TimeSheet.class)//
				.filter(TimeSheet.CLIENT_IDS_KEY, clientId)//
				.order(TimeSheet.START_DATE_KEY).list();
		return sheets;
	}

	public List<TimeSheet> getAllForClientRange(final Long clientId, final LocalDate start, final LocalDate end) {
		final List<TimeSheet> sheets = getAllForClient(clientId);
		return filterAndSortDates(start, end, sheets);
	}

	private List<TimeSheet> filterAndSortDates(final LocalDate start, final LocalDate end, final List<TimeSheet> sheets) {
		LOGGER.warn("Warning: pushed date filtering to filterDates() method.");
		final List<TimeSheet> retval = new LinkedList<>();
		for (TimeSheet sheet : sheets) {
			LOGGER.trace("Current sheet date {}", sheet.getStartDate());
			LOGGER.trace("Compare to start {}", sheet.getStartDate().compareTo(start));
			LOGGER.trace("Compare to end {}", sheet.getStartDate().compareTo(end));
			if (sheet.getStartDate().compareTo(start) >= 0 && sheet.getStartDate().compareTo(end) <= 0)
				retval.add(sheet);
		}
		Collections.sort(retval);
		return retval;
	}

	public List<TimeSheet> getAllForClientInInterval(final Long clientId, final Interval searchInterval) {
		final LocalDate startDate = searchInterval.getStart().toLocalDate();
		final LocalDate endDate = searchInterval.getEnd().toLocalDate();
		LOGGER.debug("searching for TimeSheets with clientId={}, betweeen {} and {}", new Object[] { clientId, startDate, endDate });
		final List<TimeSheet> sheets = ofy().load().type(TimeSheet.class)//
				.filter(TimeSheet.CLIENT_IDS_KEY, clientId) //
				// .filter(TimeSheet.START_DATE_KEY + " >=", startDate) //
				// .filter(TimeSheet.START_DATE_KEY + " <=", endDate) //
				// .order(TimeSheet.START_DATE_KEY)//
				.list();
		return filterAndSortDates(startDate, endDate, sheets);
		// return sheets;
	}

	public List<TimeSheet> getAllForClientYear(final Long clientId, final Integer year) {
		List<TimeSheet> sheets = ofy().load().type(TimeSheet.class)//
				.filter(TimeSheet.CLIENT_IDS_KEY, clientId)//
				.filter(TimeSheet.YEAR_KEY, year)//
				.order(TimeSheet.START_DATE_KEY)//
				.list();

		return sheets;
	}

	public List<TimeSheet> getAllForClientWeekYear(final Long clientId, final Integer week, final Integer year) {
		List<TimeSheet> sheets = ofy().load().type(TimeSheet.class)//
				.filter(TimeSheet.CLIENT_IDS_KEY, clientId)//
				.filter(TimeSheet.WEEK_KEY, week)//
				.filter(TimeSheet.YEAR_KEY, year)//
				.order(TimeSheet.START_DATE_KEY)//
				.list();

		return sheets;
	}

	public List<TimeSheet> getSheetViewsForWorkerPage(Long workerId, Integer count, Integer offset) {
		List<TimeSheet> sheets = ofy().load().type(TimeSheet.class).filter(TimeSheet.WORKER_ID_KEY, workerId)
				.order(TimeSheet.START_DATE_KEY).limit(count).offset(offset).list();
		return sheets;
	}

	/**
	 * @param item
	 *            - the {@link TimeSheet} to persist.
	 * @param days
	 *            - (optional) if present, the days' entries will be used to calculate the {@code clientIds}.
	 * @return the saved {@link TimeSheet} entity.
	 */
	public TimeSheet set(TimeSheet item, final List<TimeSheetDay> days) {
		// client id computation logic
		if (CollectionUtils.isNotEmpty(days)) {
			LOGGER.debug("computing clients");
			HashSet<Long> clientIdsSet = new HashSet<>();
			for (TimeSheetDay day : days) {
				final List<TimeCardLogEntry> entries = day.getEntries();
				for (TimeCardLogEntry entry : entries) {
					clientIdsSet.add(entry.getClientId());
				}
			}
			item.setClientIds(clientIdsSet);

			LOGGER.debug("set {} clientIds", CollectionUtils.size(clientIdsSet));
		}
		// save logic
		LOGGER.debug("Saving timesheet {}", item);
		item.setUpdateTimestamp(new LocalDateTime());
		Key<TimeSheet> sheetKey = ofy().save().entity(item).now();
		LOGGER.debug("Got key {}", sheetKey);
		item = ofy().load().key(sheetKey).now();
		LOGGER.debug("Fetched back sheet {}", item);
		return item;
	}

}
