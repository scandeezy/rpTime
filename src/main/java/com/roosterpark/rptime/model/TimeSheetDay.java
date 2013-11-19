package com.roosterpark.rptime.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * 
 * @author scandeezy
 */
public class TimeSheetDay {
	private Long id;
	private List<TimeCardLogEntry> entries;

	public TimeSheetDay() {
		this.entries = new LinkedList<>();
	}

	public TimeSheetDay(List<TimeCardLogEntry> entries) {
		this.entries = entries;
		if (CollectionUtils.isNotEmpty(entries)) {
			this.id = entries.get(0).getCardId();
		} else {
			this.id = new Long((long) (Math.random() * 1111111L));
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<TimeCardLogEntry> getEntries() {
		return entries;
	}

	public void addEntry(TimeCardLogEntry entry) {
		this.entries.add(entry);
		Collections.sort(entries);
	}

	public void setEntries(List<TimeCardLogEntry> entries) {
		this.entries = entries;
	}

	@Override
	public String toString() {
		return "TimeSheetDay{" + "id=" + id + ", entries=" + entries + '}';
	}
}
