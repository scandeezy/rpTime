package com.roosterpark.rptime.model.report;

import com.roosterpark.rptime.model.TimeCardLogEntry;
import com.roosterpark.rptime.model.TimeSheetView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.LocalDate;

public class HoursForClientInRange
{
    private Long clientId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Map<Long, List<TimeCardLogEntry>> workerToTimeMap;
    private Map<Long, Double> workerToTotalMap;
    
    public HoursForClientInRange() {
        workerToTimeMap = new HashMap<>();
        workerToTotalMap = new HashMap<>();
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }

    public LocalDate getEndDate()
    {
        return endDate;
    }

    public void setEndDate(LocalDate endDate)
    {
        this.endDate = endDate;
    }

    public Map<Long, List<TimeCardLogEntry>> getWorkerToTimeMap()
    {
        return workerToTimeMap;
    }

    public void setWorkerToTimeMap(Map<Long, List<TimeCardLogEntry>> workerToTimeMap)
    {
        this.workerToTimeMap = workerToTimeMap;
    }

    public Map<Long, Double> getWorkerToTotalMap()
    {
        return workerToTotalMap;
    }

    public void setWorkerToTotalMap(Map<Long, Double> workerToTotalMap)
    {
        this.workerToTotalMap = workerToTotalMap;
    }
}
