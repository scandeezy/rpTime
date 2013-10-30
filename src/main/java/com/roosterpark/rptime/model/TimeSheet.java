package com.roosterpark.rptime.model;

import org.joda.time.LocalDate;

import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import java.util.LinkedList;
import java.util.List;
import org.joda.time.LocalTime;

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
	@Index
	private List<LocalTime> sundayHours;
	@Index
	private List<LocalTime> mondayHours;
	@Index
	private List<LocalTime> tuesdayHours;
	@Index
	private List<LocalTime> wednesdayHours;
	@Index
	private List<LocalTime> thursdayHours;
	@Index
	private List<LocalTime> fridayHours;
	@Index
	private List<LocalTime> saturdayHours;

	/**
	 * required for Objectify.
	 * 
	 * @deprecated use {@code TimeSheet(Long)} instead
	 * */
	@Deprecated
	public TimeSheet() {
                initHours();
	}

	public TimeSheet(Long workerId, Long clientId, LocalDate startDate) {
                this.workerId = workerId;
		this.clientId = clientId;
		this.startDate = startDate;
		this.week = startDate.getWeekOfWeekyear();
                initHours();
	}
        
        private void initHours() {
                this.sundayHours = new LinkedList<LocalTime>(){{add(LocalTime.MIDNIGHT);add(LocalTime.MIDNIGHT.plusMinutes(1));}};
                this.mondayHours = new LinkedList<LocalTime>(){{add(LocalTime.MIDNIGHT);add(LocalTime.MIDNIGHT.plusMinutes(1));}};
                this.tuesdayHours = new LinkedList<LocalTime>(){{add(LocalTime.MIDNIGHT);add(LocalTime.MIDNIGHT.plusMinutes(1));}};
                this.wednesdayHours = new LinkedList<LocalTime>(){{add(LocalTime.MIDNIGHT);add(LocalTime.MIDNIGHT.plusMinutes(1));}};
                this.thursdayHours = new LinkedList<LocalTime>(){{add(LocalTime.MIDNIGHT);add(LocalTime.MIDNIGHT.plusMinutes(1));}};
                this.fridayHours = new LinkedList<LocalTime>(){{add(LocalTime.MIDNIGHT);add(LocalTime.MIDNIGHT.plusMinutes(1));}};
                this.saturdayHours = new LinkedList<LocalTime>(){{add(LocalTime.MIDNIGHT);add(LocalTime.MIDNIGHT.plusMinutes(1));}};
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

        public Integer getYear()
        {
            return year;
        }

        public void setYear(Integer year)
        {
            this.year = year;
        }

        public Integer getStartDayOfWeek()
        {
            return startDayOfWeek;
        }

        public void setStartDayOfWeek(Integer startDayOfWeek)
        {
            this.startDayOfWeek = startDayOfWeek;
        }

        public List<LocalTime> getSundayHours()
        {
            return sundayHours;
        }

        public void setSundayHours(List<LocalTime> sundayHours)
        {
            this.sundayHours = sundayHours;
        }

        public List<LocalTime> getMondayHours()
        {
            return mondayHours;
        }

        public void setMondayHours(List<LocalTime> mondayHours)
        {
            this.mondayHours = mondayHours;
        }

        public List<LocalTime> getTuesdayHours()
        {
            return tuesdayHours;
        }

        public void setTuesdayHours(List<LocalTime> tuesdayHours)
        {
            this.tuesdayHours = tuesdayHours;
        }

        public List<LocalTime> getWednesdayHours()
        {
            return wednesdayHours;
        }

        public void setWednesdayHours(List<LocalTime> wednesdayHours)
        {
            this.wednesdayHours = wednesdayHours;
        }

        public List<LocalTime> getThursdayHours()
        {
            return thursdayHours;
        }

        public void setThursdayHours(List<LocalTime> thursdayHours)
        {
            this.thursdayHours = thursdayHours;
        }

        public List<LocalTime> getFridayHours()
        {
            return fridayHours;
        }

        public void setFridayHours(List<LocalTime> fridayHours)
        {
            this.fridayHours = fridayHours;
        }

        public List<LocalTime> getSaturdayHours()
        {
            return saturdayHours;
        }

        public void setSaturdayHours(List<LocalTime> saturdayHours)
        {
            this.saturdayHours = saturdayHours;
        }
        
	@Override
	public String toString() {
		return Objects.toStringHelper(this)//
				.add("id", this.id)//
				.add("workerId", this.workerId)//
				.add("clientId", this.clientId)//
				.add("week", this.week)//
				.add("startDate", this.startDate)//
                                .add("sundayHours", this.sundayHours)//
                                .add("mondayHours", this.mondayHours)
                                .add("tuesdayHours", this.tuesdayHours)
                                .add("wednesdayHours", this.wednesdayHours)
                                .add("thursdayHours", this.thursdayHours)
                                .add("fridayHours", this.fridayHours)
                                .add("saturdayHours", this.saturdayHours)
				.toString();
	}

}
