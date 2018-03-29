package com.roosterpark.rptime.model.report;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class Report {

	private static final String DATE_TIME_FORMAT_YEAR_MONTH = "MMMM YYYY";

	private LocalDate nextMonthReportDate;
	private LocalDate previousMonthReportDate;
	private LocalDate reportDate;
	private LocalDateTime updateDate;

	@SuppressWarnings("unused")
	private Report() {
	}

	public Report(final LocalDate reportDate) {
		this.reportDate = reportDate;
		if (reportDate != null) {
			this.nextMonthReportDate = reportDate.plusMonths(1);
			this.previousMonthReportDate = reportDate.minusMonths(1);
		}
	}

	public LocalDate getNextMonthReportDate() {
		return nextMonthReportDate;
	}

	public void setNextMonthReportDate(LocalDate nextMonthReportDate) {
		this.nextMonthReportDate = nextMonthReportDate;
	}

	public LocalDate getPreviousMonthReportDate() {
		return previousMonthReportDate;
	}

	public void setPreviousMonthReportDate(LocalDate previousMonthReportDate) {
		this.previousMonthReportDate = previousMonthReportDate;
	}

	public String getReportDate() {
		return reportDate.toString(DATE_TIME_FORMAT_YEAR_MONTH);
	}

	public void setReportDate(LocalDate date) {
		this.reportDate = date;
	}

	public LocalDateTime getUpdateDate() {
		if (updateDate != null) {
			return updateDate;
		} else {
			return new LocalDateTime();
		}
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

}
