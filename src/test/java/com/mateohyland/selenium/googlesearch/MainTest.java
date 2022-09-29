package com.mateohyland.selenium.googlesearch;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainTest {

    protected ChromeDriver driver;
    protected DateFormat dateFormat;

    public MainTest(){
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        // TODO: Find a better way to do this
        System.setProperty("webdriver.chrome.silentOutput", "true");
        System.setProperty("webdriver.edge.silentOutput", "true");
        System.setProperty("webdriver.firefox.logfile", "/dev/null");
        System.setProperty("webdriver.ie.driver.silent", "true");
        System.setProperty("webdriver.opera.silentOutput", "true");
        System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");
    }

    @BeforeMethod
    public void beforeTest(){
        driver = new ChromeDriver();
        dateFormat = DateFormat.getDateInstance(DateFormat.FULL, new Locale("es", "AR"));
    }

    @Test
    public void testHotel() {
        //-----------------------------------ACCOMMODATION SEARCH TEST--------------------------------------------------
        driver.get("https://www.booking.com/index.es-ar.html");

        WebElement searchInput = driver.findElement(By.id("ss"));
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
        Calendar startDate = Calendar.getInstance();

        WebElement checkInDate = waitForCalendar.until(ExpectedConditions.visibilityOfElementLocated(By.
                cssSelector(".bui-calendar__date--today")));

        Assert.assertEquals(toISODate(startDate), checkInDate.getAttribute("data-date"));
        checkInDate.click();

        Calendar endDate = (Calendar) startDate.clone();
        endDate.add(Calendar.DATE, 10);

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

        //DONE: Verify the selected dates correspond with the information available on the landing page.
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-testid=\"date-display-field-start\"]")).getText(),
                startDate.get(Calendar.DAY_OF_MONTH) + "\n" + dateFormat.format(startDate.getTime()),
                "Landing page check in date did not match selected check in date");
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-testid=\"date-display-field-end\"]")).getText(),
                endDate.get(Calendar.DAY_OF_MONTH) + "\n" + dateFormat.format(endDate.getTime()),
                "Landing page check out date did not match selected check out date");
    }

    @Test
    public void testBookingCarRental() throws InterruptedException {
        //---------------------------------------CAR RENTAL TEST--------------------------------------------------------
        //Assignment: Test the same process with car rentals between two different cities using the same website.

        Calendar startDate = setUTCMidnight(Calendar.getInstance());

        Calendar endDate = (Calendar) startDate.clone();
        endDate.add(Calendar.DATE, 10);

        //Return to home page.
        driver.get("https://www.booking.com/index.es-ar.html");

        //Click on car rentals button.
        WebElement carRentalButton = driver.findElement(By.cssSelector("[data-decider-header=\"bookinggo\"]"));
        Thread.sleep(3000);
        carRentalButton.click();

        //Click on "Different return location" button.
        WebDriverWait waitForDifferentReturnLocation = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement differentReturnLocationButton = waitForDifferentReturnLocation.until(ExpectedConditions.
                visibilityOfElementLocated(By.cssSelector("[for=\"return-location-different\"]")));
        Thread.sleep(3000);
        differentReturnLocationButton.click();

        //Wait for input options.
        WebDriverWait waitForOriginInput = new WebDriverWait(driver, Duration.ofSeconds(5));

        //Input city of origin.
        WebElement originInput = waitForOriginInput.until(ExpectedConditions.visibilityOfElementLocated(By.
                cssSelector("#ss_origin")));
        Thread.sleep(3000);
        originInput.sendKeys("Mar del Plata");

        //Select origin site.
        WebDriverWait waitForOriginOptions = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement firstOriginElement = waitForOriginOptions.until(ExpectedConditions.visibilityOfElementLocated(By.
                cssSelector("ul.c-autocomplete__list.sb-autocomplete__list.-visible [data-i=\"0\"]")));
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
                        cssSelector("ul.c-autocomplete__list.sb-autocomplete__list.-visible [data-i=\"0\"]")));
        Thread.sleep(3000);
        firstDestinationElement.click();

        //Click on calendar.
        WebElement carCalendar = driver.findElement(By.cssSelector("[data-calendar2-type=\"checkin\"]"));
        Thread.sleep(3000);
        carCalendar.click();

        //DONE: Select today's date. Access visible calendar without using indexes.
        WebElement carCheckInDate = driver.findElement(By.
                cssSelector("[data-id=\"" + startDate.getTimeInMillis() + "\"]"));
        Assert.assertEquals("" + startDate.get(Calendar.DAY_OF_MONTH), carCheckInDate.getText());
        Thread.sleep(3000);
        carCheckInDate.click();

        //UNNECESSARY
        //WebElement carCheckOutDate = waitForCalendar.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-mode=\"checkout\"]")));
        //carCheckOutDate.click();

        //TODO: Select check-out date ten days from now - without using the index to access the check-out calendar.
        WebElement carCheckOutDate = driver.findElements(By.
                cssSelector("[data-id=\"" + endDate.getTimeInMillis() + "\"]")).get(1);

        Assert.assertEquals(carCheckOutDate.getText().trim(), "" + endDate.get(Calendar.DAY_OF_MONTH));
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
            //PROBLEM: Chrome perceives this test as a "bot" and as such does not allow me to proceed following line 171.
            //UPDATE (28/09 09:12): Tried solving problem by adding several flags to chrome configuration. Worked for the first try after that, but not afterwards.
            //UPDATE (28/09 09:33): Hotel test was re-ran, no related problems encountered.
            //UPDATE (28/09 10:11): Tried sprinkling driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); all over the code. Didn't work.
            //UPDATE (28/09 11:33): Tried same method but with Thread.sleep(3000); - worked in making the process go slower, didn't prevent browser interruption.
            //UPDATE (28/09 16:34): Will try to implement less actions and check whether that changes anything. Results: Nothing changed.

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
                startDate.get(Calendar.DAY_OF_MONTH) + "\n" + dateFormat.format(startDate.getTime()),
                "Landing page check in date did not match selected check in date");
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-testid=\"drop-off-date\"]")).getText(),
                endDate.get(Calendar.DAY_OF_MONTH) + "\n" + dateFormat.format(endDate.getTime()),
                "Landing page check out date did not match selected check out date");
    }

    @Test
    public void testKayakCarRental() throws InterruptedException {
        //---------------------------------------CAR RENTAL TEST--------------------------------------------------------
        //Assignment: Test the same process with car rentals between two different cities using a different website.

        //TODO: Get all necessary selectors and try the test.

        Calendar startDate = setUTCMidnight(Calendar.getInstance());

        Calendar endDate = (Calendar) startDate.clone();
        endDate.add(Calendar.DATE, 10);

        driver.get("https://www.kayak.com/cars");

        //TODO: Click on dropdown.
        WebElement dropdown = driver.findElement(By.cssSelector(""));
        Thread.sleep(3000);
        dropdown.click();

        //TODO: Click on "Different drop off".
        WebDriverWait waitForDifferentReturnLocation = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement differentReturnLocationButton = waitForDifferentReturnLocation.until(ExpectedConditions.
                visibilityOfElementLocated(By.cssSelector("")));
        Thread.sleep(3000);
        differentReturnLocationButton.click();

        //Wait for input options.
        WebDriverWait waitForOriginInput = new WebDriverWait(driver, Duration.ofSeconds(5));

        //TODO: Input "Mar del Plata".
        WebElement originInput = waitForOriginInput.until(ExpectedConditions.visibilityOfElementLocated(By.
                cssSelector("")));
        Thread.sleep(3000);
        originInput.sendKeys("Mar del Plata");

        //TODO: Select first option.
        WebDriverWait waitForOriginOptions = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement firstOriginElement = waitForOriginOptions.until(ExpectedConditions.visibilityOfElementLocated(By.
                cssSelector("")));
        Thread.sleep(3000);
        firstOriginElement.click();

        //TODO: Input "Bahía Blanca".
        WebElement destinationInput = driver.findElement(By.id(""));
        Thread.sleep(3000);
        destinationInput.sendKeys("Bahía Blanca");

        //TODO: Select first option.
        WebDriverWait waitForDestinationOptions = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement firstDestinationElement = waitForDestinationOptions.until(ExpectedConditions.
                visibilityOfElementLocated(By.
                        cssSelector("")));
        Thread.sleep(3000);
        firstDestinationElement.click();

        //TODO: Click on calendar
        WebElement carCalendar = driver.findElement(By.cssSelector(""));
        Thread.sleep(3000);
        carCalendar.click();

        //TODO: Click on pick up date, select today.
        WebElement carCheckInDate = driver.findElement(By.
                cssSelector(""));

        Assert.assertEquals("" + startDate.get(Calendar.DAY_OF_MONTH), carCheckInDate.getText());
        Thread.sleep(3000);
        carCheckInDate.click();

        //TODO: Click on drop off date, select ten days from today.
        WebElement carCheckOutDate = driver.findElement(By.
                cssSelector(""));

        Assert.assertEquals(carCheckOutDate.getText().trim(), "" + endDate.get(Calendar.DAY_OF_MONTH));
        Thread.sleep(3000);
        carCheckOutDate.click();

        //TODO:: Click on search button.
        WebElement carSearchButton = driver.findElement(By.cssSelector(""));
        Thread.sleep(3000);
        carSearchButton.click();

       //TODO: Verify pick-up and drop-off locations correspond with selected.
        Thread.sleep(3000);
        WebElement landingPageCheckinInfo = new WebDriverWait(driver, Duration.ofSeconds(5)).
                until(ExpectedConditions.visibilityOfElementLocated(By.
                        cssSelector("")));
        WebElement landingPageCheckoutInfo = new WebDriverWait(driver, Duration.ofSeconds(5)).
                until(ExpectedConditions.visibilityOfElementLocated(By.
                        cssSelector("")));

        Assert.assertTrue(
                landingPageCheckinInfo.getText().contains("Mar del Plata")
                        && landingPageCheckoutInfo.getText().contains("Bahía Blanca"),
                "Landing page header did not contain requested location"
        );

        //TODO: Verify pick-up and drop-off dates correspond with selected.
        Assert.assertEquals(driver.findElement(By.cssSelector("")).getText(),
                startDate.get(Calendar.DAY_OF_MONTH) + "\n" + dateFormat.format(startDate.getTime()),
                "Landing page check in date did not match selected check in date");
        Assert.assertEquals(driver.findElement(By.cssSelector("")).getText(),
                endDate.get(Calendar.DAY_OF_MONTH) + "\n" + dateFormat.format(endDate.getTime()),
                "Landing page check out date did not match selected check out date");
    }

    public String toISODate(Calendar calendar){
        return DateTimeFormatter.ISO_LOCAL_DATE.format(calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
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

    //DONE: Create a local repository using Git, then commit this project.
}
