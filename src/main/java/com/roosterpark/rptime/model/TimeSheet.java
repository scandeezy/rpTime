package com.roosterpark.rptime.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class TimeSheet {

	public static final String WORKER_KEY = "workerId";
	public static final String CLIENT_KEY = "clientIds";
	public static final String START_DATE_KEY = "startDate";
	public static final String WEEK_KEY = "week";
	public static final String YEAR_KEY = "year";

	@Id
	private Long id;
	@Index
	private Long workerId;
	@Index
	private SortedSet<Long> clientIds;
	@Index
	private LocalDate startDate;
	@Index
	private Integer week;
	@Index
	private Integer year;
	@Index
	private Integer startDayOfWeek;

	private LocalDateTime updateTimestamp;
	private List<Long> timeCardIds;
	private String note;

	/**
	 * required for Objectify.
	 * 
	 * @deprecated use {@code TimeSheet(Long)} instead
	 * */
	@Deprecated
	public TimeSheet() {
		this.updateTimestamp = new LocalDateTime();
		this.timeCardIds = new LinkedList<>();
		this.clientIds = new TreeSet<Long>();
	}

	public TimeSheet(final Long workerId, final Set<Long> clientIds, final LocalDate startDate) {
		this();
		this.workerId = workerId;
		setClientIds(clientIds);
		this.startDate = startDate;
		this.startDayOfWeek = startDate.getDayOfWeek();
		this.week = startDate.getWeekOfWeekyear();
		this.year = startDate.getYear();
	}

	public TimeSheet(final Long workerId, final Set<Long> clientIds, final LocalDate startDate, final List<Long> logIds) {
		this(workerId, clientIds, startDate);
		setTimeCardIds(logIds);
	}

	public TimeSheet(TimeSheetView view) {
		this();
		setId(view.getId());
		setWorkerId(view.getWorkerId());
		setClientIds(view.getClientIds());
		setStartDate(view.getStartDate());
		setWeek(view.getWeek());
		setYear(view.getYear());
		setStartDayOfWeek(view.getStartDayOfWeek());
		setNote(view.getNote());

		for (TimeSheetDay entry : view.getDays()) {
			this.timeCardIds.add(entry.getId());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}

	public Set<Long> getClientIds() {
		return clientIds;
	}

	public void setClientIds(Collection<Long> clientIds) {
		this.clientIds.clear();
		this.clientIds.addAll(clientIds);
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getStartDayOfWeek() {
		return startDayOfWeek;
	}

	public void setStartDayOfWeek(Integer startDayOfWeek) {
		this.startDayOfWeek = startDayOfWeek;
	}

	public LocalDateTime getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(LocalDateTime updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public List<Long> getTimeCardIds() {
		return timeCardIds;
	}

	public void setTimeCardIds(List<Long> timeCardIds) {
		this.timeCardIds.clear();
		this.timeCardIds.addAll(timeCardIds);
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
		//
				.add("id", this.id)
				//
				.add("workerId", this.workerId)
				//
				.add("clientIds", this.clientIds)
				//
				.add("startDate", this.startDate)
				//
				.add("week", this.week)
				//
				.add("year", this.year)
				//
				.add("startDate", this.startDate)
				//
				.add("timeSheetDayIds", this.timeCardIds)
				//
				.toString();
	}
}
