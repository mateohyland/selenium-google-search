package com.mateohyland.selenium.googlesearch.despegar.tests;

import com.mateohyland.selenium.googlesearch.TestNGTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

public class FlightBookingTest extends TestNGTest {

    @Test
    public void testDespegarFlightBooking() throws InterruptedException {
        //-------------------------------------FLIGHT BOOKING TEST------------------------------------------------------
        //Goal: to exercise acquired knowledge developing a similar test to the previous one using a different website.

        //TODO: NEVER use aria-label OR placeholders as selectors - prioritize css selectors, use hierarchy and learned techniques.
        //Use webdriverwait.

        //Calendar startDate = setUTCMidnight(Calendar.getInstance());

        //Calendar endDate = (Calendar) startDate.clone();
        //endDate.add(Calendar.DATE, 10);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.from(startDate).plusDays(10);

        driver.get("https://www.despegar.com.ar/");

        WebDriverWait waitForPopup = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement loginPopupClose = waitForPopup.until(ExpectedConditions.visibilityOfElementLocated(By.
                className("login-incentive--close")));
        loginPopupClose.click();

        WebElement originInput = driver.findElements(By.cssSelector(".sbox-places-origin--G_Rvw input")).get(0);
        originInput.click();
        originInput.sendKeys("Buenos Aires");

        WebDriverWait waitForOriginOptions = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement firstOriginElement = waitForOriginOptions.until(ExpectedConditions.
                visibilityOfElementLocated(By.cssSelector(".item.-active")));
        firstOriginElement.click();

        WebDriverWait waitForDropdown = new WebDriverWait(driver, Duration.ofSeconds(5));
        waitForDropdown.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ac-container")));

        WebDriverWait waitForDestinationInput = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement destinationInput = waitForDestinationInput.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".sbox-places-destination--1xd0k input")));
        destinationInput.click();

        //TODO: write a CUSTOM expected condition that returns the dropdown itself in order to avoid the sleep.
        //This sleep was put in place in order to avoid a bug on the site only discovered when running automatized tests.
        Thread.sleep(500);

        destinationInput.sendKeys("Paris");

        WebDriverWait waitForDestinationOptions = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement firstDestinationElement = waitForDestinationOptions.until(ExpectedConditions.
                visibilityOfElementLocated(By.cssSelector(".item.-active")));
        firstDestinationElement.click();

        WebElement departureCalendar = driver.findElements(By.cssSelector(".sbox5-dates-input1")).get(0);
        departureCalendar.click();

        WebElement departureDate = driver.findElements(By.cssSelector(".sbox5-monthgrid-datenumber.-today")).get(0);
        departureDate.click();

        //TODO: Page Object Model -> order

        //TODO: change selector, find correct date by other methods - perhaps using Calendar class (see previous tests).
        WebElement returnDate = driver.findElements(By.
                cssSelector(".sbox5-monthgrid-datenumber.-in-range:nth-of-type(7)")).get(0);
        returnDate.click();

        WebElement applyButton = driver.findElement(By.cssSelector(".sbox5-3-btn.-primary.-lg"));
        applyButton.click();

        WebElement passengerCount = driver.findElements(By.cssSelector(".sbox5-distributionPassengers")).get(0);
        passengerCount.click();

        WebElement addAdultPassenger = driver.findElements(By.cssSelector(".steppers-icon-right")).get(0);
        addAdultPassenger.click();

        WebElement addChildPassenger = driver.findElements(By.cssSelector(".steppers-icon-right")).get(1);
        addChildPassenger.click();

        WebElement childAgeDropdown = driver.findElement(By.cssSelector(".select.select"));
        childAgeDropdown.click();

        WebElement childAge = driver.findElement(By.cssSelector("[value=\"7\"]"));
        childAge.click();

        WebElement applyButton2 = driver.findElement(By.cssSelector(".sbox5-3-btn.-md.-primary"));
        applyButton2.click();

        WebElement searchButton = driver.findElement(By.cssSelector(".sbox5-box-button-ovr--3LK5x"));
        searchButton.click();

        //TODO: Validate landing page results correspond with search terms inputted.

    }
}
