package com.github.denakh.deckofcardsapi.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CheckersPage {

    private static final Integer waitingTimeInSec = 5;
    private WebDriver driver;

    @FindBys(@FindBy(css = "img[name]"))
    private List<WebElement> boardCells;

    @FindBy(id = "message")
    private WebElement message;

    public CheckersPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public CheckersPage clickOnDraught(WebElement cell) {
        for (int i = 1; i <= 5; i++) {
            cell.click();
            if (cell.getAttribute("src") != null && cell.getAttribute("src").contains("you2.gif")) return this;
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public Boolean canCellBeUsed(WebElement cell) {
        return cell.getAttribute("src") != null && cell.getAttribute("src").contains("gray.gif");
    }

    public void checkAllCellsAreFound() {
        if (boardCells.size() != 64) {
            boardCells = new WebDriverWait(driver, waitingTimeInSec)
                    .until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("img[name]"), 64));
        }
    }

    public WebElement selectRandomDraughtFromFrontLine() {
        List<WebElement> draughtsFromFrontLine = boardCells.stream()
                .filter(cell -> cell.getAttribute("name").endsWith("2") && cell.getAttribute("src").contains("you1.gif"))
                .collect(Collectors.toList());
        Collections.shuffle(draughtsFromFrontLine);
        return draughtsFromFrontLine.stream().findFirst().get();
    }

    public WebElement moveDraughtForwardAndLeft(WebElement draughtOnCell) {
        String draughtCoordinateString = draughtOnCell.getAttribute("name");
        int draughtLineIndex = Integer.parseInt(draughtCoordinateString.substring(draughtCoordinateString.length() - 1));
        int draughtColumnIndex =
                Integer.parseInt(draughtCoordinateString.substring(draughtCoordinateString.length() - 2, draughtCoordinateString.length() - 1));
        if (draughtColumnIndex != 7) {
            WebElement newDraughtCell = driver.findElement(By.name("space" + (draughtColumnIndex + 1) + (draughtLineIndex + 1)));
            if (canCellBeUsed(newDraughtCell)) {
                newDraughtCell.click();
                waitForMakeMoveMessage();
                return newDraughtCell;
            }
        } else {
            System.out.println("Can't move the draught forward and left");
        }
        return draughtOnCell;
    }

    public WebElement moveCheckerForwardAndRight(WebElement draughtOnCell) {
        String draughtCoordinateString = draughtOnCell.getAttribute("name");
        int draughtLineIndex = Integer.parseInt(draughtCoordinateString.substring(draughtCoordinateString.length() - 1));
        int draughtColumnIndex =
                Integer.parseInt(draughtCoordinateString.substring(draughtCoordinateString.length() - 2, draughtCoordinateString.length() - 1));
        if (draughtColumnIndex != 0) {
            WebElement newDraughtCell = driver.findElement(By.name("space" + (draughtColumnIndex - 1) + (draughtLineIndex + 1)));
            if (canCellBeUsed(newDraughtCell)) {
                newDraughtCell.click();
                waitForMakeMoveMessage();
                return newDraughtCell;
            }
        } else {
            System.out.println("Can't move the draught forward and right");
        }
        return draughtOnCell;
    }

    public Boolean isDraughtOnCellOrBeaten(WebElement cell) {
        if (cell.getAttribute("src") != null && cell.getAttribute("src").contains("gray.gif")) {
            System.out.println("Your draught is beaten");
            return true;
        }
        return cell.getAttribute("src").contains("you1.gif") || cell.getAttribute("src").contains("you2.gif");
    }

    public CheckersPage waitForMakeMoveMessage() {
        new WebDriverWait(driver, waitingTimeInSec).until(ExpectedConditions.textToBePresentInElement(message, "Make a move"));
        return this;
    }
}
