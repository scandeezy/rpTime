package com.roosterpark.rptime.model;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import java.util.LinkedList;
import java.util.List;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

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
	private List<Long> clientIds;
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
        }
        
        public TimeSheetView(TimeSheet sheet) {
                this.id = sheet.getId();
                this.workerId = sheet.getWorkerId();
                this.clientIds = sheet.getClientIds();
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

        public List<Long> getClientIds()
        {
            return clientIds;
        }

        public void setClientIds(List<Long> clientIds)
        {
            this.clientIds = clientIds;
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
