package com.roosterpark.rptime.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.users.UserService;
import com.roosterpark.rptime.exceptions.ContractNotFoundException;
import com.roosterpark.rptime.model.Contract;
import com.roosterpark.rptime.model.TimeCardLogEntry;
import com.roosterpark.rptime.model.TimeSheet;
import com.roosterpark.rptime.model.TimeSheetDay;
import com.roosterpark.rptime.model.TimeSheetStatus;
import com.roosterpark.rptime.model.TimeSheetView;
import com.roosterpark.rptime.model.Worker;
import com.roosterpark.rptime.service.dao.TimeSheetDao;
import com.roosterpark.rptime.service.dao.TimeSheetDayDao;

@Named
public class TimeSheetService {
	private static final boolean DEFAULT_LUNCH_REQUIRED = false;
	private static final int DEFAULT_START_DAY_OF_WEEK = DateTimeConstants.SUNDAY;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	ClientService clientService;
	@Inject
	ContractService contractService;
	@Inject
	UserService userService;
	@Inject
	WorkerService workerService;

	private TimeSheetDao timeSheetDao;

	private TimeSheetDayDao timeSheetDayDao;

	@Inject
	public void setTimeSheetDao(TimeSheetDao timeSheetDao) {
		this.timeSheetDao = timeSheetDao;
	}

	@Inject
	public void setTimeSheetDayDao(TimeSheetDayDao timeSheetDayDao) {
		this.timeSheetDayDao = timeSheetDayDao;
	}

	/**
	 * Get the {@link TimeSheetView} for the {@code workerId} and (normalized) {@code date}.
	 * 
	 * @param workerId
	 * @param date
	 *            - the date whose week to query against
	 * @return the {@link TimeSheetView} for the week containing {@code date}. If it exists, it returns the persisted one; else it is
	 *         created.
	 */
	public TimeSheetView getForWorkerDate(final Long workerId, final LocalDate date) {
		Validate.noNullElements(new Object[] { workerId, date }, "Required: workerId and date");
		final LocalDate normalizedDate = normalizeStartDate(date);
		TimeSheet exists = timeSheetDao.getByWorkerWeekYear(workerId, normalizedDate.getWeekOfWeekyear(), normalizedDate.getYear());
		LOGGER.debug("checking if exists:{}, workerId={}, normalizedDate={}", exists != null, workerId, normalizedDate);
		TimeSheetView result;
		if (exists != null) {
			result = convert(exists);
		} else {
			result = createForWorkerDate(workerId, normalizedDate);
		}
		return result;
	}

	protected TimeSheetView createForWorkerDate(final Long workerId, final LocalDate date) {
		Validate.noNullElements(new Object[] { workerId, date }, "Required: workerId and date");
		final LocalDate normalizedDate = normalizeStartDate(date);
		final Interval interval = normalizeInterval(normalizedDate);
		LOGGER.debug("creating new TimeSheet for worker={}, interval={}", workerId, interval);
		List<Contract> contracts = contractService.getActiveContractsForWorker(workerId, interval);

		if (CollectionUtils.isEmpty(contracts)) {
			throw new ContractNotFoundException(workerId, normalizedDate);
		}

		final Long defaultClientId = contracts.get(0).getClient();

		boolean lunchRequired = DEFAULT_LUNCH_REQUIRED;
		Set<Long> clientIds = new HashSet<>();
		for (Contract contract : contracts) {
			clientIds.add(contract.getClient());
			if (contract.getLunchRequired()) {
				lunchRequired = true;
			}
		}

		LOGGER.debug("defaultClientId={}, lunchRequired={}", defaultClientId, lunchRequired);

		final List<Long> logIds = new LinkedList<>();
		final List<TimeSheetDay> entries = new LinkedList<>();
		LocalDate currentDate = normalizedDate;
		for (int i = 0; i < DateTimeConstants.DAYS_PER_WEEK; i++) {
			TimeSheetDay day = new TimeSheetDay();
			if (isWeekend(currentDate)) {
				TimeCardLogEntry entry = new TimeCardLogEntry(workerId, defaultClientId, currentDate, 12, 12);
				day.addEntry(entry);
			} else if (!lunchRequired) {
				TimeCardLogEntry entry = new TimeCardLogEntry(workerId, defaultClientId, currentDate);
				day.addEntry(entry);
			} else {
				TimeCardLogEntry entry1 = new TimeCardLogEntry(workerId, defaultClientId, currentDate, 8, 12);
				TimeCardLogEntry entry2 = new TimeCardLogEntry(workerId, defaultClientId, currentDate, 13, 17);
				day.addEntry(entry1);
				day.addEntry(entry2);
			}
			day = timeSheetDayDao.set(day);
			entries.add(day);
			logIds.add(day.getId());
			currentDate = currentDate.plusDays(1);
		}
		TimeSheet result = new TimeSheet(workerId, clientIds, normalizedDate, logIds);

		result = timeSheetDao.set(result, null);

		TimeSheetView view = convert(result);
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

	private List<TimeSheetView> convert(List<TimeSheet> sheets) {
		List<TimeSheetView> views = new LinkedList<>();
		for (TimeSheet sheet : sheets) {
			try {
				views.add(convert(sheet));
			} catch (Exception e) {
				LOGGER.warn("error converting TimeSheet id=" + sheet.getId() + ":" + e.getMessage());
			}
		}

		return views;
	}

	private TimeSheetView convert(final TimeSheet sheet) {
		final List<TimeSheetDay> days = timeSheetDayDao.getEntries(sheet.getTimeCardIds());
		LOGGER.debug("converting to TimeSheetView: {}, days={}", sheet, days);
		return new TimeSheetView(sheet, days, timeSheetDao, clientService);
	}

	private Interval normalizeInterval(final LocalDate normalizedDate) {
		Validate.notNull(normalizedDate, "non-null date required");
		Validate.isTrue(normalizedDate.getDayOfWeek() == DEFAULT_START_DAY_OF_WEEK, "normalized date required");
		return new Interval(normalizedDate.toDateTimeAtStartOfDay(), normalizedDate.plusDays(DateTimeConstants.DAYS_PER_WEEK - 1)
				.toDateTimeAtStartOfDay());
	}

	public LocalDate normalizeStartDate(final LocalDate date) {
		Validate.notNull(date, "date required");
		return normalizeStartDate(date, DEFAULT_START_DAY_OF_WEEK);
	}

	@Deprecated
	private LocalDate normalizeStartDate(final LocalDate date, final Integer dayOfWeek) {
		int currentDayOfWeek = date.dayOfWeek().get();
		LOGGER.trace("Current day of week {} and needed day of week {}", currentDayOfWeek, dayOfWeek);
		if (currentDayOfWeek == dayOfWeek) {
			return date;
		}
		int week = date.getWeekOfWeekyear();
		LOGGER.trace("Current week before correction is {}", week);
		LocalDate returnDate;
		if (dayOfWeek == DateTimeConstants.SUNDAY) {
			returnDate = date.minusDays(currentDayOfWeek);
		} else {
			returnDate = date.plusDays(dayOfWeek - currentDayOfWeek);
		}
		LOGGER.trace("The normalized return date is {}", returnDate);
		return returnDate;
	}

	public List<TimeSheetView> getAllAdmin() {
		Validate.isTrue(userService.isUserAdmin(), "Admin required for this operation");
		return convert(timeSheetDao.getAll());
	}

	public List<TimeSheetView> getAllForWorker(final Long workerId) {
		boolean isAdmin = userService.isUserAdmin();
		boolean isCurrentWorker = workerService.isCurrentUserWorkerWithId(workerId);
		if (!(isAdmin || isCurrentWorker)) {
			throw new IllegalArgumentException("Required: Admin or current workerId=" + workerId);
		}
		return convert(timeSheetDao.getAllByWorker(workerId));
	}

	public List<TimeSheetView> getAllForClientWeek(final Long clientId, final LocalDate date) {
		final LocalDate adjustedDate = normalizeStartDate(date);
		final List<TimeSheet> timeSheets = timeSheetDao.getAllForClientWeekYear(clientId, adjustedDate.getWeekOfWeekyear(),
				adjustedDate.getYear());
		return convert(timeSheets);
	}

	public List<TimeSheetView> getAllForClientInInterval(final Long clientId, final Interval searchInterval) {
		final List<TimeSheet> timeSheets = timeSheetDao.getAllForClientInInterval(clientId, searchInterval);
		return convert(timeSheets);
	}

	public List<TimeSheetView> getAllForClient(final Long clientId) {
		return convert(timeSheetDao.getAllForClient(clientId));
	}

	public List<TimeSheetView> getAllForClientYear(final Long clientId, final Integer year) {
		return convert(timeSheetDao.getAllForClientYear(clientId, year));
	}

	public List<TimeSheetView> getAllForClientRange(final Long clientId, final LocalDate start, final LocalDate end) {
		return convert(timeSheetDao.getAllForClientRange(clientId, start, end));
	}

	public TimeSheetView getById(Long id) {
		TimeSheet sheet = timeSheetDao.getById(id);

		return convert(sheet);
	}

	public TimeSheetView getCurrentForWorker(final Long workerId) {
		final List<TimeSheet> list = timeSheetDao.getAllByWorker(workerId);
		final int size = list.size();
		TimeSheetView converted = null;
		for (int i = 0; i < size; i++) {
			final TimeSheet s = list.get(i);
			converted = convert(s);
			final Double hours = getHours(converted);
			if ((hours < 40.0)) {
				LOGGER.debug("returning TimeSheet {} since hours ({}) < 40", s.getId(), hours);
				return converted;
			} else if (TimeSheetStatus.UNSUBMITTED.equals(s.getStatus())) {
				LOGGER.debug("returning TimeSheet {} since status is {}", s.getId(), s.getStatus());
				return converted;
			}
		}
		return converted;
	}

	/**
	 * @param timeSheetView
	 *            - the {@link TimeSheetView} whose {@link TimeCardLogEntry entries}' {@code} hours to sum.
	 * @return the {@link Double summed} {@code hours} found in the input {@link TimeSheet}
	 */
	public Double getHours(TimeSheetView timeSheetView) {
		final List<TimeSheetDay> days = timeSheetView.getDays();
		Double sum = 0.0;
		for (TimeSheetDay day : days) {
			final List<TimeCardLogEntry> entries = day.getEntries();
			for (TimeCardLogEntry entry : entries) {
				if (entry != null) {
					LOGGER.trace("\thours={}, entry={}", sum, entry);
					Double h = entry.getHours();
					if (h != null) {
						sum = sum + h;
					}
				}
			}
		}
		return sum;
	}

	public Map<Long, List<TimeCardLogEntry>> getLogsForClientOverRange(Long clientId, LocalDate start, LocalDate end) {
		return timeSheetDayDao.getForClientOverRange(clientId, start, end);
	}

	/**
	 * @return a "random" {@link TimeSheet}, retrieved by selecting (1) a random {@link Worker} and (2) a related random {@link Contract}
	 *         which determines the date.
	 */
	public TimeSheetView getRandom() {
		final List<Worker> workers = workerService.getAll();
		Validate.isTrue(workers.size() > 0);
		Worker randomWorker = null;
		List<Contract> contracts = null;
		int workersSize = workers.size();
		int randomWorkerNum = (int) Math.floor(Math.random() * ((double) workersSize));

		while (randomWorker == null && CollectionUtils.isEmpty(contracts)) {
			LOGGER.trace("create a random timesheet...");
			randomWorkerNum = (randomWorkerNum + 1) % workersSize;
			randomWorker = workers.get(randomWorkerNum);
			LOGGER.trace("...with with worker[{}/{}]={}", randomWorkerNum, workersSize, randomWorker);
			Long workerId = randomWorker.getId();
			contracts = contractService.getContractsForWorker(workerId);
			if (CollectionUtils.isNotEmpty(contracts)) {
				int contractsSize = contracts.size();
				int randomContract = (int) Math.floor(Math.random() * contractsSize);
				Contract contract = contracts.get(randomContract);
				LOGGER.trace("...with contract[{}/{}]={}", randomContract, contractsSize, contract);
				final LocalDate randomDate = normalizeStartDate(contract.getStartDate().plusDays((int) (Math.random() * 720.0)));
				LOGGER.trace("...for normalized date={}", randomDate);
				TimeSheetView w = getForWorkerDate(workerId, randomDate);
				w.setStartDate(randomDate);
				LOGGER.trace("submitting timesheet={}", w);
				submit(w.getId());
				return w;
			}
		}
		return null;
	}

	/**
	 * @param input
	 *            - the {@link List} to filter
	 * @return the filtered {@link List} of "reportable" (i.e., not-{@link TimeSheetStatus.UNSUBMITTED UNSUBMITTED}) {@link TimeSheetView}s.
	 */
	public List<TimeSheetView> getReportable(final List<TimeSheetView> input) {
		List<TimeSheetView> output = new LinkedList<>();
		for (TimeSheetView tsv : input) {
			if (!tsv.getStatus().equals(TimeSheetStatus.UNSUBMITTED)) {
				output.add(tsv);
			}
		}
		return output;
	}

	public TimeSheetView set(TimeSheetView view) {
		LOGGER.debug("Saving timesheet {}", view);
		List<TimeSheetDay> days = view.getDays();
		days = timeSheetDayDao.set(days);
		view.setDays(days);
		TimeSheet sheet = view.toTimeSheet();
		sheet = timeSheetDao.set(sheet, days);
		return new TimeSheetView(sheet, days, timeSheetDao, clientService);
	}

	public void delete(Long id) {
		TimeSheet c = timeSheetDao.getById(id);

		timeSheetDayDao.delete(c.getTimeCardIds());
		timeSheetDao.delete(c.getId());
	}

	public void submit(Long id) {
		TimeSheet c = timeSheetDao.getById(id);
		c.setStatus(TimeSheetStatus.SUBMITTED);
		timeSheetDao.set(c, null);
	}

	public void flag(final Long id, final Boolean flagged) {
		TimeSheet c = timeSheetDao.getById(id);
		c.setFlagged(flagged);
		timeSheetDao.set(c, null);
	}

	/**
	 * Find {@link TimeSheet TimeSheets} per {@link Worker}, filling in missing {@link TimeSheetStatus} as needed.
	 * 
	 * @param workerId
	 *            The id of the worker.
	 * @return a {@link List<TimeSheet>} of 52 {@link TimeSheet TimeSheets}; 26 weeks prior to {@code date} and 26 after.
	 */
	public List<TimeSheet> getStatusPerWorker(final Long workerId) {
		LocalDate startDate = normalizeStartDate(new LocalDate()).minusWeeks(26);
		final List<TimeSheet> result = new ArrayList<>(52);
		final List<TimeSheet> workerTimeSheets = timeSheetDao.getAllByWorker(workerId);
		LOGGER.debug("found {} workerTimeSheets for workerId={}", workerTimeSheets.size(), workerId);

		for (int i = 0; i < 52; i++) {
			startDate = startDate.plusWeeks(1);
			LOGGER.debug("\ti={}, startDate={}, endDate={}", i, startDate, startDate.plusDays(7));
			// check if already exists
			TimeSheet exists = null;
			for (TimeSheet wts : workerTimeSheets) {
				LOGGER.debug("\t\tcheck if {}=={}", wts.getStartDate(), startDate);
				if (wts.getStartDate().equals(startDate)) {
					LOGGER.debug("found! wts={}", wts);
					exists = wts;
					break;
				}
			}
			if (exists != null) {
				result.add(exists);
			} else {
				TimeSheet t = new TimeSheet(workerId, null, startDate);
				t.setStatus(TimeSheetStatus.NOT_CREATED);
				LOGGER.debug("not found: created t={}", t);
				result.add(t);
			}
		}

		return result;
	}

	private boolean isWeekend(LocalDate currentDate) {
		final int dow = currentDate.getDayOfWeek();
		return dow == DateTimeConstants.SATURDAY || dow == DateTimeConstants.SUNDAY;
	}

}
