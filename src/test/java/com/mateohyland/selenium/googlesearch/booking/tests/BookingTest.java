package com.mateohyland.selenium.googlesearch.booking.tests;

import com.mateohyland.selenium.googlesearch.TestNGTest;
import org.testng.annotations.BeforeMethod;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class BookingTest extends TestNGTest {

    protected DateTimeFormatter dateFormat;

    @BeforeMethod
    public void beforeTest(){
        super.beforeTest();
        dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
    }

}
