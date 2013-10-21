package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roosterpark.rptime.model.Contract;
import com.roosterpark.rptime.model.TimeSheet;

public class TimeSheetService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	private ContractService contractService;
	
	public List<TimeSheet> getClientWeekPage(String client, Integer week, int count, int offset)
	{
		return ofy().load().type(TimeSheet.class).filter(TimeSheet.CLIENT_KEY, client).filter(TimeSheet.WEEK_KEY, week).limit(count).offset(offset).list();
	}
	
	public List<TimeSheet> getWorkerWeekPage(String worker, Integer week, int count, int offset)
	{
		return ofy().load().type(TimeSheet.class).filter(TimeSheet.WORKER_KEY, worker).filter(TimeSheet.WEEK_KEY, week).limit(count).offset(offset).list();
	}
	
	public List<TimeSheet> getPage(int count, int offset) {
		return ofy().load().type(TimeSheet.class).limit(count).offset(offset).list();
	}
	
	public void set(TimeSheet item) {
		ofy().save().entity(item).now();
	}

	public List<TimeSheet> getRecentForWorker(Long worker, Date date, int limit) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		Integer week = cal.get(Calendar.WEEK_OF_YEAR);
		LOGGER.debug("querying Sheets for worker={},date={},week", worker, date, week);
		List<TimeSheet> result = ofy()//
				.load()//
				.type(TimeSheet.class)//
				.filter(TimeSheet.WORKER_KEY, worker)//
				.filter(TimeSheet.WEEK_KEY + " <=", week)//
				.limit(limit)//
				// .order("-date")//Descending date sort
				.list();
		if (CollectionUtils.isEmpty(result)) {
			LOGGER.debug("creating new Sheet for worker={},date={},week={}", worker, date,week);
			List<Contract> contracts = contractService.getContractsForWorker(worker);
			result = new LinkedList<TimeSheet>();
			for(Contract contract : contracts)
			{
				TimeSheet s = new TimeSheet(contract.getWorker(), contract.getClient(), week, contract.getStartDayOfWeek());
				set(s); // persist;
				LOGGER.debug("adding sheet to list: sheet={}", s);
				result.add(s);
			}
		}
		LOGGER.debug("returning List<Sheet>={}", result);
		return result;
	}

}
