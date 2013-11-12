package com.roosterpark.rptime.model;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * 
 * @author scandeezy
 */
@Entity
public class TimeCardLogEntry implements Comparable {
        public static final String CARD_ID_KEY = "cardId";
    
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

	public TimeCardLogEntry() {
	}

	public TimeCardLogEntry(Long workerId, Long clientId, LocalDate date) {
		this.workerId = workerId;
		this.clientId = clientId;
		this.date = date;
		this.startTime = new LocalTime(9, 0);
		this.endTime = new LocalTime(17, 0);
	}

	public TimeCardLogEntry(Long workerId, Long clientId, LocalDate date, Integer start, Integer end) {
		this(workerId, clientId, date);
		this.startTime = new LocalTime(start, 0);
		this.endTime = new LocalTime(end, 0);
	}

        public Long getId()
        {
            return id;
        }

        public void setId(Long id)
        {
            this.id = id;
        }

        public Long getCardId()
        {
            return cardId;
        }

        public void setCardId(Long cardId)
        {
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
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
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
        public String toString()
        {
            return "TimeCardLogEntry{" 
                    + "workerId=" + workerId 
                    + ", clientId=" + clientId 
                    + ", date=" + date 
                    + ", startTime=" + startTime 
                    + ", endTime=" + endTime 
                    + '}';
        }
}
