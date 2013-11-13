package com.roosterpark.rptime.model;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A view of a {@link TimeSheet} that contains {@link TimeSheetDays} instead of {@code timeSheetDayIds} ({@link Long Long's}).
 * 
 * @author scandeezy
 */
public class TimeSheetView extends TimeSheet {

	private List<TimeSheetDay> days;

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

	public TimeSheetView(final TimeSheet sheet, final List<TimeSheetDay> days) {
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

}
