package com.demoblaze;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class DemoBlazeAccessTest {
    WebDriver driver;
    String[] signUpData;
    List<WebElement> bodyElements;
    int minPrice ;
    WebElement homeElement;
    String alertText;
//================================================
    @BeforeTest
    public void setupBrowser() {

        System.setProperty("webdriver.gecko.driver", "./FilesAndDrivers/geckodriver.exe");//setting up firefox driver and driver path
        driver = new FirefoxDriver();//creating a new instance of the driver
        driver.manage().window().maximize();//maximizing screen
        driver.get("https://www.demoblaze.com");//visiting site URL
    }

    @Test//Test dependency
    public void demoBlazeAccess() throws Exception {
        register();//Signup method
        logIn();//Login method
        checkListedCatHasItems();//Checking the listed Categories has Items
        addItemToCart();//adding items to cart method
        removeItemFromCart();//removing one item to cart method
        successfulCheckOut();//checking out and logging out method

    }

    private void register() throws Exception {
        boolean present;

        signUpData = generatePersonalData();//generating random personal data (check CSVGEN class) and storing in variable "signUpData"

        //checking weather signup modal dialog is visible
        try {
            WebElement SignupButton = driver.findElement(By.id("signin2"));
            SignupButton.click();
            driver.findElement(By.id("signInModalLabel"));
            present = true;
        } catch (
                NoSuchElementException e) {
            present = false;
        }

        Assert.assertTrue(present, "signInModalLabel(Signup dialog)not found ");//assering signup model is visible or sending exception message
        //sending letters to the  username & password fields
        WebElement username = driver.findElement(By.id("sign-username"));//locating username field
        username.sendKeys(signUpData[0]);//filling username field
        WebElement password = driver.findElement(By.id("sign-password"));//locating password field
        password.sendKeys(signUpData[1]);//filling password field

        //locating the signup button in dialogue model & clocking it
        WebElement model_footer_Sign_up_button = driver.findElement(By.xpath("//*[@id=\"signInModal\"]/div/div/div[3]/button[2]"));
        model_footer_Sign_up_button.click();
        Thread.sleep(1000);
        String alert_box_text = driver.switchTo().alert().getText();//getting alert text to analyze

        if (alert_box_text.equals("This user already exist.")) {
            //if the random username already exists in the database we'll change username by appending random numbers
            driver.switchTo().alert().accept();
            Thread.sleep(500);
            username.clear();//clearing username field
            password.clear();//clearing password field

            signUpData[0] += String.valueOf(new Random().nextInt(700));//appending random numbers to username field
            username.sendKeys(signUpData[0]);//filling username field using the new username
            password.sendKeys(signUpData[1]);//filling password field using same old password

            Thread.sleep(500);

            model_footer_Sign_up_button.click();//clicking signup button
            Thread.sleep(1000);
            driver.switchTo().alert().accept();//accepting alert

        } else {
            //if the first username we generated doesn't exist in the DB then just accept alert
            Thread.sleep(500);
            driver.switchTo().alert().accept();
            Thread.sleep(500);
        }
        Thread.sleep(1000);

    }

    //log in method
    private void logIn() throws Exception {
        boolean present;
        //checking weather Login modal dialog is visible
        try {
            WebElement SignupButton = driver.findElement(By.id("login2"));
            SignupButton.click();
            driver.findElement(By.id("logInModalLabel"));
            present = true;
        } catch (NoSuchElementException e) {
            present = false;
        }

        Assert.assertTrue(present,"logInModalLabel(login dialog)not found ");
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

    //method to generate random personal data to use throughout the process
    private String[] generatePersonalData() throws Exception {
        //generating csv file
        new CSVGEN().Gen();//using CSVGEN class
        //storing data from CSV file into an array for easier handling
        return new ReadCSV().Read("./FilesAndDrivers/PersonalData.csv");//reading CSV file using ReadCSV class
    }

    private void getMinPrice() throws Exception {
        //generating csv file
        int[] prices = new int[9];
        for (int j = 0; j < 9; j++) {
            //finding the prices of every element in the category list
            String StringTest = bodyElements.get(0).findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[" + (j + 1) + "]/div/div/h5")).getText();
            prices[j] = parseInt(StringTest.substring(1));//converting to integer & removing "$" and saving prices in array for debugging use
        }

        for (int price : prices) {
            System.out.println(price);
        }
        Thread.sleep(1000);
        int indexmin = 0;
        int min = prices[0];
        for (int x = 0; x < prices.length; x++) {
            if (prices[x] <= min) {
                min = prices[x];
                indexmin = x;
            }
        }
        minPrice = indexmin;//saving minimum price index in var to use later in "addItemToCart" method
    }


    private boolean checkListedCatHasItems() throws InterruptedException {//Checking if the Categories list has Items
        Thread.sleep(1500);

        int numberOfCategories=driver.findElements(By.className("card-title")).size();//number as showing in demoblaze.com should be 9
        //System.out.println(numberOfCategories);
        Thread.sleep(700);
        return  numberOfCategories >= 1;
    }

    private void addItemToCart() throws Exception {
        //=========================adding first item=========================
        bodyElements = driver.findElements(By.id("tbodyid"));
        Assert.assertTrue(checkListedCatHasItems(),"category list has no items ");//Checking if the Categories list has Items
        getMinPrice();//running "getMinPrice()" method to get the minimum price across items
        Thread.sleep(500);
        //oppening the lowest price item
        bodyElements.get(0).findElement(By.xpath("/html/body/div[5]/div/div[2]/div/div[" + (minPrice + 1) + "]/div/div/h4/a")).click();
        Thread.sleep(700);
        //adding item to cart
        driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div[2]/div/a")).click();
        Thread.sleep(700);
        //getting alert text to analyze
        alertText = driver.switchTo().alert().getText();
        //checking weather the product added to cart or not,if not show message
        Assert.assertEquals(alertText, "Product added.","product wasn't added to cart");
        driver.switchTo().alert().accept();
        Thread.sleep(500);
        //=========================adding second item=========================
        //navigating to home button
        homeElement=driver.findElement(By.id("navbarExample"));
        Thread.sleep(500);
        //clicking home button
        homeElement.findElement(By.xpath("/html/body/nav/div/div/ul/li[1]/a")).click();
        Thread.sleep(500);
        //choosing the element that has "galaxy s6" in the name
        driver.findElement(By.partialLinkText("galaxy s6")).click();
        Thread.sleep(800);
        //adding element to cart
        driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div[2]/div/a")).click();
        Thread.sleep(800);

        //getting alert text to analyze
        alertText = driver.switchTo().alert().getText();
        //checking weather the product added to cart or not,if not show message
        Assert.assertEquals(alertText, "Product added.","product wasn't added to cart");
        driver.switchTo().alert().accept();
        Thread.sleep(500);

    }
    private void removeItemFromCart() throws Exception {//removing item from the cart method
        //locating& clicking cart button
        homeElement=driver.findElement(By.id("navbarExample"));
        Thread.sleep(500);
        homeElement.findElement(By.id("cartur")).click();
        Thread.sleep(500);
        //locating and clicking the "Delete" button from the table
        List<WebElement> cartElementToBeDeleted=driver.findElements(By.id("tbodyid"));
        Thread.sleep(2000);
        cartElementToBeDeleted.get(0).findElements(By.xpath("/html/body/div[6]/div/div[1]/div/table/tbody/tr[1]/td[4]/a")).getLast().click();
        Thread.sleep(1000);
    }
    private void successfulCheckOut() throws InterruptedException {//checkout and logout method
        boolean orderModalLabelpresent;
        //locating and clicking "Place order button"
        WebElement placeOrderButton = driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/button"));
        placeOrderButton.click();
        Thread.sleep(500);
        //checking weather place order modal dialog is visible
        try {
            driver.findElement(By.id("orderModalLabel"));
            orderModalLabelpresent = true;
        } catch (NoSuchElementException e) {
            orderModalLabelpresent = false;
        }
        //Checking weather "place order dialog" is visible or not , if not send exception message
        Assert.assertTrue(orderModalLabelpresent,"orderModalLabel (order finalizing dialog ) not visible");
        //filling place order dialog using random personal data generated earlier by generatePersonalData() method
        WebElement name = driver.findElement(By.id("name"));
        name.sendKeys(signUpData[0]);
        Thread.sleep(500);
        WebElement Country = driver.findElement(By.id("country"));
        Country.sendKeys(signUpData[3]);
        Thread.sleep(500);
        WebElement city = driver.findElement(By.id("city"));
        city.sendKeys(signUpData[4]);
        Thread.sleep(500);
        WebElement card = driver.findElement(By.id("card"));
        card.sendKeys(signUpData[2]);
        Thread.sleep(500);
        WebElement month = driver.findElement(By.id("month"));
        month.sendKeys(signUpData[5]);
        Thread.sleep(500);
        WebElement year = driver.findElement(By.id("year"));
        year.sendKeys(signUpData[6]);
        Thread.sleep(3000);
        //locating and clicking "Purchase" button
        driver.findElement(By.xpath("/html/body/div[3]/div/div/div[3]/button[2]")).click();
        Thread.sleep(700);
        //locating and clicking "OK" button after successfull purchase
        driver.findElement(By.xpath("/html/body/div[10]/div[7]/div/button")).click();
        //locating logout button
        WebElement logOutButton=driver.findElement(By.id("navbarExample"));
        Thread.sleep(1000);
        //cliking logout button
        logOutButton.findElement(By.id("logout2")).click();
        Thread.sleep(5000);
        //finalizing and quitting browser
        driver.quit();



    }


}





