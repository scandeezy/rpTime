package com.roosterpark.rptime.model;

import java.util.Arrays;

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
	private Integer week;
	@Index
	private Integer startDay;
	@Index
	private Double[] hours;

	public TimeSheet() {
		hours = new Double[7];
	}

	public TimeSheet(Long workerId, Long clientId, Integer week, Integer startDay) {
		this.workerId = workerId;
		this.clientId = clientId;
		this.week = week;
		this.startDay = startDay;
		this.hours = new Double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0};
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

	public Integer getStartDay() {
		return startDay;
	}

	public void setStartDay(Integer startDay) {
		this.startDay = startDay;
	}

	public Double[] getHours() {
		return hours;
	}

	public void setHours(Double[] hours) {
		if(hours == null || hours.length != 7)
			this.hours = new Double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0};
		else
			this.hours = hours;
	}

	@Override
	public String toString() {
		return "Sheet [id=" + id + ", workerId=" + workerId + ", clientId="
				+ clientId + ", week=" + week + ", startDay=" + startDay
				+ ", hours=" + Arrays.toString(hours) + "]";
	}
	
}
