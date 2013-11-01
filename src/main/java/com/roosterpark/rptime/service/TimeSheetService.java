package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityNotFoundException;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roosterpark.rptime.model.Contract;
import com.roosterpark.rptime.model.TimeSheet;

@Named
public class TimeSheetService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	ContractService contractService;

	public List<TimeSheet> createForWorkerDate(Long workerId, LocalDate date) {
		List<Contract> contracts = contractService.getActiveContractsForWorker(workerId, date);
		if (!contracts.isEmpty()) {
			List<TimeSheet> sheets = new LinkedList<>();

			for (Contract contract : contracts) {
				Long clientId = contract.getClient();
				TimeSheet result = new TimeSheet(workerId, clientId, date);
				// result.setWorkerId(contract.getWorker());
				// result.setClientId(contract.getClient());
				// result.setStartDate(new LocalDate());
				result.setWeek(date.getWeekOfWeekyear());
				result.setYear(date.getYear());
				result.setStartDayOfWeek(contract.getStartDayOfWeek());

				set(result);
				sheets.add(result);
				LOGGER.debug("created new TimeSheet for worker={},timesheet={}", workerId, result);
			}

			return sheets;
		}
		throw new EntityNotFoundException("Required 'Worker' not found for '" + workerId
				+ "'.  Solution: create Worker on the /workers page.");
	}

	public List<TimeSheet> getAllForWorker(Long workerId) {
		LOGGER.warn("Getting TimeSheets for workerId={}", workerId);
		return ofy().load().type(TimeSheet.class).filter("workerId", workerId).list();
	}

	public List<TimeSheet> getAll() {
		LOGGER.warn("Getting TimeSheets for admin");
		return ofy().load().type(TimeSheet.class).list();
	}

	public TimeSheet getById(Long id) {
		return ofy().load().type(TimeSheet.class).id(id).now();
	}

	public List<TimeSheet> getClientWeekPage(String client, Integer week, int count, int offset) {
		return ofy().load().type(TimeSheet.class).filter(TimeSheet.CLIENT_KEY, client).filter(TimeSheet.WEEK_KEY, week).limit(count)
				.offset(offset).list();
	}

	public List<TimeSheet> getWorkerWeekPage(String worker, Integer week, int count, int offset) {
		return ofy().load().type(TimeSheet.class).filter(TimeSheet.WORKER_KEY, worker).filter(TimeSheet.WEEK_KEY, week).limit(count)
				.offset(offset).list();
	}

	public List<TimeSheet> getPage(int count, int offset) {
		return ofy().load().type(TimeSheet.class).limit(count).offset(offset).list();
	}

	public void set(TimeSheet item) {
		LOGGER.debug("Saving timesheet {}", item);
		item.setUpdateTimestamp(new LocalDateTime());
		ofy().save().entity(item).now();
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

	public void delete(Long id) {
		TimeSheet c = getById(id);
		ofy().delete().entity(c).now();
	}

}
