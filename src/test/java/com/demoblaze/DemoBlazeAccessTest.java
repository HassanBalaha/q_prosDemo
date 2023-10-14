package com.demoblaze;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;
import java.util.Random;

public class DemoBlazeAccessTest {
    WebDriver driver;
    String[] signUpData;
    @BeforeTest
    public void setupBrowser() {

        System.setProperty("webdriver.gecko.driver", "./FilesAndDrivers/geckodriver.exe");
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.get("https://www.demoblaze.com");
    }

    @Test
    public void demoBlazeAccess() throws Exception {
        register();
        logIn();
        checkListedCatHasItems();
        addItemToCart();
        removeItemFromCart();
        successfulCheckOut();

    }

    private void logIn() throws Exception {
        boolean present;
        try {
            WebElement SignupButton = driver.findElement(By.id("login2"));
            SignupButton.click();
            driver.findElement(By.id("logInModalLabel"));
            present = true;
        } catch (NoSuchElementException e) {
            present = false;
        }

       Assert.assertTrue(present);
            //sending letters to the  username & password fields
            WebElement username = driver.findElement(By.id("loginusername"));
            username.sendKeys(signUpData[0]);
            Thread.sleep(500);
            WebElement password = driver.findElement(By.id("loginpassword"));
            password.sendKeys(signUpData[1]);
            Thread.sleep(500);

            //finding the signup button in dialogue model & clocking it
            WebElement model_footer_Sign_up_button = driver.findElement(By.xpath(".//button[contains(text(),'Log in')]"));
            model_footer_Sign_up_button.click();

    }

    private String[] generatePersonalData() throws Exception {
        //generating csv file
        new CSVGEN().Gen1();
        //storing data from CSV file into an array for easier handling
        String[] varReadCsv = new ReadCSV().Read("./FilesAndDrivers/PersonalData.csv");
        return varReadCsv;
    }

    private void successfulCheckOut() {
    }


    private void removeItemFromCart() {
    }

    private void addItemToCart() {
    }

    private void checkListedCatHasItems() {
    }

    private void register() throws Exception {
        boolean present;

        signUpData=generatePersonalData();

        try {
            WebElement SignupButton = driver.findElement(By.id("signin2"));
            SignupButton.click();
            driver.findElement(By.id("signInModalLabel"));
            present = true;
        } catch (
                NoSuchElementException e) {
            present = false;
        }
        Assert.assertTrue(present, "signInModalLabel(Signup dialog)not found ");
        //sending letters to the  username & password fields
        WebElement username = driver.findElement(By.id("sign-username"));
        username.sendKeys(signUpData[0]);
        WebElement password = driver.findElement(By.id("sign-password"));
        password.sendKeys(signUpData[1]);

        //finding the signup button in dialogue model & clocking it
        WebElement model_footer_Sign_up_button = driver.findElement(By.xpath("//*[@id=\"signInModal\"]/div/div/div[3]/button[2]"));
        model_footer_Sign_up_button.click();
        Thread.sleep(1000);
        String alert_box_text = driver.switchTo().alert().getText();

        if (alert_box_text.equals("This user already exist.")) {
            driver.switchTo().alert().accept();
            Thread.sleep(500);

            username.clear();
            password.clear();

            signUpData[0] += String.valueOf(new Random().nextInt(700));

            username.sendKeys(signUpData[0]);
            password.sendKeys(signUpData[1]);

            Thread.sleep(500);

            model_footer_Sign_up_button.click();
            Thread.sleep(1000);
            driver.switchTo().alert().accept();

        } else {
            Thread.sleep(500);
            driver.switchTo().alert().accept();
            Thread.sleep(500);
        }
        Thread.sleep(1000);

    }
}
