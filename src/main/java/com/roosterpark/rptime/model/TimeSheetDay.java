/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roosterpark.rptime.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author scandeezy
 */
@Entity
public class TimeSheetDay
{
    @Id
    private Long id;
    private List<TimeCardLogEntry> entries;
    
    public TimeSheetDay() {
        entries = new LinkedList<>();
    }

    public List<TimeCardLogEntry> getEntries()
    {
        return entries;
    }
    
    public void addEntry(TimeCardLogEntry entry) {
        this.entries.add(entry);
        Collections.sort(entries);
    }

    public void setEntries(List<TimeCardLogEntry> entries)
    {
        this.entries = entries;
    }
}
