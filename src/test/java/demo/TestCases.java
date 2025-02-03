package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import demo.wrappers.FilmsDitails;
import demo.wrappers.TeamDitails;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;
    @Test(enabled = true)
    public void testCase01() throws InterruptedException{
        System.out.println(" Start Test Case 1 - Collect Hockey Score Data");
        // naviage to particular url
        Wrappers.nevaigateToURLWait(driver, "https://www.scrapethissite.com/pages/");
        // click on Hockey Teams
        Wrappers.clickOnElement(driver, By.partialLinkText("Hockey Teams"));
        ArrayList<TeamDitails> teamDitails =  Wrappers.collectTeamNameAndYearAndWinPercentLessThanFourty(driver, By.xpath("//tr[@class='team']"), 1);
        // printing information 
        Wrappers.printingArayListData(teamDitails);
        // convert arrayList object to json File
        boolean status = Wrappers.covertArrayListToJsonFile(teamDitails);
        Assert.assertTrue(status);
        System.out.println(" End Test Case 1 - Collect Hockey Score Data");
    }

    @Test(enabled = true)
    public void testCase02() throws InterruptedException{
        System.out.println("Start Test Case 2 - Collect Film Award Data");
        // naviage to particular url
        Wrappers.nevaigateToURLWait(driver, "https://www.scrapethissite.com/pages/");
        // click on Oscar Winning Films
        Wrappers.clickOnElement(driver, By.partialLinkText("Oscar Winning Films"));
        ArrayList<FilmsDitails> filmsDitails = Wrappers.stoardTopFiveFilmDitailsIntoArrayList(driver, By.xpath("//a[contains(@class,'year-link')]"));
        boolean status = Wrappers.covertArrayListToJsonFileForFilm(filmsDitails);
        Assert.assertTrue(status);
        status = Wrappers.fileIsPresent("oscar-winner-data.json");
        Assert.assertTrue(status);
        System.out.println("End Test Case 2 - Collect Film Award Data");

    }
    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest()
    {
        driver.close();
        driver.quit();

    }
}