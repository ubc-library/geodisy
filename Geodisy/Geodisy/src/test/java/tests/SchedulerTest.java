/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;




import BaseFiles.Scheduler;
import org.junit.*;


import java.util.Calendar;

import static org.junit.Assert.assertEquals;


/**
 *
 * @author pdante
 */
public class SchedulerTest {
    public SchedulerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class BaseFiles.Scheduler.
     */
    @Test
    public void testConstructor(){
        Scheduler schedule = new Scheduler();
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,2);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        assertEquals(schedule.getToday().get(Calendar.DAY_OF_MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }
    
}
