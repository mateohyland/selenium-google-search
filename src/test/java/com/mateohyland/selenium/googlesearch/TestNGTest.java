package com.mateohyland.selenium.googlesearch;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;

public class TestNGTest {

    public static WebDriverManager wdm;

    protected WebDriver driver;

    public TestNGTest(){
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        // TODO: Find a better way to do this
        System.setProperty("webdriver.chrome.silentOutput", "true");
        System.setProperty("webdriver.edge.silentOutput", "true");
        System.setProperty("webdriver.firefox.logfile", "/dev/null");
        System.setProperty("webdriver.ie.driver.silent", "true");
        System.setProperty("webdriver.opera.silentOutput", "true");
    }

    @BeforeSuite
    public void beforeAll(){
        wdm = WebDriverManager.getInstance("chrome");
        wdm.setup();
    }

    @BeforeMethod
    public void beforeTest(){
        driver = wdm.create();
        System.out.println("Before Method executed correctly.");
    }

    public Calendar setUTCMidnight(Calendar calendar){
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    @AfterMethod
    public void after(){
        driver.quit();
    }

    public String toISODate(LocalDate localDate){
        return DateTimeFormatter.ISO_LOCAL_DATE.format(localDate);
    }
}
