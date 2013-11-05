/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roosterpark.rptime.service.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.Key;
import com.roosterpark.rptime.model.TimeSheet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.inject.Named;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author scandeezy
 */
@Named
public class TimeSheetDao
{
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    public void delete(Long id) {
        TimeSheet sheet = getById(id);
        
        ofy().delete().entity(sheet);
    }
    
    @Deprecated
    public List<TimeSheet> getAll() {
        return ofy().load().type(TimeSheet.class).list();
    }
    
    public TimeSheet getById(Long id) {
        TimeSheet sheet = ofy().load().type(TimeSheet.class).id(id).now();

        return sheet;
    }

    public List<TimeSheet> getRecentForWorker(Long workerId, Date date, int limit) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        Integer week = cal.get(Calendar.WEEK_OF_YEAR);
        LOGGER.debug("querying Sheets for worker={},date={},week={}", workerId, date, week);
        List<TimeSheet> result = ofy()//
                        .load()//
                        .type(TimeSheet.class)//
                        .filter(TimeSheet.WORKER_KEY, workerId)//
                        .filter(TimeSheet.WEEK_KEY + " <=", week)//
                        .limit(limit)//
                        // .order("-date")//Descending date sort
                        .list();
        LOGGER.debug("returning List<Sheet>={}", result);
        return result;
    }

    public List<TimeSheet> getSheetViewsForWorkerPage(Long workerId, Integer count, Integer offset) {
        List<TimeSheet> sheets = ofy().load().type(TimeSheet.class)
                    .filter("workerId", workerId)
                    .order(TimeSheet.START_DATE_KEY)
                    .limit(count)
                    .offset(offset)
                    .list();
        return sheets;
    }
    
    public TimeSheet set(TimeSheet item) {
        LOGGER.debug("Saving timesheet {}", item);
        item.setUpdateTimestamp(new LocalDateTime());
        Key<TimeSheet> sheetKey = ofy().save().entity(item).now();
        LOGGER.debug("Got key {}", sheetKey);
        item = ofy().load().key(sheetKey).now();
        LOGGER.debug("Fetched back sheet {}", item);
        return item;
    }
}
