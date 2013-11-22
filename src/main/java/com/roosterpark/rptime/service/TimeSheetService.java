package com.roosterpark.rptime.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.users.UserService;
import com.roosterpark.rptime.model.Contract;
import com.roosterpark.rptime.model.TimeCardLogEntry;
import com.roosterpark.rptime.model.TimeSheet;
import com.roosterpark.rptime.model.TimeSheetDay;
import com.roosterpark.rptime.model.TimeSheetStatus;
import com.roosterpark.rptime.model.TimeSheetView;
import com.roosterpark.rptime.service.dao.TimeSheetDao;
import com.roosterpark.rptime.service.dao.TimeSheetDayDao;

@Named
public class TimeSheetService {
	private static final boolean DEFAULT_LUNCH_REQUIRED = false;

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

	private Long ptoClientId;

	@Inject
	public void setPtoClientId(PaidTimeOffService ptoService) {
		ptoClientId = ptoService.getPtoClient().getId();
	}

	@Inject
	public void setTimeSheetDao(TimeSheetDao timeSheetDao) {
		this.timeSheetDao = timeSheetDao;
	}

	@Inject
	public void setTimeSheetDayDao(TimeSheetDayDao timeSheetDayDao) {
		this.timeSheetDayDao = timeSheetDayDao;
	}

	/**
	 * @param workerId
	 * @param date
	 *            - the date whose week to query against
	 * @return the {@link TimeSheetView} for the week containing {@code date}. If it exists, it returns the persisted one; else it is
	 *         created.
	 */
	public TimeSheetView getForWorkerDate(Long workerId, LocalDate date) {
		date = adjustDate(date, DateTimeConstants.SUNDAY);
		Validate.noNullElements(new Object[] { workerId, date }, "Required: workerId and date");
		TimeSheet exists = timeSheetDao.getByWorkerWeekYear(workerId, date.getWeekOfWeekyear(), date.getYear());
		TimeSheetView result;
		if (exists != null) {
			result = convert(exists);
		} else {
			result = createForWorkerDate(workerId, date);
		}
		return result;
	}

	protected TimeSheetView createForWorkerDate(final Long workerId, final LocalDate date) {
		Validate.noNullElements(new Object[] { workerId, date }, "Required: workerId and date");

		TimeSheet exists = timeSheetDao.getByWorkerWeekYear(workerId, date.getWeekOfWeekyear(), date.getYear());
		if (exists != null) {
			LOGGER.info("TimeSheet already exists for date {}; returning {}", date, exists.getId());
			return convert(exists);
		}
		final Interval interval = new Interval(date.toDateTimeAtStartOfDay(), date.plusDays(DateTimeConstants.DAYS_PER_WEEK - 1)
				.toDateTimeAtStartOfDay());
		LOGGER.debug("creating new TimeSheet for worker={}, interval={}", workerId, interval);
		List<Contract> contracts = contractService.getActiveContractsForWorker(workerId, interval);

		if (CollectionUtils.isEmpty(contracts)) {
			throw new EntityNotFoundException("No active Contracts found for Worker id='" + workerId + "' and date '" + date
					+ "'.  Solution: for Worker, create Contract on the /contracts page with beginDate < " + date + "< endDate.");
		}

		boolean lunchRequired = DEFAULT_LUNCH_REQUIRED;
		final Long defaultClientId = contracts.get(0).getClient();
		if (CollectionUtils.size(contracts) == 1) {
			lunchRequired = contracts.get(0).getLunchRequired();
		}

		LOGGER.debug("defaultClientId={}, lunchRequired={}", defaultClientId, lunchRequired);

		Set<Long> clientIds = new HashSet<Long>();
		for (Contract contract : contracts) {
			clientIds.add(contract.getClient());
		}

		// Normalized/Hardcoded because of our current data layer.
		final LocalDate contractDate = adjustDate(date, DateTimeConstants.SUNDAY);

		// Long defaultClientId = clientIds.get(0);
		final List<Long> logIds = new LinkedList<>();
		final List<TimeSheetDay> entries = new LinkedList<>();
		for (int i = 0; i < DateTimeConstants.DAYS_PER_WEEK; i++) {
			TimeSheetDay day = new TimeSheetDay();
			int dayOfWeek = contractDate.plusDays(i).getDayOfWeek();

			boolean weekend = (dayOfWeek == DateTimeConstants.SATURDAY || dayOfWeek == DateTimeConstants.SUNDAY);
			LOGGER.debug("Saturday is {}, Sunday is {}", DateTimeConstants.SATURDAY, DateTimeConstants.SUNDAY);
			LOGGER.debug("Day {} is weekend {}", dayOfWeek, weekend);
			if (weekend) {
				TimeCardLogEntry entry = new TimeCardLogEntry(workerId, defaultClientId, contractDate.plusDays(i), 12, 12);
				day.addEntry(entry);
			} else if (!lunchRequired) {
				TimeCardLogEntry entry = new TimeCardLogEntry(workerId, defaultClientId, contractDate.plusDays(i));
				day.addEntry(entry);
			} else {
				TimeCardLogEntry entry1 = new TimeCardLogEntry(workerId, defaultClientId, contractDate.plusDays(i), 8, 12);
				TimeCardLogEntry entry2 = new TimeCardLogEntry(workerId, defaultClientId, contractDate.plusDays(i), 13, 17);
				day.addEntry(entry1);
				day.addEntry(entry2);
			}
			day = timeSheetDayDao.set(day);
			entries.add(day);
			logIds.add(day.getId());
		}
		TimeSheet result = new TimeSheet(workerId, clientIds, contractDate, logIds);

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
			views.add(convert(sheet));
		}

		return views;
	}

	private TimeSheetView convert(TimeSheet sheet) {
		final List<TimeSheetDay> days = timeSheetDayDao.getEntries(sheet.getTimeCardIds());
		final Set<Long> activeClientIds = clientService.getAvailableForTimeSheetDays(days);
		activeClientIds.add(ptoClientId);
		LOGGER.debug("converting to TimeSheetView: {}, days={}", sheet, days);
		// TODO assert the order is consistent
		return new TimeSheetView(sheet, days, timeSheetDao, activeClientIds);
	}

	private LocalDate adjustDate(LocalDate date, Integer dayOfWeek) {
		int currentDayOfWeek = date.dayOfWeek().get();
		LOGGER.debug("Current day of week {} and needed day of week {}", currentDayOfWeek, dayOfWeek);
		if (currentDayOfWeek == dayOfWeek) {
			return date;
		}
		LocalDate returnDate = date.withDayOfWeek(dayOfWeek);
		// Edge condition
		if (dayOfWeek == DateTimeConstants.SUNDAY) {
			int week = date.getWeekOfWeekyear();
			LOGGER.debug("Current week {} and desired week {}", week, week - 1);
			returnDate = returnDate.withWeekOfWeekyear(week - 1);
		}
		LOGGER.debug("The adjusted return date is {}", returnDate);
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
		final LocalDate adjustedDate = adjustDate(date, DateTimeConstants.SUNDAY);
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

	/**
	 * @param input
	 *            - the {@link List} to filter
	 * @return the filtered {@link List} of "reportable" (i.e., not-{@link TimeSheetStatus.UNSUBMITTED UNSUBMITTED}) {@link TimeSheetView}s.
	 */
	public List<TimeSheetView> getReportable(final List<TimeSheetView> input) {
		List<TimeSheetView> output = new LinkedList<TimeSheetView>();
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
		Set<Long> activeClientIds = clientService.getAvailableForTimeSheetDays(days);
		return new TimeSheetView(sheet, days, timeSheetDao, activeClientIds);
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

	public Map<Long, List<TimeCardLogEntry>> getLogsForClientOverRange(Long clientId, LocalDate start, LocalDate end) {
		return timeSheetDayDao.getForClientOverRange(clientId, start, end);
	}

	public void flag(final Long id, final Boolean flagged) {
		TimeSheet c = timeSheetDao.getById(id);
		c.setFlagged(flagged);
		timeSheetDao.set(c, null);
	}

}
