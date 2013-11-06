package com.roosterpark.rptime.service;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityNotFoundException;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roosterpark.rptime.model.Contract;
import com.roosterpark.rptime.model.TimeCardLogEntry;
import com.roosterpark.rptime.model.TimeSheet;
import com.roosterpark.rptime.model.TimeSheetDay;
import com.roosterpark.rptime.model.TimeSheetView;
import com.roosterpark.rptime.service.dao.TimeSheetDao;
import com.roosterpark.rptime.service.dao.TimeSheetDayDao;
import org.joda.time.DateTimeConstants;

@Named
public class TimeSheetService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	ContractService contractService;

	@Inject
	TimeSheetDao timeSheetDao;

	@Inject
	TimeSheetDayDao timeSheetDayDao;

	public TimeSheetView createForWorkerDate(Long workerId, LocalDate date) {
		List<Contract> contracts = contractService.getActiveContractsForWorker(workerId, date);
		if (!contracts.isEmpty()) {
			List<TimeSheet> sheets = new LinkedList<>();

                        boolean lunch = false;
                        Long defaultClientId = 0L;
			for (Contract contract : contracts) {
                                if(defaultClientId == 0L)
                                        defaultClientId = contract.getClient();
                                if(contract.getLunchRequired())
                                {
                                        lunch = true;
                                        break;
                                }
			}
                        TimeSheetView view = createForWorkerDateContract(workerId, date, defaultClientId, lunch);

			return view;
		}

		throw new EntityNotFoundException("No active Contracts found for Worker id='" + workerId + "' and date '" + date
				+ "'.  Solution: create Contract on the /contracts page for this Worker with beginDate < " + date + "< endDate.");

	}
        
        public TimeSheetView createForWorkerDateContract(Long workerId, LocalDate date, Long defaultClientId, boolean lunchRequired) {
                // TODO verify this sheet doesn't already exist.
                LOGGER.debug("created new TimeSheet for worker={}, date={}, lunchRequired={}", workerId, date, lunchRequired);
                
                List<Long> logIds = new LinkedList<>();
                List<TimeSheetDay> entries = new LinkedList<>();
                for(int i = 0; i < 7; i++) {
                        TimeSheetDay day = new TimeSheetDay();
                        if(!lunchRequired) {
                            TimeCardLogEntry entry = new TimeCardLogEntry(workerId, defaultClientId, date.plusDays(i));
                            day.addEntry(entry);
                        } else {
                            TimeCardLogEntry entry1 = new TimeCardLogEntry(workerId, defaultClientId, date.plusDays(i), 8, 12);
                            TimeCardLogEntry entry2 = new TimeCardLogEntry(workerId, defaultClientId, date.plusDays(i), 13, 17);
                            day.addEntry(entry1);
                            day.addEntry(entry2);
                        }
                        day = timeSheetDayDao.set(day);
                        entries.add(day);
                        logIds.add(day.getId());
                }
                Long clientId = defaultClientId;
                // Hardcoded because of our current data layer.
                LocalDate contractDate = adjustDate(date, DateTimeConstants.SUNDAY);
                TimeSheet result = new TimeSheet(workerId, clientId, contractDate, logIds);

                result = timeSheetDao.set(result);
                
                TimeSheetView view = new TimeSheetView(result,entries);
                
                return view;
        }
        
        public List<TimeSheetView> getSheetViewsForWorkerPage(Long workerId, Integer count, Integer offset) {
                List<TimeSheet> sheets = timeSheetDao.getSheetViewsForWorkerPage(workerId, count, offset);
                List<TimeSheetView> views = new LinkedList<>();
                for(TimeSheet sheet : sheets) {
                        views.add(convert(sheet));
                }

                return views;
        }
        
        private TimeSheetView convert(TimeSheet sheet) {
                List<TimeSheetDay> entries = timeSheetDayDao.getEntries(sheet.getTimeCardIds());
                // TODO assert the order is consistent
                return new TimeSheetView(sheet, entries);
        }
        
        private LocalDate adjustDate(LocalDate date, Integer dayOfWeek) {
                int currentDayOfWeek = date.dayOfWeek().get();
                LOGGER.debug("Current day of week {} and needed day of week {}", currentDayOfWeek, dayOfWeek);

                return date.plusDays(dayOfWeek - currentDayOfWeek);
        }

	public List<TimeSheetView> getAll() {
		LOGGER.warn("Getting TimeSheets for admin");
		List<TimeSheet> sheets = timeSheetDao.getAll();
		LOGGER.debug("Found these sheets for all {}", sheets);
		List<TimeSheetView> views = new LinkedList<>();
		for (TimeSheet sheet : sheets) {
			views.add(convert(sheet));
		}

		return views;
	}

	public TimeSheetView getById(Long id) {
		TimeSheet sheet = timeSheetDao.getById(id);

		return convert(sheet);
	}
        
        public TimeSheetView set(TimeSheetView view) {
                LOGGER.debug("Saving timesheet {}", view);
                List<TimeSheetDay> entries = view.getDays();
                entries = timeSheetDayDao.set(entries);
                view.setDays(entries);
                TimeSheet sheet = new TimeSheet(view);
                sheet = timeSheetDao.set(sheet);
                
                return new TimeSheetView(sheet, entries);
        }

	public void delete(Long id) {
		TimeSheet c = timeSheetDao.getById(id);
		List<TimeSheetDay> entries = timeSheetDayDao.getEntries(c.getTimeCardIds());

		timeSheetDayDao.delete(c.getTimeCardIds());
		timeSheetDao.delete(c.getId());
	}

}
