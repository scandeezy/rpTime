package com.roosterpark.rptime.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: John
 * Date: 10/21/13
 * Time: 11:11 AM
 */
public class SheetTest {

    private static final Long ID = 1L;
    private static final String USER_ID = "Ima User";
    private static final Integer WEEK = 1;

    private Sheet sheet;

    @Before
    public void setup() {
        sheet = new Sheet();
    }

    @Test
    public void idTest() {
        sheet.setId(ID);
        assertEquals("ID doesn't match!", ID, sheet.getId());
    }

    @Test
    public void userIdTest() {
        sheet.setUserId(USER_ID);
        assertEquals("User ID doesn't match!", USER_ID, sheet.getUserId());
    }

    @Test
    public void weekTest() {
        sheet.setWeek(WEEK);
        assertEquals("Week doesn't match!", WEEK, sheet.getWeek());
    }

}
