package com.roosterpark.rptime.service;

import com.googlecode.objectify.Key;
import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityNotFoundException;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roosterpark.rptime.model.Contract;
import com.roosterpark.rptime.model.TimeCardLogEntry;
import com.roosterpark.rptime.model.TimeSheet;
import com.roosterpark.rptime.model.TimeSheetView;

@Named
public class TimeSheetService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	ContractService contractService;

	public List<TimeSheetView> createForWorkerDate(Long workerId, LocalDate date) {
		List<Contract> contracts = contractService.getActiveContractsForWorker(workerId, date);
		if (!contracts.isEmpty()) {
                        List<TimeSheetView> views = new LinkedList<>();
                        TimeSheetView view = new TimeSheetView();
			List<TimeSheet> sheets = new LinkedList<>();

			for (Contract contract : contracts) {
                            views.add(createForWorkerDateContract(workerId, date, contract));
			}

			return views;
		}

		throw new EntityNotFoundException("No active Contracts found for Worker id='" + workerId + "' and date '" + date
				+ "'.  Solution: create Contract on the /contracts page for this Worker with beginDate < " + date + "< endDate.");

	}
        
        public TimeSheetView createForWorkerDateContract(Long workerId, LocalDate date, Contract contract) {
                // TODO verify this sheet doesn't already exist.
                LOGGER.debug("created new TimeSheet for worker={}, date={}, contract={}", workerId, date, contract);
                
                List<Long> logIds = new LinkedList<>();
                List<TimeCardLogEntry> entries = new LinkedList<>();
                for(int i = 0; i < 7; i++) {
                    TimeCardLogEntry entry = new TimeCardLogEntry(workerId, contract.getClient(), date.plusDays(i));
                    // Retain the ID
                    entry = set(entry);
                    entries.add(entry);
                    logIds.add(entry.getId());
                }
                Long clientId = contract.getClient();
                LocalDate contractDate = adjustDate(date, contract.getStartDayOfWeek());
                TimeSheet result = new TimeSheet(workerId, clientId, contractDate, logIds);

                result = set(result);
                
                TimeSheetView view = new TimeSheetView(result,entries);
                
                return view;
        }
        
        public List<TimeSheetView> getSheetViewsForWorkerPage(Long workerId, Integer count, Integer offset) {
                List<TimeSheet> sheets = ofy().load().type(TimeSheet.class)
                                                .filter("workerId", workerId)
                                                .order(TimeSheet.START_DATE_KEY)
                                                .limit(count)
                                                .offset(offset)
                                                .list();
                List<TimeSheetView> views = new LinkedList<>();
                for(TimeSheet sheet : sheets) {
                        views.add(convert(sheet));
                }

                return views;
        }
        
        private TimeSheetView convert(TimeSheet sheet) {
                List<TimeCardLogEntry> entries = getEntries(sheet.getTimeCardIds());
                // TODO assert the order is consistent
                return new TimeSheetView(sheet, entries);
        }
        
        public List<TimeCardLogEntry> getEntries(List<Long> ids) {
            return new LinkedList<TimeCardLogEntry>(ofy().load().type(TimeCardLogEntry.class).ids(ids).values());
        }
        
        private LocalDate adjustDate(LocalDate date, Integer dayOfWeek) {
            int currentDayOfWeek = date.dayOfWeek().get();
            LOGGER.debug("Current day of week {} and needed day of week {}", currentDayOfWeek, dayOfWeek);
            
            return date.plusDays(dayOfWeek - currentDayOfWeek);
        }

	public List<TimeSheet> getAllForWorker(Long workerId) {
		LOGGER.warn("Getting TimeSheets for workerId={}", workerId);
		return ofy().load().type(TimeSheet.class).filter("workerId", workerId).list();
	}

	public List<TimeSheetView> getAll() {
		LOGGER.warn("Getting TimeSheets for admin");
		List<TimeSheet> sheets = ofy().load().type(TimeSheet.class).list();
                List<TimeSheetView> views = new LinkedList<>();
                for(TimeSheet sheet : sheets) {
                    views.add(convert(sheet));
                }
                
                return views;
	}

	public TimeSheetView getById(Long id) {
                TimeSheet sheet = getSheetById(id);
                
                return convert(sheet);
	}
        
        private TimeSheet getSheetById(Long id) {
                TimeSheet sheet = ofy().load().type(TimeSheet.class).id(id).now();
                
                return sheet;
        }

	public List<TimeSheet> getClientWeekPage(String client, Integer week, int count, int offset) {
		return ofy().load().type(TimeSheet.class).filter(TimeSheet.CLIENT_KEY, client).filter(TimeSheet.WEEK_KEY, week).limit(count)
				.offset(offset).list();
	}

	public List<TimeSheet> getWorkerWeekPage(String worker, Integer week, int count, int offset) {
		return ofy().load().type(TimeSheet.class).filter(TimeSheet.WORKER_KEY, worker).filter(TimeSheet.WEEK_KEY, week).limit(count)
				.offset(offset).list();
	}

	public List<TimeSheet> getPage(int count, int offset) {
		return ofy().load().type(TimeSheet.class).limit(count).offset(offset).list();
	}

        public TimeSheetView set(TimeSheetView view) {
                LOGGER.debug("Saving timesheet {}", view);
                List<TimeCardLogEntry> entries = view.getTimeCards();
                entries = set(entries);
                view.setTimeCards(entries);
                TimeSheet sheet = new TimeSheet(view);
                sheet = set(sheet);
                
                return new TimeSheetView(sheet, entries);
        }
        
	private TimeSheet set(TimeSheet item) {
		LOGGER.debug("Saving timesheet {}", item);
		item.setUpdateTimestamp(new LocalDateTime());
		Key<TimeSheet> sheetKey = ofy().save().entity(item).now();
                LOGGER.debug("Got key {}", sheetKey);
                item = ofy().load().key(sheetKey).now();
                LOGGER.debug("Fetched back sheet {}", item);
                return item;
	}
        
        private TimeCardLogEntry set(TimeCardLogEntry entry) {
            LOGGER.debug("Saving entry {}", entry);
            Key<TimeCardLogEntry> entryKey = ofy().save().entity(entry).now();
            LOGGER.debug("Got key {}", entryKey);
            entry = ofy().load().key(entryKey).now();
            
            LOGGER.debug("Fetched back entry {}", entry);
            return entry;
        }
        
        private List<TimeCardLogEntry> set(List<TimeCardLogEntry> entries) {
            List<TimeCardLogEntry> outbound = new LinkedList<>();
            
            for(TimeCardLogEntry entry : entries) {
                outbound.add(set(entry));
            }
            
            return outbound;
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

	public void delete(Long id) {
		TimeSheet c = getSheetById(id);
                List<TimeCardLogEntry> entries = getEntries(c.getTimeCardIds());
                ofy().delete().entities(entries).now();
		ofy().delete().entity(c).now();
	}

}
