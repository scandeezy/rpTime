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
    private static final String HEADER = "Some Header";
    private static final String PHONE = "(206) 867-5309";
    private static final Integer START_DAY_OF_WEEK = 1;

    private Client client;

    @Before
    public void setup() {
        client = new Client();
    }

    @Test
    public void constructorTest() {
        client = new Client(NAME, HEADER, PHONE, START_DAY_OF_WEEK);
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
    public void headerTest() {
        client.setHeader(HEADER);
        assertEquals("Header doesn't match!", HEADER, client.getHeader());
    }

    @Test
    public void phoneTest() {
        client.setPhone(PHONE);
        assertEquals("Jenny changed her number!", PHONE, client.getPhone());
    }

    @Test
    public void startDayOfWeekTest() {
        client.setStartDayOfWeek(START_DAY_OF_WEEK);
        assertEquals("Start day of week doesn't match!", START_DAY_OF_WEEK, client.getStartDayOfWeek());
    }

    @Test
    public void toStringTest() {
        client.setName(NAME);
        client.setHeader(HEADER);
        client.setPhone(PHONE);
        client.setStartDayOfWeek(START_DAY_OF_WEEK);
        String output = "Company : {  name : " + NAME + ",  header : " + HEADER + ",  phone : " + PHONE +
                        ",  startDayOfWeek : " + START_DAY_OF_WEEK + ",}";
        assertEquals("toString() doesn't match!", output, client.toString());
    }

}
