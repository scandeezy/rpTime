package com.roosterpark.rptime.model;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import com.roosterpark.rptime.BasicRptimeUnitTest;

/**
 * User: John Date: 10/21/13 Time: 3:35 PM
 */
public class ContractTest extends BasicRptimeUnitTest {

	private static final Long WORKER = 1L;
	private static final Long CLIENT = 2L;
	private static final LocalDate END_DATE = new LocalDate(START_DATE.plusDays(5));

	private Contract contract;

	@Before
	public void setup() {
		contract = new Contract();
	}

	@Test
	public void idTest() {
		contract.setId(ID);
		assertEquals("ID doesn't match!", ID, contract.getId());
	}

	@Test
	public void workerTest() {
		contract.setWorker(WORKER);
		assertEquals("Worker doesn't match!", WORKER, contract.getWorker());
	}

	@Test
	public void clientTest() {
		contract.setClient(CLIENT);
		assertEquals("Client doesn't match!", CLIENT, contract.getClient());
	}

	@Test
	public void startTest() {
		contract.setStart(START_DATE);
		assertEquals("Start doesn't match!", START_DATE, contract.getStart());
	}

	@Test
	public void endTest() {
		contract.setEnd(END_DATE);
		assertEquals("End doesn't match!", END_DATE, contract.getEnd());
	}

	@Test
	public void toStringTest() {
		contract.setId(ID);
		contract.setWorker(WORKER);
		contract.setClient(CLIENT);
		contract.setStart(START_DATE);
		contract.setEnd(END_DATE);
		String output = "Contract [id=" + ID + ", worker=" + WORKER + ", client=" + CLIENT + ", start=" + START_DATE + ", end=" + END_DATE
				+ "]";
		assertEquals("toString() doesn't match!", output, contract.toString());
	}

}
