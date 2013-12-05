package com.roosterpark.rptime.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roosterpark.rptime.service.ClientService;
import com.roosterpark.rptime.service.dao.TimeSheetDao;

/**
 * A view of a {@link TimeSheet} that contains {@link TimeSheetDays} instead of {@code timeSheetDayIds} ({@link Long Long's}).
 * 
 * @author scandeezy
 */
public class TimeSheetView extends TimeSheet {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeSheet.class);

	private Set<Client> availableClients;
	private Set<Long> selectedClientIds;
	private boolean currentTimeSheet;
	private List<TimeSheetDay> days;
	private Long nextTimeSheetId;
	private Long previousTimeSheetId;

	@SuppressWarnings("deprecation")
	public TimeSheetView() {
		super();
		this.days = new LinkedList<>();
		this.availableClients = new HashSet<>();
		this.selectedClientIds = new HashSet<>();
	}

	public TimeSheetView(final TimeSheet sheet, final List<TimeSheetDay> days, final TimeSheetDao dao, final ClientService clientService) {
		this();
		Validate.notNull(sheet.getStartDate(), "startDate required");

		this.setAdminNote(sheet.getAdminNote());
		this.setClientIds(sheet.getClientIds());
		this.setDays(days);
		this.setFlagged(sheet.isFlagged());
		this.setId(sheet.getId());
		this.setNote(sheet.getNote());
		this.setStartDate(sheet.getStartDate());
		this.setStartDayOfWeek(sheet.getStartDayOfWeek());
		this.setStatus(sheet.getStatus());
		this.setTimeCardIds(sheet.getTimeCardIds());
		this.setUpdateTimestamp(sheet.getUpdateTimestamp());
		this.setWeek(sheet.getWeek());
		this.setWorkerId(sheet.getWorkerId());
		this.setYear(sheet.getYear());

		// "my" (non-TimeSheet fields)

		final Interval timeSheetInterval = new Interval(sheet.getStartDate().toDateTimeAtStartOfDay(), sheet.getStartDate().plusDays(7)
				.toDateTimeAtStartOfDay());

		final Set<Client> availableClients = clientService.getAvailableForWorkerIntervalDays(sheet.getWorkerId(), timeSheetInterval, days);
		LOGGER.debug("found {} availableClients for TimeSheet.id={}", CollectionUtils.size(availableClients), sheet.getId());
		final Set<Long> selectedClientIds = clientService.getSelectedIdsForTimeSheetDays(days);
		LOGGER.debug("found {} selectedClientIds for TimeSheet.id={}", CollectionUtils.size(selectedClientIds), sheet.getId());
		this.setAvailableClients(availableClients);
		this.setSelectedClientIds(selectedClientIds);

		if (sheet.getStartDate() != null) {
			LocalDate prev = sheet.getStartDate().minusWeeks(1);
			LocalDate next = sheet.getStartDate().plusWeeks(1);
			TimeSheet t = dao.getByWorkerWeekYear(getWorkerId(), prev.getWeekOfWeekyear(), prev.getYear());
			if (t != null) {
				this.setPreviousTimeSheetId(t.getId());
			}
			t = dao.getByWorkerWeekYear(getWorkerId(), next.getWeekOfWeekyear(), next.getYear());
			if (t != null) {
				this.setNextTimeSheetId(t.getId());
			} else {
				this.setCurrentTimeSheet(true);
			}
		}
	}

	public List<TimeSheetDay> getDays() {
		return days;
	}

	public void setDays(List<TimeSheetDay> timeSheetDays) {
		this.days = timeSheetDays;
		super.getTimeCardIds().clear();
		for (TimeSheetDay d : timeSheetDays) {
			super.getTimeCardIds().add(d.getId());
		}
	}

	@JsonIgnore
	@Override
	public List<Long> getTimeCardIds() {
		return super.getTimeCardIds();
	}

	public TimeSheet toTimeSheet() {
		@SuppressWarnings("deprecation")
		final TimeSheet result = new TimeSheet();
		result.setAdminNote(this.getAdminNote());
		result.setClientIds(this.getClientIds());
		result.setFlagged(this.isFlagged());
		result.setId(this.getId());
		result.setNote(this.getNote());
		result.setStartDate(this.getStartDate());
		result.setStartDayOfWeek(this.getStartDayOfWeek());
		result.setStatus(this.getStatus());
		result.setTimeCardIds(this.getTimeCardIds());
		result.setUpdateTimestamp(this.getUpdateTimestamp());
		result.setWeek(this.getWeek());
		result.setWorkerId(this.getWorkerId());
		result.setYear(this.getYear());
		return result;
	}

	public boolean isCurrentTimeSheet() {
		return currentTimeSheet;
	}

	public Set<Client> getAvailableClients() {
		return availableClients;
	}

	public void setAvailableClients(Collection<Client> availableClients) {
		this.availableClients.clear();
		this.availableClients.addAll(availableClients);
	}

	public Set<Long> getSelectedClientIds() {
		return selectedClientIds;
	}

	public void setSelectedClientIds(Collection<Long> selectedClientIds) {
		this.selectedClientIds.clear();
		this.selectedClientIds.addAll(selectedClientIds);
	}

	public void setCurrentTimeSheet(boolean currentTimeSheet) {
		this.currentTimeSheet = currentTimeSheet;
	}

	public Long getNextTimeSheetId() {
		return nextTimeSheetId;
	}

	public void setNextTimeSheetId(Long nextTimeSheetId) {
		this.nextTimeSheetId = nextTimeSheetId;
	}

	public Long getPreviousTimeSheetId() {
		return previousTimeSheetId;
	}

	public void setPreviousTimeSheetId(Long previousTimeSheetId) {
		this.previousTimeSheetId = previousTimeSheetId;
	}

}
