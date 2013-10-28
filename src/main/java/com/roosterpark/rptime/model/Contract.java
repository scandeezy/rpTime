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
	public static final String START_KEY = "startDate";
	public static final String END_KEY = "endDate";

	@Id
	private Long id;
	@Index
	private Long worker;
	@Index
	private Long client;
        @Index
        private Boolean onSite;
	@Index
	private LocalDate startDate;
	@Index
	private LocalDate endDate;
	private Integer startDayOfWeek;
        private Boolean lunchRequired;

	public Contract() {

	}

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
        
        public Boolean getOnSite() {
                return this.onSite;
        }
        
        public void setOnSite(Boolean onSite) {
                this.onSite = onSite;
        }

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Integer getStartDayOfWeek() {
		return startDayOfWeek;
	}

	public void setStartDayOfWeek(Integer startDayOfWeek) {
		this.startDayOfWeek = startDayOfWeek;
	}
        
        public Boolean getLunchRequired() {
                return this.lunchRequired;
        }
        
        public void setLunchRequired(Boolean lunchRequired) {
                this.lunchRequired = lunchRequired;
        }

	@Override
	public String toString() {
		return Objects.toStringHelper(this)//
				.add("id", this.id)//
				.add("worker", this.worker)//
				.add("client", this.client)//
				.add("startDate", this.startDate)//
				.add("endDate", this.endDate)//
				.toString();
	}
}
