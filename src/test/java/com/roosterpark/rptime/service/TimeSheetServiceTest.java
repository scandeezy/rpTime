/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roosterpark.rptime.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import com.roosterpark.rptime.BasicRptimeUnitTest;
import com.roosterpark.rptime.exceptions.ContractNotFoundException;
import com.roosterpark.rptime.model.Contract;
import com.roosterpark.rptime.model.TimeSheet;
import com.roosterpark.rptime.model.TimeSheetDay;
import com.roosterpark.rptime.model.TimeSheetView;
import com.roosterpark.rptime.service.dao.TimeSheetDao;
import com.roosterpark.rptime.service.dao.TimeSheetDayDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author scandeezy
 */
@RunWith(PowerMockRunner.class)
public class TimeSheetServiceTest extends BasicRptimeUnitTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimeSheetServiceTest.class);
	private static final Long ID_1 = 1L;
	private static final Long WORKER_ID = 123L;
	private static final LocalDate TEST_DATE = new LocalDate(2013, 12, 1); // Dec 1, 2013 is Sunday
	private static final Interval TEST_INTERVAL_CONTAINING_TEST_DATE = TEST_DATE.toInterval();

	private TimeSheetService service;

	private TimeSheet mockTimeSheet;
	@Mock
	private TimeSheetDao mockTimeSheetDao;
	@Mock
	private TimeSheetDay mockTimeSheetDay;
	@Mock
	private TimeSheetDayDao mockTimeSheetDayDao;
	private List<TimeSheetDay> mockTimeSheetDayList;

	@Mock
	private ClientService mockClientService;
	@Mock
	private Contract mockContract;
	@Mock
	private ContractService mockContractService;
	private List<Contract> mockContractList;

	public TimeSheetServiceTest() {
	}

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this); // initializes @Mock
		service = new TimeSheetService();
		service.setTimeSheetDao(mockTimeSheetDao);
		service.setTimeSheetDayDao(mockTimeSheetDayDao);
		service.clientService = mockClientService;
		service.contractService = mockContractService;

		mockTimeSheet = new TimeSheet();
		mockTimeSheet.setStartDate(TEST_DATE);
		mockTimeSheet.setId(ID_1);
		mockContractList = new ArrayList<>();
		mockContractList.add(mockContract);
		mockTimeSheetDayList = new ArrayList<>();
	}

	@After
	public void tearDown() {
	}

	@Test(expected = NullPointerException.class)
	public void failNoDao1() {
		service.delete(ID_1);
	}

	@Test(expected = NullPointerException.class)
	public void failNoDao2() {
		service.delete(ID_1);
	}

	@Test
	public void daosSet() {
		TimeSheet sheet = new TimeSheet();
		when(mockTimeSheetDao.getById(ID_1)).thenReturn(sheet);
		service.delete(ID_1);
	}

	@Test
	public void testGetForWorkerDate() {
		when(mockTimeSheetDao.getById(ID_1)).thenReturn(mockTimeSheet);
		// mockTimeSheetDao.s
		when(mockTimeSheetDao.set(any(TimeSheet.class), anyListOf(TimeSheetDay.class))).thenReturn(mockTimeSheet);
		when(mockContractService.getActiveContractsForWorkerInterval(any(Long.class), any(Interval.class))).thenReturn(mockContractList);
		when(mockTimeSheetDayDao.set(any(TimeSheetDay.class))).thenReturn(mockTimeSheetDay);
		// when(mockTimeSheetDayDao.set(any(TimeSheetDay.class),null)).thenReturn(mockTimeSheetDay);
		when(mockTimeSheetDayDao.getEntries(anyListOf(Long.class))).thenReturn(mockTimeSheetDayList);
		service.getForWorkerDate(ID_1, TEST_DATE);
	}

	@Test
	public void testGetForWorkerDateExisting() {
		when(mockTimeSheetDao.getByWorkerWeekYear(any(Long.class), any(int.class), any(int.class))).thenReturn(mockTimeSheet);
		TimeSheetView result = service.getForWorkerDate(ID_1, TEST_DATE);
		assertEquals(result.getId(), mockTimeSheet.getId());
	}

	@Test(expected = ContractNotFoundException.class)
	public void testGetForWorkerDateContractNotFoundException() {
		when(mockTimeSheetDao.getById(ID_1)).thenReturn(mockTimeSheet);
		service.getForWorkerDate(ID_1, TEST_DATE);
	}

	@Test
	public void testNormalizeStartDate() {
		LocalDate day = new LocalDate(TEST_DATE);
		for (int i = 0; i < 365; i++) {
            LOGGER.trace("Rectifying date for day {}", day);
			day = service.normalizeStartDate(day);
			assertEquals(DateTimeConstants.SUNDAY, day.getDayOfWeek());
			day = day.plusDays(i);
		}
	}

	// TODO add test methods here.
	// The methods must be annotated with annotation @Test. For example:
	//
	// @Test
	// public void hello() {}

	@Test
	@Ignore
	public void getNull() {
		TimeSheet sheet = new TimeSheet();
		when(mockTimeSheetDao.getById(ID_1)).thenReturn(sheet);

		TimeSheetView view = service.getForWorkerDate(WORKER_ID, START_DATE);

		assertNull(view);
	}
}
