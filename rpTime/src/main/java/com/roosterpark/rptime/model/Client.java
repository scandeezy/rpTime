package com.roosterpark.rptime.model;

import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Client {
	@Id
	private Long id;
	@Index
	private String name;
	@Index
	private boolean lunchRequired;
	@Index
	private Integer startDayOfWeek;
	private boolean editable;

	public Client() {
		setEditable(true);
	}

	public Client(String name, Boolean lunchRequired, Integer startDayOfWeek) {
		this();
		this.name = name;
		this.lunchRequired = lunchRequired;
		this.startDayOfWeek = startDayOfWeek;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getLunchRequired() {
		return this.lunchRequired;
	}

	public void setLunchRequired(Boolean lunchRequired) {
		this.lunchRequired = lunchRequired;
	}

	public Integer getStartDayOfWeek() {
		return startDayOfWeek;
	}

	public void setStartDayOfWeek(Integer startDayOfWeek) {
		this.startDayOfWeek = startDayOfWeek;
	}

	public String toString() {
		return Objects.toStringHelper(this)//
				.add("id", this.id)//
				.add("name", this.name)//
				.add("lunchRequired", this.lunchRequired)//
				.add("startDayOfWeek", this.startDayOfWeek)//
				.toString();
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
}
