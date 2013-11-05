package com.roosterpark.rptime.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import java.util.LinkedList;
import java.util.List;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 *
 * @author scandeezy
 */
@Entity
public class TimeCardLogEntry
{
    @Id
    private Long id;
    @Index
    private Long workerId;
    @Index
    private Long clientId;
    @Index
    private LocalDate date;
    private List<LocalTime> startTimes;
    private List<LocalTime> endTimes;

    public TimeCardLogEntry() {
        this.startTimes = new LinkedList<LocalTime>(){{add(new LocalTime(9,0));}};
        this.endTimes = new LinkedList<LocalTime>(){{add(new LocalTime(17,0));}};
    }
    
    public TimeCardLogEntry(Long workerId, Long clientId, LocalDate date) {
        this();
        this.workerId = workerId;
        this.clientId = clientId;
        this.date = date;
    }
    
    // Assumes lunch taken at noon for lunchLength minutes
    public TimeCardLogEntry(Long workerId, Long clientId, LocalDate date, Integer lunchLength) {
        this(workerId, clientId, date);
        LocalTime noon = new LocalTime(12,0);
        this.startTimes.add(noon.plusMinutes(lunchLength));
        this.endTimes.add(0, noon);
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

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public List<LocalTime> getStartTimes()
    {
        return startTimes;
    }

    public void setStartTimes(List<LocalTime> startTimes)
    {
        this.startTimes = startTimes;
    }

    public List<LocalTime> getEndTimes()
    {
        return endTimes;
    }

    public void setEndTimes(List<LocalTime> endTimes)
    {
        this.endTimes = endTimes;
    }
}
