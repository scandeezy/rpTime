package com.roosterpark.rptime.model;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class TimeSheet {

	public static final String WORKER_KEY = "workerId";
	public static final String CLIENT_KEY = "clientId";
	public static final String WEEK_KEY = "week";

	@Id
	private Long id;
	@Index
	private Long workerId;
	@Index
	private Long clientId;
	@Index
	private LocalDate startDate;
	@Index
	private Integer week;
	@Index
	private Integer year;
	@Index
	private Integer startDayOfWeek;

	private LocalDateTime updateTimestamp;

	private List<LocalDate> days;
	private List<LocalTime> startTimes;
	private List<LocalTime> endTimes;

	/**
	 * required for Objectify.
	 * 
	 * @deprecated use {@code TimeSheet(Long)} instead
	 * */
	@Deprecated
	public TimeSheet() {
		this.updateTimestamp = new LocalDateTime();
		this.days = new LinkedList<LocalDate>();
		this.startTimes = new LinkedList<LocalTime>();
		this.endTimes = new LinkedList<LocalTime>();
	}

	public TimeSheet(Long workerId, Long clientId, LocalDate startDate) {
		this();
		this.workerId = workerId;
		this.clientId = clientId;
		this.startDate = startDate;
                this.startDayOfWeek = startDate.getDayOfWeek();
		this.week = startDate.getWeekOfWeekyear();
                this.year = startDate.getYear();

		LocalDate d = new LocalDate(startDate);
		for (int i = 0; i < 7; i++) {
			days.add(d);
			startTimes.add(new LocalTime(9, 0));
			endTimes.add(new LocalTime(17, 0));
			d = d.plusDays(1);
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

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
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

	@Override
	public String toString() {
		return Objects
				.toStringHelper(this)
				//
				.add("id", this.id)
				//
				.add("workerId", this.workerId)
				//
				.add("clientId", this.clientId)
				//
				.add("week", this.week)
				//
				.add("startDate", this.startDate)
				//
				.toString();
	}

	public LocalDateTime getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(LocalDateTime updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public List<LocalDate> getDays() {
		return days;
	}

	public void setDays(List<LocalDate> days) {
		this.days = days;
	}

	public List<LocalTime> getStartTimes() {
		return startTimes;
	}

	public void setStartTimes(List<LocalTime> startTimes) {
		this.startTimes = startTimes;
	}

	public List<LocalTime> getEndTimes() {
		return endTimes;
	}

	public void setEndTimes(List<LocalTime> endTimes) {
		this.endTimes = endTimes;
	}

}
