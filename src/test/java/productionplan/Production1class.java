package productionplan;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Production1class {

	static WebDriver driver;
	static JavascriptExecutor js;
	static WebDriverWait wait;

	public static void main(String[] args) throws InterruptedException {

		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://mavro.app/");

		js = (JavascriptExecutor) driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		driver.findElement(By.xpath("//input[@type=\"text\"]")).sendKeys("Test@mavro");
		driver.findElement(By.xpath("//input[@type=\"password\"]")).sendKeys("1234567890");
		driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();
		Thread.sleep(5000);

		driver.findElement(By.xpath("//input[@placeholder=\"Search menu...\"]")).sendKeys("Production");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[span='Production']")).click();
		driver.findElement(By.xpath("//*[span=\"Production Plan\"]")).click();
		Thread.sleep(2000);

		driver.findElement(By.xpath("//input[@placeholder=\"Plan name\"]")).sendKeys("Rootsma 100");
		driver.findElement(By.xpath("//div[@class=\"production-plan-container container-fluid\"]")).click();

		WebElement itemCode = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[1]"));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", itemCode);
		Thread.sleep(500);
		itemCode.click();
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys("FM00000005");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[contains(text(),'FM00000005')]")).click();

		driver.findElement(By.xpath("//input[@name=\"productionQuantity\"]")).sendKeys("1000");

		WebElement unit = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[2]"));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", unit);
		Thread.sleep(500);
		unit.click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//*[contains(text(),'PC')])[1]")).click();

		WebElement process1 = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[4]"));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", process1);
		Thread.sleep(500);
		process1.click();
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys("P15");
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//*[contains(text(),'P15')])[1]")).click();
		Thread.sleep(2000);

		WebElement sfgOutput = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[5]"));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", sfgOutput);
		Thread.sleep(500);
		sfgOutput.click();
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys("SM00000113");
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//*[contains(text(),'SM00000113')])[1]")).click();

		WebElement qty = driver.findElement(By.xpath("(//input[@placeholder=\"Qty\" and @type=\"number\"])[1]"));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", qty);
		Thread.sleep(500);
		qty.click();
		qty.clear();
		qty.sendKeys("500");

		WebElement sfgUnit = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[6]"));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", sfgUnit);
		sfgUnit.click();
		Thread.sleep(800);

		driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys("PC");
		Thread.sleep(1000);

		java.util.List<WebElement> pcOptions = driver.findElements(By.xpath("//*[normalize-space()='PC']"));
		js.executeScript("arguments[0].click();", pcOptions.get(pcOptions.size() - 1));
		Thread.sleep(500);

		closeDropdown();
		

		String[] itemCodes = { "RA00000059", "RA00000060", "RA00000061" };

		// ITEM 1
		WebElement searchItem1 = driver.findElement(By.xpath("//span[contains(text(),'Search item')]/parent::div"));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", searchItem1);
		js.executeScript("arguments[0].click();", searchItem1);
		Thread.sleep(800);
		driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys(itemCodes[0]);
		Thread.sleep(800);
		driver.findElement(By.xpath("(//*[contains(text(),'" + itemCodes[0] + "')])[1]")).click();
		closeDropdown();

		driver.findElement(By.xpath("(//button[contains(@class,'production-plan-btn-add')])[1]")).click();
		Thread.sleep(800);

		// ITEM 2
		WebElement searchItem2 = driver.findElement(By.xpath("//span[contains(text(),'Search item')]/parent::div"));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", searchItem2);
		js.executeScript("arguments[0].click();", searchItem2);
		Thread.sleep(800);
		driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys(itemCodes[1]);
		Thread.sleep(800);
		driver.findElement(By.xpath("(//*[contains(text(),'" + itemCodes[1] + "')])[1]")).click();
		closeDropdown();

		driver.findElement(By.xpath("(//button[contains(@class,'production-plan-btn-add')])[1]")).click();
		Thread.sleep(800);

		// ITEM 3
		WebElement searchItem3 = driver.findElement(By.xpath("//span[contains(text(),'Search item')]/parent::div"));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", searchItem3);
		js.executeScript("arguments[0].click();", searchItem3);
		Thread.sleep(800);
		driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys(itemCodes[2]);
		Thread.sleep(800);
		driver.findElement(By.xpath("(//*[contains(text(),'" + itemCodes[2] + "')])[1]")).click();
		closeDropdown();
		
		driver.findElement(By.xpath("//input[@placeholder=\"Duration\"]")).sendKeys("2");
		//Thread.sleep(3000);
//		driver.findElement(By.xpath("//input[@class=\"form-control\"]")).sendKeys("H");
//		closeDropdown();
//		driver.findElement(By.xpath("//input[@placeholder=\"Duration\"]")).sendKeys("2");

		WebElement durationUnit = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[10]"));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", durationUnit);
		Thread.sleep(500);
		js.executeScript("arguments[0].click();", durationUnit);
		Thread.sleep(800);

		driver.findElement(By.xpath("(//*[normalize-space()='H'])[1]")).click();
		closeDropdown();

		
		driver.findElement(By.xpath("(//input[@class=\"production-plan-control form-control\"])[5]")).sendKeys("2");
		//Complete Printing process
		
		Thread.sleep(3000);
		// Sahi XPath ke sath element ko find karein
		WebElement addProcessBtn = driver.findElement(By.xpath("//button[normalize-space()='+ Add Process']"));

		
		org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", addProcessBtn);
		Thread.sleep(1000);
		js.executeScript("arguments[0].click();", addProcessBtn);
		
		WebElement process2 = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[13]"));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", process2);
		Thread.sleep(500);
		process2.click();
		Thread.sleep(1000);

		// "P16" search karein
		driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys("P16");
		Thread.sleep(1000);

		// Exact "P16 - Cutting" option par click karein
		driver.findElement(By.xpath("//div[contains(text(),'P16 - Cutting')]")).click();
		Thread.sleep(2000);
		
		WebElement sfgOutput1 = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[14]"));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", sfgOutput1);
		Thread.sleep(500);
		sfgOutput1.click();
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys("SM00000114");
		Thread.sleep(1000);

		driver.findElement(By.xpath("//div[contains(text(),'SM00000114 - Semi Finished Leafelet 2')]")).click();
		Thread.sleep(1000);
		
		WebElement qty1 = driver.findElement(By.xpath("(//input[@placeholder=\"Qty\"])[3]"));
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", qty1);
		Thread.sleep(500);
		qty1.click();
		qty1.clear();
		qty1.sendKeys("1000");
		Thread.sleep(2000);
		
		// 1. Dropdown open karne ke liye "Search unit..." par click karein
		driver.findElement(By.xpath("//span[text()='Search unit...']")).click();
		Thread.sleep(1000);

		WebElement searchBox = driver.findElement(By.xpath("//input[@placeholder='Search...']"));
		searchBox.sendKeys("PC");
		Thread.sleep(1000);

		// Yeh line sirf dropdown list ke option par click karegi, input field par nahi
		driver.findElement(By.xpath("//div[contains(@style, 'background-color')]//div[text()='PC']")).click();
		Thread.sleep(2000);
		
		driver.findElement(By.xpath("//span[text()='Search item...']")).click();
		Thread.sleep(1000);

		// Search box mein text type karein
		WebElement searchBox1 = driver.findElement(By.xpath("(//input[@placeholder='Search...'])[last()]"));
		searchBox1.sendKeys("SM00000113 - Semi Finished Leaflet");
		Thread.sleep(1000);

		// List ke exact option par click karwayein taaki wo properly select ho kar close ho jaye
		driver.findElement(By.xpath("(//div[normalize-space()='SM00000113 - Semi Finished Leaflet'])[last()]")).click();
		Thread.sleep(1000);
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//input[@placeholder=\"Duration\"])[2]")).sendKeys("2");
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//input[@class=\"production-plan-control form-control\"])[8]")).sendKeys("2");
		Thread.sleep(1000);

		// Scroll karne ke liye JavaScript Executor
		WebElement saveBtn = driver.findElement(By.xpath("//button[text()=\"Save Production Plan\"]"));
		org.openqa.selenium.JavascriptExecutor js1 = (org.openqa.selenium.JavascriptExecutor) driver;
		js1.executeScript("arguments[0].scrollIntoView(true);", saveBtn);
		Thread.sleep(1000);

		// Aapka diya hua locator aur click action
		driver.findElement(By.xpath("//button[text()=\"Save Production Plan\"]")).click();
		
		
		System.out.println("All Script is working perfectly!");
		Thread.sleep(5000);
		// driver.quit();
	}

	private static void closeDropdown() {
		// TODO Auto-generated method stub
		
	}

}