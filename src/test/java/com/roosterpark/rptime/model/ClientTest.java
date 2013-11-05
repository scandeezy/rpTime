package com.roosterpark.rptime.model;

import com.roosterpark.rptime.BasicRptimeUnitTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: John
 * Date: 10/21/13
 * Time: 2:46 PM
 */
public class ClientTest extends BasicRptimeUnitTest {

    private static final String NAME = "Some Client";
    private static final Boolean LUNCH = true;
    private static final Integer START_DAY_OF_WEEK = 1;

    private Client client;

    @Before
    public void setup() {
        client = new Client();
    }

    @Test
    public void constructorTest() {
        client = new Client(NAME, LUNCH, START_DAY_OF_WEEK);
        assertNotNull("Object null!", client);
    }

    @Test
    public void idTest() {
        client.setId(ID);
        assertEquals("ID doesn't match!", ID, client.getId());
    }

    @Test
    public void nameTest() {
        client.setName(NAME);
        assertEquals("Name doesn't match!", NAME, client.getName());
    }

    @Test
    public void lunchTest() {
        client.setLunchRequired(LUNCH);
        assertEquals("Header doesn't match!", LUNCH, client.getLunchRequired());
    }

    @Test
    public void startDayOfWeekTest() {
        client.setStartDayOfWeek(START_DAY_OF_WEEK);
        assertEquals("Start day of week doesn't match!", START_DAY_OF_WEEK, client.getStartDayOfWeek());
    }

//    @Test
//    public void toStringTest() {
//        client.setName(NAME);
//        client.setLunchRequired(LUNCH);
//        client.setStartDayOfWeek(START_DAY_OF_WEEK);
//        String output = "Company : {  name : " + NAME + ",  lunchRequired : " + LUNCH + 
//                        ",  startDayOfWeek : " + START_DAY_OF_WEEK + ",}";
//        assertEquals("toString() doesn't match!", output, client.toString());
//    }

}
