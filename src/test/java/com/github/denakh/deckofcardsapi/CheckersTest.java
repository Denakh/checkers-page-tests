package com.github.denakh.deckofcardsapi;

import com.github.denakh.deckofcardsapi.pageobject.CheckersPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertTrue;

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

    @Test
    public void userCanMoveCheckerTwice() {
        //given: a draught from front line
        WebElement draught = checkersPage.selectRandomDraughtFromFrontLine();

        //when: click on the draught
        checkersPage.clickOnDraught(draught);

        //and: move the draught forward and left
        WebElement newCell = checkersPage.moveDraughtForwardAndLeft(draught);

        //then: the draught is on the new cell or beaten
        assertTrue(checkersPage.isDraughtOnCellOrBeaten(newCell));

        //when: click on the draught
        draught = newCell;
        checkersPage.clickOnDraught(draught);

        //and: move the draught forward and right
        newCell = checkersPage.moveDraughtForwardAndRight(draught);

        //then: the draught is on the new cell or beaten
        assertTrue(checkersPage.isDraughtOnCellOrBeaten(newCell));
    }

    @AfterClass
    public static void afterClass() {
        driver.manage().deleteAllCookies();
        driver.close();
    }
}
