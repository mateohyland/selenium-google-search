package com.mateohyland.selenium.googlesearch;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import java.text.DateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

//TODO: Download new driver that can work with the current version of chrome.

public class MainTest {

    public static WebDriverManager wdm;

    protected WebDriver driver;
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
    }

    @BeforeSuite
    public void beforeAll(){
        wdm = WebDriverManager.getInstance("chrome");
        wdm.setup();
    }

    @BeforeMethod
    public void beforeTest(){
        driver = wdm.create();
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

        //TODO: NEVER use aria-label OR placeholders as selectors - prioritize css selectors, use hierarchy and learned techniques.

        Calendar startDate = setUTCMidnight(Calendar.getInstance());

        Calendar endDate = (Calendar) startDate.clone();
        endDate.add(Calendar.DATE, 10);

        driver.get("https://www.kayak.com.ar/cars");

        //Click on dropdown.
            //NOTE: Appropriate selector is used.
        WebDriverWait waitForDropdown = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement dropdown = waitForDropdown.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".wIIH-handle")));
        dropdown.click();

        //Click on "Different drop off".
            //NOTE: Check out li:nth-of-type
        WebDriverWait waitForDifferentReturnLocation = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement differentReturnLocationButton = waitForDifferentReturnLocation.until(ExpectedConditions.
                visibilityOfElementLocated(By.cssSelector(".QHyi li:last-of-type")));
        differentReturnLocationButton.click();

        //Wait for input options.
        WebDriverWait waitForOriginInput = new WebDriverWait(driver, Duration.ofSeconds(5));

        //Input "Mar del Plata".
        WebElement originInput = waitForOriginInput.until(ExpectedConditions.visibilityOfElementLocated(By.
                cssSelector("#ss_origin")));
        originInput.sendKeys("Mar del Plata");

        //TODO: Check selector on 06/10 meeting.
        //Select first option.
        WebDriverWait waitForOriginOptions = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement firstOriginElement = waitForOriginOptions.until(ExpectedConditions.visibilityOfElementLocated(By.
                cssSelector("[data-value=\"Mar del Plata, Provincia de Buenos Aires, Argentina\"]")));
        //Thread.sleep(3000);
        firstOriginElement.click();

        //Input "Bahía Blanca".
        WebElement destinationInput = driver.findElement(By.cssSelector("#ss"));
        //Thread.sleep(3000);
        destinationInput.sendKeys("Bahía Blanca");

        //TODO: Check selector on 06/10 meeting.
        //Select first option.
        WebDriverWait waitForDestinationOptions = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement firstDestinationElement = waitForDestinationOptions.until(ExpectedConditions.
                visibilityOfElementLocated(By.
                        cssSelector("[data-value=\"Bahía Blanca, Provincia de Buenos Aires, Argentina\"]")));
        //Thread.sleep(3000);
        firstDestinationElement.click();

        //------------------------------------------------NEW ASSIGNMENT------------------------------------------------
        //TODO: Select a date four months from now as pickup, then a month from that date as drop-off (after optimizing
        // existing test).

        //Note on nth-of-type:
        //table:nth-of-type(2) tr:nth-of-type(2) td:nth-of-type(2)

        //TODO: Use calendar to calculate dates. Check earlier tests.

        //Click on calendar
        WebElement carCalendar = driver.findElements(By.cssSelector(".sb-date-field__icon")).get(0);
        //Thread.sleep(3000);
        carCalendar.click();

        //TODO: Select a date four months from now as pick-up.
        //WebDriverWait waitForCheckinDate = new WebDriverWait(driver, Duration.ofSeconds(5));

        //This loop will make the 240th day from the start date visible by accessing the fourth month calendar.
        WebElement nextMonth = driver.findElements(By.cssSelector(".c2-button-inner")).get(1);
        for(int i = 0; i < 3; i++){
            nextMonth.click();
        }

        //TODO: Fix this selector, it doesn't work. My thinking: four months from now is 240 days from now, so I pick a
        // date 240 days or four months from today.
        WebElement carCheckInDate = driver.findElement(By.cssSelector(".c2-day td:nth-of-type(240)"));

        //TODO: Fix Assertion
        //Thread.sleep(3000);
        Assert.assertEquals(carCheckInDate.getText(),"" + startDate.get(Calendar.DAY_OF_MONTH));
        carCheckInDate.click();

        //TODO: Select a date a month after pick-up as drop-off.

        //TODO: Fix this selector, it doesn't work. My thinking: five months from now is 270 days from now, so I pick a
        // date 270 days or five months from today.
        WebElement carCheckOutDate = driver.findElement(By.cssSelector(".c2-day td:nth-of-type(270)"));

        //TODO: Fix Assertion
        Assert.assertEquals(carCheckOutDate.getText().trim(), "" + endDate.get(Calendar.DAY_OF_MONTH));
        carCheckOutDate.click();

        //Click on search button.
        WebElement carSearchButton = driver.findElement(By.cssSelector(".MPAi-button"));
        carSearchButton.click();

       //Verify pick-up and drop-off locations correspond with selected.
        WebElement landingPageLocationInfo = driver.findElements(By.
                cssSelector(".eszI-location")).get(1);

        Assert.assertTrue(
                landingPageLocationInfo.getText().contains("Mar del Plata")
                        && landingPageLocationInfo.getText().contains("Bahía Blanca"),
                "Landing page header did not contain requested locations"
        );

        //TODO: Fix assertions -  pick-up and drop-off dates correspond with selected.
            //TODO: Check if selectors can be optimized by eliminating style references.
        Assert.assertTrue(driver.findElement(By.cssSelector(".lfBz-field-outline.lfBz-mod-presentation-compact.lfBz-mod-full-width"))
                        .getText().contains("" + startDate.get(Calendar.DAY_OF_MONTH)),
                "Landing page check in date did not match selected check in date");
        Assert.assertTrue(driver.findElement(By.cssSelector(".lfBz-field-outline.lfBz-mod-presentation-compact.lfBz-mod-full-width"))
                        .getText().contains("" + endDate.get(Calendar.DAY_OF_MONTH)),
                "Landing page check out date did not match selected check out date");
    }

    @Test
    public void testDespegarFlightBooking() throws InterruptedException {
        //-------------------------------------FLIGHT BOOKING TEST------------------------------------------------------
        //Goal: to exercise acquired knowledge developing a similar test to the previous one using a different website.

        //TODO: NEVER use aria-label OR placeholders as selectors - prioritize css selectors, use hierarchy and learned techniques.
        //TODO: NEVER use sleepers. Only use WaitFor.

        //Use webdriverwait.

        Calendar startDate = setUTCMidnight(Calendar.getInstance());

        Calendar endDate = (Calendar) startDate.clone();
        endDate.add(Calendar.DATE, 10);

        driver.get("https://www.despegar.com.ar/");

        WebElement originInput = driver.findElements(By.cssSelector(".sbox-places-origin--G_Rvw input")).get(0);
        originInput.sendKeys("Buenos Aires");

        WebElement firstOriginElement = driver.findElement(By.cssSelector(".item.-active"));
        firstOriginElement.click();

        WebElement destinationInput = driver.findElements(By.cssSelector(".sbox-places-destination--1xd0k")).get(0);
        destinationInput.sendKeys("Paris");

        WebElement firstDestinationElement = driver.findElement(By.cssSelector(".item.-active"));
        firstDestinationElement.click();

        WebElement departureCalendar = driver.findElements(By.cssSelector(".sbox5-dates-input1")).get(0);
        departureCalendar.click();

        Thread.sleep(3000);

        WebElement departureDate = driver.findElements(By.cssSelector(".sbox5-monthgrid-datenumber.-today")).get(0);
        departureDate.click();

        Thread.sleep(3000);

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
