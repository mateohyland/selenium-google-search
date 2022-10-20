package com.mateohyland.selenium.googlesearch.booking.tests;

import com.mateohyland.selenium.googlesearch.TestNGTest;
import org.testng.annotations.BeforeMethod;

import java.text.DateFormat;
import java.util.Locale;

public class BookingTest extends TestNGTest {

    protected DateFormat dateFormat;

    @BeforeMethod
    public void beforeTest(){
        super.beforeTest();
        dateFormat = DateFormat.getDateInstance(DateFormat.FULL, new Locale("es", "AR"));
    }

}
