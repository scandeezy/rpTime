package com.roosterpark.rptime.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import com.roosterpark.rptime.BasicRptimeUnitTest;

/**
 * User: John Date: 10/21/13 Time: 11:11 AM
 */
public class TimeSheetTest extends BasicRptimeUnitTest {

	private static final Long WORKER_ID = 123L;
	private static final Long CLIENT_ID_1 = 234L;
	private static final Long CLIENT_ID_2 = 567L;
        private static final SortedSet<Long> CLIENT_IDS = new TreeSet<Long>()
                {{
                        add(CLIENT_ID_1);
                        add(CLIENT_ID_2);
                }};
	private static final Integer WEEK = 1;
	private static final LocalDate START_DATE = new LocalDate();
	private static final Double[] HOURS = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

	private TimeSheet sheet;

	@Before
	public void setup() {
		sheet = new TimeSheet(WORKER_ID, CLIENT_IDS, START_DATE);
	}

	@Test
	public void constructorTest() {
		sheet = new TimeSheet(WORKER_ID, CLIENT_IDS, START_DATE);
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
		sheet.setClientIds(CLIENT_IDS);
		assertEquals("Client ID doesn't match!", CLIENT_IDS, sheet.getClientIds());
	}

	@Test
	public void startDayTest() {
		sheet.setStartDate(START_DATE);
		assertEquals("Start day doesn't match!", START_DATE, sheet.getStartDate());
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
