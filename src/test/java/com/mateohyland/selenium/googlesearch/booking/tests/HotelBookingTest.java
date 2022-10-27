package com.mateohyland.selenium.googlesearch.booking.tests;

import com.mateohyland.selenium.googlesearch.TestNGTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Calendar;

public class HotelBookingTest extends BookingTest {

    @Test
    public void testHotel() {
        //-----------------------------------ACCOMMODATION SEARCH TEST--------------------------------------------------
        driver.get("https://www.booking.com/index.es-ar.html");

        WebDriverWait waitForSearchInput = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchInput = waitForSearchInput.until(ExpectedConditions.visibilityOfElementLocated(By.id("ss")));
        searchInput.sendKeys("camboriu");

        WebDriverWait waitForOptions = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement firstElement = waitForOptions.until(ExpectedConditions.visibilityOfElementLocated(By.
                cssSelector("[data-component='search/destination/input'] ul.-visible [data-i='0']")));
        firstElement.click();

        //DONE: Verify calendar is visible.
        //Element reference: $$('.bui-calendar__content');
        WebDriverWait waitForCalendar = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement calendar = waitForCalendar.until(ExpectedConditions.visibilityOfElementLocated(By.
                cssSelector(".bui-calendar__content")));

        //DONE: Select dates for arrival and departure, selecting dates ten days apart.

        //Calendar startDate = setUTCMidnight(Calendar.getInstance());
        LocalDate startDate = LocalDate.now();

        WebElement checkInDate = waitForCalendar.until(ExpectedConditions.visibilityOfElementLocated(By.
                cssSelector(".bui-calendar__date--today")));

        Assert.assertEquals(toISODate(startDate), checkInDate.getAttribute("data-date"));
        checkInDate.click();

        LocalDate endDate = LocalDate.now().plusDays(7);

        WebElement checkOutDate = waitForCalendar.until(ExpectedConditions.visibilityOfElementLocated(By.
                cssSelector("td[data-date=\"" + toISODate(endDate) + "\"]")));
        checkOutDate.click();

        WebDriverWait waitForSearchButton = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement searchButton = waitForSearchButton.until(ExpectedConditions.visibilityOfElementLocated(By.
                cssSelector(".sb-searchbox__button")));
        searchButton.click();

        WebDriverWait waitForHiddenCalendar = new WebDriverWait(driver, Duration.ofSeconds(5));
        waitForHiddenCalendar.until(ExpectedConditions.invisibilityOfElementLocated(By.
                cssSelector(".bui-calendar__content")));

        //DONE: Verify the destination on the landing page correspond to the input data.
        Assert.assertTrue(driver.getTitle().contains("Camboriú"),
                "Landing page title did not match selected destination.");

        //DONE: Verify arp-header element contains input location.
        //[data-component="arp-header"] h1
        Assert.assertTrue(driver.findElement(By.
                        cssSelector("[data-component=\"arp-header\"] h1")).getText().contains("Camboriú"),
                "Landing page header did not contain requested location");

        //TODO: Fix error with these assertions.
        // Hypothesis: language difference.
        // Proposed solution: change site language to English.
        //DONE: Verify the selected dates correspond with the information available on the landing page.
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-testid=\"date-display-field-start\"]")).getText().toLowerCase(),
                startDate.getDayOfMonth() + "\n" + dateFormat.format(startDate).toLowerCase(),
                "Landing page check in date did not match selected check in date");

        Assert.assertEquals(driver.findElement(By.cssSelector("[data-testid=\"date-display-field-end\"]")).getText().toLowerCase(),
                endDate.getDayOfMonth() + "\n" + dateFormat.format(endDate).toLowerCase(),
                "Landing page check out date did not match selected check out date");
    }

}
