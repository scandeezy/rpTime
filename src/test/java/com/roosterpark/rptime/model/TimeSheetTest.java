package com.roosterpark.rptime.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import com.roosterpark.rptime.BasicRptimeUnitTest;

/**
 * User: John Date: 10/21/13 Time: 11:11 AM
 */
public class TimeSheetTest extends BasicRptimeUnitTest {

	private static final Long WORKER_ID = 123L;
	private static final Long CLIENT_ID = 234L;
	private static final Integer WEEK = 1;
	private static final LocalDate START_DATE = new LocalDate();
	private static final Double[] HOURS = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

	private TimeSheet sheet;

	@Before
	public void setup() {
		sheet = new TimeSheet(WORKER_ID);
	}

	@Test
	public void constructorTest() {
		sheet = new TimeSheet(WORKER_ID, CLIENT_ID, WEEK, START_DATE);
		assertNotNull("Object null!", sheet);
	}

	@Test
	public void idTest() {
		sheet.setId(ID);
		assertEquals("ID doesn't match!", ID, sheet.getId());
	}

	@Test
	public void workerIdTest() {
		sheet.setWorkerId(WORKER_ID);
		assertEquals("Worker ID doesn't match!", WORKER_ID, sheet.getWorkerId());
	}

	@Test
	public void weekTest() {
		sheet.setWeek(WEEK);
		assertEquals("Week doesn't match!", WEEK, sheet.getWeek());
	}

	@Test
	public void clientIdTest() {
		sheet.setClientId(CLIENT_ID);
		assertEquals("Client ID doesn't match!", CLIENT_ID, sheet.getClientId());
	}

	@Test
	public void startDayTest() {
		sheet.setStartDate(START_DATE);
		assertEquals("Start day doesn't match!", START_DATE, sheet.getStartDate());
	}

	@Test
	public void hoursTest() {
		sheet.setHours(HOURS);
		assertArrayEquals("Hours doesn't match!", HOURS, sheet.getHours());
	}

	@Test
	public void setHoursWithNullTest() {
		sheet.setHours(null);
		assertArrayEquals("Arrays don't match!", HOURS, sheet.getHours());
	}

	// @Test
	// public void toStringTest() {
	// sheet.setId(ID);
	// sheet.setWorkerId(WORKER_ID);
	// sheet.setClientId(CLIENT_ID);
	// sheet.setWeek(WEEK);
	// sheet.setStartDate(START_DATE);
	// sheet.setHours(HOURS);
	// String output = "TimeSheet [id=" + ID + ", workerId=" + WORKER_ID + ", clientId=" + CLIENT_ID + ", week=" + WEEK + ", startDay="
	// + START_DAY + ", hours=" + Arrays.toString(HOURS) + "]";
	// assertEquals("toString() doesn't match!", output, sheet.toString());
	// }

}
