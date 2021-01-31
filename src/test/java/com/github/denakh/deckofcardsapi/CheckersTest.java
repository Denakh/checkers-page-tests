package com.github.denakh.deckofcardsapi;

import com.github.denakh.deckofcardsapi.pageobject.CheckersPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class CheckersTest {
    private static final String CHECKERS_URL = "https://www.gamesforthebrain.com/game/checkers/";

    private static WebDriver driver;
    private static CheckersPage checkersPage;

    @BeforeClass
    public static void beforeClass() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get(CHECKERS_URL);
        checkersPage = new CheckersPage(driver);
        checkersPage.checkAllCellsAreFound();
    }

    @AfterClass
    public static void afterClass() {
        driver.manage().deleteAllCookies();
        driver.close();
    }
}
