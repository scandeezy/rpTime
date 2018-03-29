package com.roosterpark.rptime.model.report;

import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;

import com.roosterpark.rptime.model.Worker;

public class TimeSheetsPerWorkerByMonthForClientReport extends Report {

	private Map<Long, Map<LocalDate, Double>> reportMap;
	private Map<Long, Object> timeSheetMap;
	private Map<Long, Map<LocalDate, Long>> workerToDateToTimeSheetMap;
	private List<Worker> workerList;

	public TimeSheetsPerWorkerByMonthForClientReport(final LocalDate reportDate) {
		super(reportDate);
	}

	public Map<Long, Map<LocalDate, Double>> getReportMap() {
		return reportMap;
	}

	public void setReportMap(Map<Long, Map<LocalDate, Double>> reportMap) {
		this.reportMap = reportMap;
	}

	public Map<Long, Object> getTimeSheetMap() {
		return timeSheetMap;
	}

	public void setTimeSheetMap(Map<Long, Object> timeSheetMap) {
		this.timeSheetMap = timeSheetMap;
	}

	public Map<Long, Map<LocalDate, Long>> getWorkerToDateToTimeSheetMap() {
		return workerToDateToTimeSheetMap;
	}

	public void setWorkerToDateToTimeSheetMap(Map<Long, Map<LocalDate, Long>> workerToDateToTimeSheetMap) {
		this.workerToDateToTimeSheetMap = workerToDateToTimeSheetMap;
	}

	public List<Worker> getWorkerList() {
		return workerList;
	}

	public void setWorkerList(List<Worker> workers) {
		this.workerList = workers;
	}

}
