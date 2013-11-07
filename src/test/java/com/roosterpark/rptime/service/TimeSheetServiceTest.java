/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.roosterpark.rptime.service;

import com.roosterpark.rptime.BasicRptimeUnitTest;
import com.roosterpark.rptime.model.TimeSheet;
import com.roosterpark.rptime.service.dao.TimeSheetDao;
import com.roosterpark.rptime.service.dao.TimeSheetDayDao;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author scandeezy
 */
public class TimeSheetServiceTest extends BasicRptimeUnitTest
{
    private static final Long ID_1 = 1L;
    
    private TimeSheetService service;
    
    public TimeSheetServiceTest()
    {
        service = new TimeSheetService();
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }
    
    @Test (expected=NullPointerException.class)
    public void failNoDao1() {
        service.delete(ID_1);
    }
    
    @Test (expected=NullPointerException.class)
    public void failNoDao2() {
        TimeSheetDao dao = mock(TimeSheetDao.class);
        service.setTimeSheetDao(dao);
        service.delete(ID_1);
    }
    
    @Test
    public void daosSet() {
        TimeSheet sheet = new TimeSheet();
        TimeSheetDao dao = mock(TimeSheetDao.class);
        when(dao.getById(ID_1)).thenReturn(sheet);
        service.setTimeSheetDao(dao);
        TimeSheetDayDao dao2 = mock(TimeSheetDayDao.class);
        service.setTimeSheetDayDao(dao2);
        service.delete(ID_1);
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
