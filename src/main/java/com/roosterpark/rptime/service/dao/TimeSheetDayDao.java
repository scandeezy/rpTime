package com.roosterpark.rptime.service.dao;

import com.googlecode.objectify.Key;
import static com.googlecode.objectify.ObjectifyService.ofy;

import com.roosterpark.rptime.model.TimeSheetDay;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Named;
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
    
    public void delete(Long id) {
        TimeSheetDay day = getEntry(id);
        ofy().delete().entity(day);
    }
    
    public void delete(List<Long> ids) {
        List<TimeSheetDay> days = getEntries(ids);
        ofy().delete().entities(days);
    }
    
    public TimeSheetDay getEntry(Long id) {
        return ofy().load().type(TimeSheetDay.class).id(id).now();
    }
    
    public List<TimeSheetDay> getEntries(List<Long> ids) {
        LOGGER.debug("Ids being retreived {}", ids);
        return new LinkedList<TimeSheetDay>(ofy().load().type(TimeSheetDay.class).ids(ids).values());
    }
    
    public TimeSheetDay set(TimeSheetDay entry) {
        LOGGER.debug("Saving entry {}", entry);
        Key<TimeSheetDay> entryKey = ofy().save().entity(entry).now();
        LOGGER.debug("Got key {}", entryKey);
        entry = ofy().load().key(entryKey).now();

        LOGGER.debug("Fetched back entry {}", entry);
        return entry;
    }

    public List<TimeSheetDay> set(List<TimeSheetDay> entries) {
        List<TimeSheetDay> outbound = new LinkedList<>();

        for(TimeSheetDay entry : entries) {
            outbound.add(set(entry));
        }

        return outbound;
    }
}
