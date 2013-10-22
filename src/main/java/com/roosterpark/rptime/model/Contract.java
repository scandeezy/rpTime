package com.roosterpark.rptime.model;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Contract {
	public static final String WORKER_KEY = "worker";
	public static final String CLIENT_KEY = "client";
	public static final String START_KEY = "start";
	public static final String END_KEY = "end";
	
	@Id
	private Long id;
	@Index
	private Long worker;
	@Index
	private Long client;
	@Index
	private Date start;
	@Index
	private Date end;
	@Index
	private Integer startDayOfWeek;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getWorker() {
		return worker;
	}
	public void setWorker(Long worker) {
		this.worker = worker;
	}
	public Long getClient() {
		return client;
	}
	public void setClient(Long client) {
		this.client = client;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}

	public Integer getStartDayOfWeek() {
		return startDayOfWeek;
	}

	public void setStartDayOfWeek(Integer startDayOfWeek) {
		this.startDayOfWeek = startDayOfWeek;
	}
	
	@Override
	public String toString() {
		return "Contract [id=" + id + ", worker=" + worker + ", client=" + client + ", start="
				+ start + ", end=" + end + "]";
	}
}
