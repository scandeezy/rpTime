package com.roosterpark.rptime.model;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roosterpark.rptime.service.dao.TimeSheetDao;

/**
 * A view of a {@link TimeSheet} that contains {@link TimeSheetDays} instead of {@code timeSheetDayIds} ({@link Long Long's}).
 * 
 * @author scandeezy
 */
public class TimeSheetView extends TimeSheet {

	private boolean currentTimeSheet;
	private List<TimeSheetDay> days;
	private Long nextTimeSheetId;
	private Long previousTimeSheetId;

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

	public TimeSheetView() {
		super();
		this.days = new LinkedList<>();
	}

	public TimeSheetView(final TimeSheet sheet, final List<TimeSheetDay> days, final TimeSheetDao dao) {
		this();
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

	public TimeSheet toTimeSheet() {
		final TimeSheet result = new TimeSheet();
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
