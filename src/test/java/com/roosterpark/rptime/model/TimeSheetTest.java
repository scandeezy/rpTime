package com.roosterpark.rptime.model;

import com.roosterpark.rptime.BasicRptimeUnitTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: John
 * Date: 10/21/13
 * Time: 11:11 AM
 */
public class TimeSheetTest extends BasicRptimeUnitTest {

    private static final Long ID = 1L;
    private static final Long WORKER_ID = 123L;
    private static final Integer WEEK = 1;

    private TimeSheet sheet;

    @Before
    public void setup() {
        sheet = new TimeSheet();
    }

    @Test
    public void idTest() {
        sheet.setId(ID);
        assertEquals("ID doesn't match!", ID, sheet.getId());
    }

    @Test
    public void userIdTest() {
        sheet.setWorkerId(WORKER_ID);
        assertEquals("User ID doesn't match!", WORKER_ID, sheet.getWorkerId());
    }

    @Test
    public void weekTest() {
        sheet.setWeek(WEEK);
        assertEquals("Week doesn't match!", WEEK, sheet.getWeek());
    }

}
