package com.roosterpark.rptime.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.LocalDate;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 *
 * @author scandeezy
 */
public class TimeSheetView
{

	@Id
	private Long id;
	@Index
	private Long workerId;
	@Index
	private SortedSet<Long> clientIds;
	@Index
	private LocalDate startDate;
	@Index
	private Integer week;
	@Index
	private Integer year;
	@Index
	private Integer startDayOfWeek;

        private List<TimeSheetDay> days;
	private String note;

        public TimeSheetView() {
            this.days = new LinkedList<>();
            this.clientIds = new TreeSet<Long>();
        }
        
        public TimeSheetView(TimeSheet sheet) {
        	this();
                this.id = sheet.getId();
                this.workerId = sheet.getWorkerId();
                setClientIds(sheet.getClientIds());
                this.startDate = sheet.getStartDate();
                this.week = sheet.getWeek();
                this.year = sheet.getYear();
                this.startDayOfWeek = sheet.getStartDayOfWeek();
                this.note = sheet.getNote();
                this.days = new LinkedList<>();
        }
        
        public TimeSheetView(TimeSheet sheet, List<TimeSheetDay> logs) {
                this(sheet);
                this.days = logs;
        }

        public Long getId()
        {
            return id;
        }

        public void setId(Long id)
        {
            this.id = id;
        }

        public Long getWorkerId()
        {
            return workerId;
        }

        public void setWorkerId(Long workerId)
        {
            this.workerId = workerId;
        }

        public SortedSet<Long> getClientIds()
        {
            return clientIds;
        }

        public void setClientIds(Collection<Long> clientIds)
        {
        	this.clientIds.clear();
        	this.clientIds.addAll(clientIds);
        }

        public LocalDate getStartDate()
        {
            return startDate;
        }

        public void setStartDate(LocalDate startDate)
        {
            this.startDate = startDate;
        }

        public Integer getWeek()
        {
            return week;
        }

        public void setWeek(Integer week)
        {
            this.week = week;
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

        public List<TimeSheetDay> getDays()
        {
            return days;
        }

        public void setDays(List<TimeSheetDay> timeCards)
        {
            this.days = timeCards;
        }

        public String getNote()
        {
            return note;
        }

        public void setNote(String note)
        {
            this.note = note;
        }
}
