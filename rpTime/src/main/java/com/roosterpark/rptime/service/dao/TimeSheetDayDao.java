package com.roosterpark.rptime.service.dao;

import com.googlecode.objectify.Key;
import static com.googlecode.objectify.ObjectifyService.ofy;
import com.roosterpark.rptime.model.TimeCardLogEntry;

import com.roosterpark.rptime.model.TimeSheetDay;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.inject.Named;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author scandeezy
 */
@Named
public class TimeSheetDayDao
{
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    private Random random;
    
    public TimeSheetDayDao() {
        random = new Random();
    }
    
    public void delete(Long id) {
        List<TimeCardLogEntry> entries = getEntriesForId(id);
        ofy().delete().entities(entries);
    }
    
    public void delete(List<Long> ids) {
        List<TimeCardLogEntry> days = getEntriesForIds(ids);
        ofy().delete().entities(days);
    }
    
    public TimeSheetDay getEntry(Long id) {
        List<TimeCardLogEntry> entries = getEntriesForId(id);
        TimeSheetDay day = new TimeSheetDay(entries);
        return day;
    }
    
    private List<TimeCardLogEntry> getEntriesForId(Long id) {
        List<TimeCardLogEntry> entries = ofy().load().type(TimeCardLogEntry.class).filter(TimeCardLogEntry.CARD_ID_KEY, id).list();
        
        return entries;
    }
    
    private List<TimeCardLogEntry> getEntriesForIds(List<Long> ids) {
        
        List<TimeCardLogEntry> entries = new LinkedList<TimeCardLogEntry>();
        for(Long id : ids) {
            entries.addAll(getEntriesForId(id));
        }
        
        return entries;
    }
    
    public List<TimeSheetDay> getEntries(List<Long> ids) {
        LOGGER.debug("Ids being retreived {}", ids);
        List<TimeSheetDay> days = new LinkedList<>();
        for(Long id : ids) {
            days.add(getEntry(id));
        }
        return days;
    }
    
    public TimeSheetDay set(TimeSheetDay entry) {
        LOGGER.debug("Saving entry {}", entry);
        List<TimeCardLogEntry> logs = entry.getEntries();
        Long id = entry.getId();
        if(id == null)
        {
            id = generateId();
        }
        
        for(TimeCardLogEntry log : logs) {
            log.setCardId(id);
            log.correctHours();
        }
        
        Map<Key<TimeCardLogEntry>, TimeCardLogEntry> retEntries =ofy().save().entities(logs).now();
        entry.setEntries(new LinkedList<>(retEntries.values()));
        entry.setId(logs.get(0).getCardId());

        return entry;
    }
    
    private Long generateId() {
        while(true) {
            Long id = random.nextLong();
            LOGGER.debug("Checking id {}", id);
            List<TimeCardLogEntry> entries = getEntriesForId(id);
            if(entries.isEmpty())
                return id;
        }
    }

    public List<TimeSheetDay> set(List<TimeSheetDay> entries) {
        List<TimeSheetDay> outbound = new LinkedList<>();

        for(TimeSheetDay entry : entries) {
            outbound.add(set(entry));
        }

        return outbound;
    }
    
    public Map<Long, List<TimeCardLogEntry>> getForClientOverRange(Long clientId, LocalDate start, LocalDate end) {
        Map<Long, List<TimeCardLogEntry>> map = new LinkedHashMap<>();
        
        List<TimeCardLogEntry> entries = ofy().load()
                .type(TimeCardLogEntry.class)
                .filter(TimeCardLogEntry.CLIENT_ID_KEY, clientId)
                .order(TimeCardLogEntry.DATE_KEY)
                .list();
        
        for(TimeCardLogEntry entry : entries) {
            if(!map.containsKey(entry.getWorkerId()))
                map.put(entry.getWorkerId(), new LinkedList<TimeCardLogEntry>());
            map.get(entry.getWorkerId()).add(entry);
        }
        
        return map;
    } 
}
