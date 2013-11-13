package com.roosterpark.rptime.model;

import org.joda.time.DurationField;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;

/**
 * 
 * @author scandeezy
 */
@Entity
public class TimeCardLogEntry implements Comparable {
	public static final String CARD_ID_KEY = "cardId";
	public static final String CLIENT_ID_KEY = "clientId";
	public static final String DATE_KEY = "date";

	@Id
	private Long id;
	@Index
	private Long cardId;
	@Index
	private Long workerId;
	@Index
	private Long clientId;
	@Index
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
	private Double hours;

	public TimeCardLogEntry() {
	}

	public TimeCardLogEntry(Long workerId, Long clientId, LocalDate date) {
		this.workerId = workerId;
		this.clientId = clientId;
		this.date = date;
		this.startTime = new LocalTime(9, 0);
		this.endTime = new LocalTime(17, 0);
		correctHours();
	}

	public TimeCardLogEntry(Long workerId, Long clientId, LocalDate date, Integer start, Integer end) {
		this(workerId, clientId, date);
		this.startTime = new LocalTime(start, 0);
		this.endTime = new LocalTime(end, 0);
		correctHours();
	}

	@OnSave
	public void correctHours() {
		if (this.endTime != null && this.startTime != null) {
			DurationField dur = this.endTime.getChronology().millis();
			DurationField dur2 = this.startTime.getChronology().millis();
			Minutes minutes = Minutes.minutesBetween(this.startTime, this.endTime);
			this.hours = minutes.getMinutes() / 60.0;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
		correctHours();
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
		correctHours();
	}

	public Double getHours() {
		return hours;
	}

	public void setHours(Double hours) {
		this.hours = hours;
	}

	// Compared strictly based on date and start times.
	@Override
	public int compareTo(Object o) {
		if (o instanceof TimeCardLogEntry) {
			TimeCardLogEntry other = (TimeCardLogEntry) o;

			if (this.getDate().compareTo(other.getDate()) != 0)
				return this.getDate().compareTo(other.getDate());

			return this.getStartTime().compareTo(other.getStartTime());
		}

		return -1;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this) //
				.add("id", this.id) //
				.add("cardId", this.cardId) //
				.add("clientId", this.clientId) //
				.add("date", this.date) //
				.add("endTime", this.endTime) //
				.add("hours", this.hours) //
				.add("startTime", this.startTime) //
				.add("workerId", this.workerId) //
				.toString();
	}

}
