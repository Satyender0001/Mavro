package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    WebDriver driver;
    WebDriverWait wait;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void loginToApp(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type=\"text\"]"))).sendKeys(username);
        driver.findElement(By.xpath("//input[@type=\"password\"]")).sendKeys(password);
        driver.findElement(By.xpath("//button[@class=\"login-button\"]")).click();
    }

    public void navigateToCreateJV() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder=\"Search menu...\"]"))).sendKeys("Journal Voucher");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//span[@class=\"flyout-menu-item-text\"]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()=\"Create JV\"]"))).click();
    }
}