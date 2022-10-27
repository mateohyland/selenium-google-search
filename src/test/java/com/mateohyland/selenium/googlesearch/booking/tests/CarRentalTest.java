package com.mateohyland.selenium.googlesearch.booking.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Calendar;

public class CarRentalTest extends BookingTest{

    @Test
    public void testBookingCarRental() throws InterruptedException {
        //---------------------------------------CAR RENTAL TEST--------------------------------------------------------
        //Assignment: Test the same process with car rentals between two different cities using the same website.

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(10);

        //Return to home page.
        driver.get("https://www.booking.com/index.es-ar.html");

        //Click on car rentals button.
        WebElement carRentalButton = driver.findElement(By.cssSelector("[data-decider-header=\"bookinggo\"]"));
        Thread.sleep(3000);
        carRentalButton.click();

        //Click on "Different return location" button.
        WebDriverWait waitForDifferentReturnLocation = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement differentReturnLocationButton = waitForDifferentReturnLocation.until(ExpectedConditions.
                visibilityOfElementLocated(By.id("return-location-different")));
        Thread.sleep(3000);
        differentReturnLocationButton.click();

        //Wait for input options.
        WebDriverWait waitForOriginInput = new WebDriverWait(driver, Duration.ofSeconds(5));

        //Input city of origin.
        WebElement originInput = waitForOriginInput.until(ExpectedConditions.visibilityOfElementLocated(By.
                id("ss_origin")));
        Thread.sleep(3000);
        originInput.sendKeys("Mar del Plata");

        //Select origin site.
        WebDriverWait waitForOriginOptions = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement firstOriginElement = waitForOriginOptions.until(ExpectedConditions.visibilityOfElementLocated(By.
                cssSelector(".sb-autocomplete__item-with_photo.sb-autocomplete__item--city")));
        Thread.sleep(3000);
        firstOriginElement.click();

        //Input destination site.
        WebElement destinationInput = driver.findElement(By.id("ss"));
        Thread.sleep(3000);
        destinationInput.sendKeys("Bahía Blanca");

        //Select destination site.
        WebDriverWait waitForDestinationOptions = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement firstDestinationElement = waitForDestinationOptions.until(ExpectedConditions.
                visibilityOfElementLocated(By.
                        cssSelector(".sb-autocomplete__item-with_photo.sb-autocomplete__item--city")));
        Thread.sleep(3000);
        firstDestinationElement.click();

        //Click on calendar.
        WebElement carCalendar = driver.findElement(By.cssSelector("[data-calendar2-type=\"checkin\"]"));
        Thread.sleep(3000);
        carCalendar.click();

        //DONE: Select today's date. Access visible calendar without using indexes.
        WebElement carCheckInDate = driver.findElement(By.
                cssSelector("[data-id=\"" + startDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC)*1000 + "\"]"));
        Assert.assertEquals("" + startDate.getDayOfMonth(), carCheckInDate.getText());
        Thread.sleep(3000);
        carCheckInDate.click();

        //UNNECESSARY
        //WebElement carCheckOutDate = waitForCalendar.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-mode=\"checkout\"]")));
        //carCheckOutDate.click();

        //TODO: Select check-out date ten days from now - without using the index to access the check-out calendar.
        WebElement carCheckOutDate = driver.findElements(By.
                cssSelector("[data-id=\"" + endDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC)*1000 + "\"]")).get(1);

        Assert.assertEquals(carCheckOutDate.getText().trim(), "" + endDate.getDayOfMonth());
        Thread.sleep(3000);
        carCheckOutDate.click();

        //DONE: Click on search button.
        WebElement carSearchButton = driver.findElement(By.cssSelector(".sb-searchbox__button"));
        Thread.sleep(3000);
        carSearchButton.click();

        //DONE: Verify calendar is no longer visible.
        WebDriverWait waitForHiddenCarCalendar = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitForHiddenCarCalendar.until(ExpectedConditions.invisibilityOfElementLocated(By.
                cssSelector(".bui-calendar__content")));

        //TODO: Verify origin and destination sites correspond with selected.

        Thread.sleep(3000);
        WebElement landingPageCheckinInfo = new WebDriverWait(driver, Duration.ofSeconds(5)).
                until(ExpectedConditions.visibilityOfElementLocated(By.
                        cssSelector("[data-testid=\"pick-up-location\"]")));
        WebElement landingPageCheckoutInfo = new WebDriverWait(driver, Duration.ofSeconds(5)).
                until(ExpectedConditions.visibilityOfElementLocated(By.
                        cssSelector("[data-testid=\"drop-off-location\"]")));

        Assert.assertTrue(
                landingPageCheckinInfo.getText().contains("Mar del Plata")
                        && landingPageCheckoutInfo.getText().contains("Bahía Blanca"),
                "Landing page header did not contain requested location"
        );

        //TODO: Verify check-in and check-out dates correspond with selected.
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-testid=\"pick-up-date\"]")).getText(),
                startDate.getDayOfMonth() + "\n" + dateFormat.format(startDate),
                "Landing page check in date did not match selected check in date");
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-testid=\"drop-off-date\"]")).getText(),
                endDate.getDayOfMonth() + "\n" + dateFormat.format(endDate),
                "Landing page check out date did not match selected check out date");
    }

}
