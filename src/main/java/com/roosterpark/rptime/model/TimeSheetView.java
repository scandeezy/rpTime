package com.roosterpark.rptime.model;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author scandeezy
 */
public class TimeSheetView extends TimeSheet {
	// private Long id;
	// private Long workerId;
	// private SortedSet<Long> clientIds;
	// private LocalDate startDate;
	// private Integer week;
	// private Integer year;
	// private Integer startDayOfWeek;
	// private LocalDateTime updateTimestamp;
	// private boolean flagged;
	// private TimeSheetStatus status;
	private List<TimeSheetDay> days;

	// private String note;
	//

	public List<TimeSheetDay> getDays() {
		return days;
	}

	public void setDays(List<TimeSheetDay> timeCards) {
		this.days = timeCards;
	}

	@JsonIgnore
	@Override
	public List<Long> getTimeCardIds() {
		return super.getTimeCardIds();
	}

	// @Override
	// public void setTimeCardIds(List<Long> list) {
	//
	// }

	public TimeSheetView() {
		super();
		this.days = new LinkedList<>();
		// this.clientIds = new TreeSet<Long>();
	}

	//
	// public TimeSheetView(TimeSheet sheet) {
	// this();
	// this.id = sheet.getId();
	// this.workerId = sheet.getWorkerId();
	// setClientIds(sheet.getClientIds());
	// this.startDate = sheet.getStartDate();
	// this.week = sheet.getWeek();
	// this.year = sheet.getYear();
	// this.startDayOfWeek = sheet.getStartDayOfWeek();
	// this.note = sheet.getNote();
	// this.days = new LinkedList<>();
	// this.flagged = sheet.isFlagged();
	// this.status = sheet.getStatus();
	// this.updateTimestamp = sheet.getUpdateTimestamp();
	// }
	//
	public TimeSheetView(TimeSheet sheet, List<TimeSheetDay> days) {
		super();
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
	}
	//
	// public Long getId() {
	// return id;
	// }
	//
	// public void setId(Long id) {
	// this.id = id;
	// }
	//
	// public Long getWorkerId() {
	// return workerId;
	// }
	//
	// public void setWorkerId(Long workerId) {
	// this.workerId = workerId;
	// }
	//
	// public SortedSet<Long> getClientIds() {
	// return clientIds;
	// }
	//
	// public void setClientIds(Collection<Long> clientIds) {
	// this.clientIds.clear();
	// this.clientIds.addAll(clientIds);
	// }
	//
	// public LocalDate getStartDate() {
	// return startDate;
	// }
	//
	// public void setStartDate(LocalDate startDate) {
	// this.startDate = startDate;
	// }
	//
	// public Integer getWeek() {
	// return week;
	// }
	//
	// public void setWeek(Integer week) {
	// this.week = week;
	// }
	//
	// public Integer getYear() {
	// return year;
	// }
	//
	// public void setYear(Integer year) {
	// this.year = year;
	// }
	//
	// public Integer getStartDayOfWeek() {
	// return startDayOfWeek;
	// }
	//
	// public void setStartDayOfWeek(Integer startDayOfWeek) {
	// this.startDayOfWeek = startDayOfWeek;
	// }
	//
	// public List<TimeSheetDay> getDays() {
	// return days;
	// }
	//
	// public void setDays(List<TimeSheetDay> timeCards) {
	// this.days = timeCards;
	// }
	//
	// public String getNote() {
	// return note;
	// }
	//
	// public void setNote(String note) {
	// this.note = note;
	// }
	//
	// public TimeSheetStatus getStatus() {
	// return status;
	// }
	//
	// public void setStatus(TimeSheetStatus status) {
	// this.status = status;
	// }
	//
	// public boolean isFlagged() {
	// return flagged;
	// }
	//
	// public void setFlagged(boolean flagged) {
	// this.flagged = flagged;
	// }
	//
	// public LocalDateTime getUpdateTimestamp()
	// {
	// return updateTimestamp;
	// }
	//
	// public void setUpdateTimestamp(LocalDateTime updateTimestamp)
	// {
	// this.updateTimestamp = updateTimestamp;
	// }
}
