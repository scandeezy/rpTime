package com.roosterpark.rptime.model;

import com.roosterpark.rptime.BasicRptimeUnitTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: John
 * Date: 10/21/13
 * Time: 1:33 PM
 */
public class WorkerTest extends BasicRptimeUnitTest {

    private static final String FIRST_NAME = "Ima";
    private static final String LAST_NAME = "Worker";
    private static final String EMAIL = "foo@bar.com";

    private Worker worker;

    @Before
    public void setup() {
        worker = new Worker();
    }

    @Test
    public void idTest() {
        worker.setId(ID);
        assertEquals("ID doesn't match!", ID, worker.getId());
    }

    @Test
    public void emailTest() {
        worker.setEmail(EMAIL);
        assertEquals("Email doesn't match!", EMAIL, worker.getEmail());
    }

    @Test
    public void firstNameTest() {
        worker.setFirstName(FIRST_NAME);
        assertEquals("First names does not match!", FIRST_NAME, worker.getFirstName());
    }

    @Test
    public void lastNameTest() {
        worker.setLastName(LAST_NAME);
        assertEquals("Last name doesn't match!", LAST_NAME, worker.getLastName()) ;
    }

    @Test
    public void activeTest() {
        worker.setActive(Boolean.TRUE);
        assertTrue("Active not true!", worker.getActive());
    }

    @Test
    public void startTest() {
        worker.setStart(START_DATE);
        assertEquals("Start date doesn't match!", START_DATE, worker.getStart());
    }

    @Test
    public void toStringTest() {
        worker.setFirstName(FIRST_NAME);
        worker.setLastName(LAST_NAME);
        worker.setEmail(EMAIL);
        worker.setStart(START_DATE);
        String output = "Worker : {  First : " + FIRST_NAME + ",  Last : " + LAST_NAME + ",  Email : " +
                        EMAIL + ",  Start : " + START_DATE + ",}";
        assertEquals("toString() doesn't match!", output, worker.toString());
    }

}
