package com.roosterpark.rptime.model.report;

import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;

import com.roosterpark.rptime.model.Worker;

public class TotalHoursPerWorkerPerMonthReport extends Report {

	private List<Worker> workerList;
	private Map<Long, Double> workerIdToHoursMap;

	public TotalHoursPerWorkerPerMonthReport(LocalDate reportDate) {
		super(reportDate);
	}

	public List<Worker> getWorkerList() {
		return workerList;
	}

	public void setWorkerList(List<Worker> workerList) {
		this.workerList = workerList;
	}

	public Map<Long, Double> getWorkerIdToHoursMap() {
		return workerIdToHoursMap;
	}

	public void setWorkerIdToHoursMap(Map<Long, Double> workerIdToHoursMap) {
		this.workerIdToHoursMap = workerIdToHoursMap;
	}

}
