package com.roosterpark.rptime.model;

import com.googlecode.objectify.annotation.Embed;
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
@Embed
public class TimeCardLogEntry implements Comparable
{
    @Id
    private Long id;
    @Index
    private Long workerId;
    @Index
    private Long clientId;
    @Index
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    
    public TimeCardLogEntry() {}
    
    public TimeCardLogEntry(Long workerId, Long clientId, LocalDate date) {
        this.workerId = workerId;
        this.clientId = clientId;
        this.date = date;
        this.startTime = new LocalTime(9,0);
        this.endTime = new LocalTime(17,0);
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

    public LocalTime getStartTime()
    {
        return startTime;
    }

    public void setStartTime(LocalTime startTime)
    {
        this.startTime = startTime;
    }

    public LocalTime getEndTime()
    {
        return endTime;
    }

    public void setEndTime(LocalTime endTime)
    {
        this.endTime = endTime;
    }

    // Compared strictly based on date and start times.
    @Override
    public int compareTo(Object o)
    {
        if(o instanceof TimeCardLogEntry)
        {
            TimeCardLogEntry other = (TimeCardLogEntry)o;
            
            if(this.getDate().compareTo(other.getDate()) != 0)
                return this.getDate().compareTo(other.getDate());
            
            return this.getStartTime().compareTo(other.getStartTime());
        }
        
        return -1;
    }
}
