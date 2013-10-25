package com.roosterpark.rptime.model;

import org.joda.time.LocalDate;

import com.google.common.base.Objects;
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
	private LocalDate start;
	@Index
	private LocalDate end;
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

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
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
		return Objects.toStringHelper(this)//
				.add("id", this.id)//
				.add("worker", this.worker)//
				.add("client", this.client)//
				.add("start", this.start)//
				.add("end", this.end)//
				.toString();
	}
}
