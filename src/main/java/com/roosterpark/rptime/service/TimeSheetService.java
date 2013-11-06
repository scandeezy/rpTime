package com.roosterpark.rptime.service;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityNotFoundException;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roosterpark.rptime.model.Contract;
import com.roosterpark.rptime.model.TimeCardLogEntry;
import com.roosterpark.rptime.model.TimeSheet;
import com.roosterpark.rptime.model.TimeSheetDay;
import com.roosterpark.rptime.model.TimeSheetView;
import com.roosterpark.rptime.service.dao.TimeSheetDao;
import com.roosterpark.rptime.service.dao.TimeSheetDayDao;

@Named
public class TimeSheetService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	ContractService contractService;

	@Inject
	TimeSheetDao timeSheetDao;

	@Inject
	TimeSheetDayDao timeSheetDayDao;

	public List<TimeSheetView> createForWorkerDate(Long workerId, LocalDate date) {
		List<Contract> contracts = contractService.getActiveContractsForWorker(workerId, date);
		if (!contracts.isEmpty()) {
			List<TimeSheetView> views = new LinkedList<>();
			TimeSheetView view = new TimeSheetView();
			List<TimeSheet> sheets = new LinkedList<>();

			for (Contract contract : contracts) {
				views.add(createForWorkerDateContract(workerId, date, contract));
			}

			return views;
		}

		throw new EntityNotFoundException("No active Contracts found for Worker id='" + workerId + "' and date '" + date
				+ "'.  Solution: create Contract on the /contracts page for this Worker with beginDate < " + date + "< endDate.");

	}

	public TimeSheetView createForWorkerDateContract(Long workerId, LocalDate date, Contract contract) {
		// TODO verify this sheet doesn't already exist.
		LOGGER.debug("created new TimeSheet for worker={}, date={}, contract={}", workerId, date, contract);

		List<Long> logIds = new LinkedList<>();
		List<TimeSheetDay> entries = new LinkedList<>();
		for (int i = 0; i < 7; i++) {
			TimeSheetDay day = new TimeSheetDay();
			TimeCardLogEntry entry = new TimeCardLogEntry(workerId, contract.getClient(), date.plusDays(i));
			// Retain the ID
			day.addEntry(entry);
			day = timeSheetDayDao.set(day);
			entries.add(day);
			logIds.add(day.getId());
		}
		Long clientId = contract.getClient();
		LocalDate contractDate = adjustDate(date, contract.getStartDayOfWeek());
		TimeSheet result = new TimeSheet(workerId, clientId, contractDate, logIds);

		result = timeSheetDao.set(result);

		TimeSheetView view = new TimeSheetView(result, entries);

		return view;
	}

	public List<TimeSheetView> getSheetViewsForWorkerPage(Long workerId, Integer count, Integer offset) {
		List<TimeSheet> sheets = timeSheetDao.getSheetViewsForWorkerPage(workerId, count, offset);
		List<TimeSheetView> views = new LinkedList<>();
		for (TimeSheet sheet : sheets) {
			views.add(convert(sheet));
		}

		return views;
	}

	private TimeSheetView convert(TimeSheet sheet) {
		List<TimeSheetDay> entries = timeSheetDayDao.getEntries(sheet.getTimeCardIds());
		// TODO assert the order is consistent
		return new TimeSheetView(sheet, entries);
	}

	private LocalDate adjustDate(LocalDate date, Integer dayOfWeek) {
		int currentDayOfWeek = date.dayOfWeek().get();
		LOGGER.debug("Current day of week {} and needed day of week {}", currentDayOfWeek, dayOfWeek);

		return date.plusDays(dayOfWeek - currentDayOfWeek);
	}

	public List<TimeSheetView> getAll() {
		LOGGER.warn("Getting TimeSheets for admin");
		List<TimeSheet> sheets = timeSheetDao.getAll();
		LOGGER.debug("Found these sheets for all {}", sheets);
		List<TimeSheetView> views = new LinkedList<>();
		for (TimeSheet sheet : sheets) {
			views.add(convert(sheet));
		}

		return views;
	}

	public TimeSheetView getById(Long id) {
		TimeSheet sheet = timeSheetDao.getById(id);

		return convert(sheet);
	}

	// public List<TimeSheet> getClientWeekPage(String client, Integer week, int count, int offset) {
	// return ofy().load().type(TimeSheet.class).filter(TimeSheet.CLIENT_KEY, client).filter(TimeSheet.WEEK_KEY, week).limit(count)
	// .offset(offset).list();
	// }
	//
	// public List<TimeSheet> getWorkerWeekPage(String worker, Integer week, int count, int offset) {
	// return ofy().load().type(TimeSheet.class).filter(TimeSheet.WORKER_KEY, worker).filter(TimeSheet.WEEK_KEY, week).limit(count)
	// .offset(offset).list();
	// }
	//
	// public List<TimeSheet> getPage(int count, int offset) {
	// return ofy().load().type(TimeSheet.class).limit(count).offset(offset).list();
	// }

	public TimeSheetView set(TimeSheetView view) {
		LOGGER.debug("Saving timesheet {}", view);
		List<TimeSheetDay> entries = view.getDays();
		entries = timeSheetDayDao.set(entries);
		view.setDays(entries);
		TimeSheet sheet = new TimeSheet(view);
		sheet = timeSheetDao.set(sheet);

		return new TimeSheetView(sheet, entries);
	}

	public void delete(Long id) {
		TimeSheet c = timeSheetDao.getById(id);
		List<TimeSheetDay> entries = timeSheetDayDao.getEntries(c.getTimeCardIds());

		timeSheetDayDao.delete(c.getTimeCardIds());
		timeSheetDao.delete(c.getId());
	}

}
