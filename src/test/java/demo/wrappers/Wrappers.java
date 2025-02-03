package demo.wrappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class Wrappers {
    public static WebDriverWait wait;

    public static void nevaigateToURLWait(WebDriver driver, String url) {
        driver.get(url);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
                .equals("complete"));
    }

    public static void clickOnElement(WebDriver driver, By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    public static ArrayList<TeamDitails> collectTeamNameAndYearAndWinPercentLessThanFourty(WebDriver driver, By locator,
            int pageLimit) throws InterruptedException {
        boolean result = false;
        // craete array list
        ArrayList<TeamDitails> pegignationList = new ArrayList<>();

        // iterate the loop for pageLimit
        for (int i = 1; i <= pageLimit; i++) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            List<WebElement> paginationsData = driver.findElements(locator);

            for (int j = 0; j < paginationsData.size(); j++) {
                int temp = j + 1;
                String teamName = paginationsData.get(0)
                        .findElement(By.xpath("(//tr[@class='team']/td[@class='name'])["+temp+"]")).getText();
                String year = paginationsData.get(0)
                        .findElement(By.xpath("(//tr[@class='team']/td[@class='year'])["+temp+"]")).getText();
                String winPercent = paginationsData.get(0)
                        .findElement(By.xpath("(//tr[@class='team']/td[contains(@class,'pct text')])["+temp+"]"))
                        .getText();
                // convert string into double
                Thread.sleep(1000);
                double percentNumber = 0.0;
                try {
                    percentNumber = Double.parseDouble(winPercent);
                } catch (Exception e) {
                    System.out.println(" can not covert winPercent string to double " + e.getMessage());
                }
                if (percentNumber < 0.40) {
                    TeamDitails teamDitails = new TeamDitails();
                    teamDitails.putData(teamName, year, winPercent);
                    TeamDitails obj = teamDitails.getData();
                    pegignationList.add(obj);
                }
            }
            if( pageLimit != i ){  
                Thread.sleep(2000);
                clickOnElement(driver, By.xpath("//a[@aria-label='Next']"));
            }
        }
        return pegignationList;
    }

    public static void printingArayListData(List<TeamDitails> lists){
        System.out.println("printing");
        int i =1;
        for(TeamDitails list : lists){
            System.out.println(i + " Team Name : "+list.getTeamName()+" Year : "+list.getYear()+" WinPercent : "+list.getWinPercent());
            i++;
        }

    }

    public static boolean covertArrayListToJsonFile(ArrayList<TeamDitails> teamDitails){
        boolean result = true;

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        // adding data into inputmap
        int i = 1;
        for(TeamDitails ditail : teamDitails){
            // inputMap.put("Team Name", ditail.getTeamName());
            // inputMap.put("Year", ditail.getYear());
            // inputMap.put("WinPercent",ditail.getWinPercent());
            String teamNumber = "Team : "+i;
            String teamInfo = "Name : "+ditail.getTeamName()+" Year : "+ditail.getYear()+" WinPercent : "+ditail.getWinPercent();
            inputMap.put(teamNumber,teamInfo);
            i++;
        }

        // Converting map to a JSON payload as string
        try {
            String employeePrettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(inputMap);
            System.out.println(employeePrettyJson);
            
        } catch (JsonProcessingException e) {
            result = false;
            e.printStackTrace();

        }
        String userDir = System.getProperty("user.dir");

        //Writing JSON on a file
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(userDir + "\\src\\test\\resources\\JSONFromMap.json"), inputMap);
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        }
        return result;

    }

    public static ArrayList<FilmsDitails> stoardTopFiveFilmDitailsIntoArrayList(WebDriver driver, By locator) throws InterruptedException{

        // create array list
        ArrayList<FilmsDitails> filmsDitails = new ArrayList<>();
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        List<WebElement> numberOfYears = driver.findElements(locator);

        for(int i = 1; i <=  numberOfYears.size(); i++){
            WebElement year = driver.findElement(By.xpath("(//a[contains(@class,'year-link')])["+i+"]"));
            String yearText = year.getText();
            year.click();
            Thread.sleep(4000);
            List<WebElement> ditailsOfFilms = driver.findElements(By.xpath("//tr[@class='film']"));
            int j = 1;
            for(WebElement element : ditailsOfFilms){
                String titleText = element.findElement(By.xpath("(//tr[@class='film']/td[@class='film-title'])["+j+"]")).getText();
                String nominationText = element.findElement(By.xpath("(//tr[@class='film']/td[@class='film-nominations'])["+j+"]")).getText();
                String awardsText = element.findElement(By.xpath("(//tr[@class='film']/td[@class='film-awards'])["+j+"]")).getText();
                boolean status = Wrappers.isWinner(driver,By.xpath("//tr[@class='film']/td[@class='film-best-picture']/i"));
                String isWinnerText;
                if (status) {
                    isWinnerText = "true";                
                }else{
                    isWinnerText = "false";
                }

                Thread.sleep(1000);;
                FilmsDitails film = new FilmsDitails(yearText, titleText, nominationText, awardsText, isWinnerText);
                filmsDitails.add(film);

                // onaly insert top five records
                if(j == 5){
                    break;
                }
                j++;
            }
        }
        return filmsDitails;

    }

    public static boolean covertArrayListToJsonFileForFilm(ArrayList<FilmsDitails> filmsDitails){
        boolean result = true;

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> inputMap = new HashMap<String, Object>();
        // adding data into inputmap
        int i = 1;
        for(FilmsDitails ditail : filmsDitails){
           
            String flimNumber = "Flim : "+i;
            String flimInfo = "Title : "+ditail.getFilmTitle()+" Year : "+ditail.getFlimYear()+" Nomination : "+ditail.getFilmNomination()+" Awards : "+ditail.getFilmAwards()+" isWinner : "+ditail.isFilmWinner();
            inputMap.put(flimNumber,flimInfo);
            i++;
        }

        // Converting map to a JSON payload as string
        try {
            String employeePrettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(inputMap);
            System.out.println(employeePrettyJson);
            
        } catch (JsonProcessingException e) {
            result = false;
            e.printStackTrace();

        }
        String userDir = System.getProperty("user.dir");

        //Writing JSON on a file
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(userDir + "\\src\\test\\resources\\oscar-winner-data.json"), inputMap);
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        }
        return result;

    }

    public static boolean isWinner(WebDriver driver, By locator){
        
        List<WebElement> elements = driver.findElements(locator);
        if (elements.size() == 0) {
            return false;
        }else{
            return true;
        }

    }

    public static boolean fileIsPresent(String fileName){

        String userDir = System.getProperty("user.dir");

        String filePath = userDir+"\\src\\test\\resources\\"+fileName;
        File file = new File(filePath);

        if (file.exists()  && file.length() > 0) {
            return true;
        } else {
            return false;
        }


    }

}
