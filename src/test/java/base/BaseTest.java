package base;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseTest {
    public static WebDriver driver;
    public static WebDriverWait wait;

    public static void initializeDriver() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().window().maximize();
        driver.get("https://mavro.app/");
    }

    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}