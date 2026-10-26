package productionplan;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductionplanDDT {

	static WebDriver driver;
	static JavascriptExecutor js;
	static WebDriverWait wait;

	public static void main(String[] args) throws InterruptedException, IOException {

		// Updated Excel file path
		String excelPath = "C:\\Users\\Satyender STPL\\OneDrive - Saisatwik Technologies Private Limited\\Documents\\MyWorks\\Production_Plan_Data_Entry_new.xlsx";
		
		FileInputStream fis = new FileInputStream(excelPath);
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheet("Sheet1");
		int rowCount = sheet.getPhysicalNumberOfRows();

		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://mavro.app/");

		js = (JavascriptExecutor) driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Login
		driver.findElement(By.xpath("//input[@type=\"text\"]")).sendKeys("amradmin");
		driver.findElement(By.xpath("//input[@type=\"password\"]")).sendKeys("Welcome@123");
		driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();
		Thread.sleep(5000);

		// Navigation to Production Plan
		driver.findElement(By.xpath("//input[@placeholder=\"Search menu...\"]")).sendKeys("Production");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[span='Production']")).click();
		driver.findElement(By.xpath("//*[span=\"Production Plan\"]")).click();
		Thread.sleep(2000);

		// Loop through Excel rows (starting from row 1, as row 0 is header)
		for (int i = 1; i < rowCount; i++) {
			Row row = sheet.getRow(i);
			if (row == null) continue;

			String planName = getCellData(row, 0);
			String itemCodeVal = getCellData(row, 1);
			String itemQtyVal = getCellData(row, 2);
			String unitVal = getCellData(row, 3);
			String process1Val = getCellData(row, 4);
			String sfg1Val = getCellData(row, 5);
			String qty1Val = getCellData(row, 6);
			String sfgUnit1Val = getCellData(row, 7);
			String rm1 = getCellData(row, 8);
			String rm2 = getCellData(row, 9);
			String rm3 = getCellData(row, 10);
			String duration1Val = getCellData(row, 11);
			String durationUnit1Val = getCellData(row, 12);
			String controlVal5 = getCellData(row, 13);
			String process2Val = getCellData(row, 14);
			String sfg2Val = getCellData(row, 15);
			String qty2Val = getCellData(row, 16);
			String sfgUnit2Val = getCellData(row, 17);
			String item2Val = getCellData(row, 18);
			String duration2Val = getCellData(row, 19);
			String controlVal8 = getCellData(row, 20);

			driver.findElement(By.xpath("//input[@placeholder=\"Plan name\"]")).sendKeys(planName);
			driver.findElement(By.xpath("//div[@class=\"production-plan-container container-fluid\"]")).click();

			WebElement itemCode = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[1]"));
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", itemCode);
			Thread.sleep(500);
			itemCode.click();
			Thread.sleep(1000);

			driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys(itemCodeVal);
			Thread.sleep(1000);
			driver.findElement(By.xpath("//*[contains(text(),'" + itemCodeVal + "')]")).click();

			driver.findElement(By.xpath("//input[@name=\"productionQuantity\"]")).sendKeys(itemQtyVal);

			WebElement unit = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[2]"));
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", unit);
			Thread.sleep(500);
			unit.click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("(//*[contains(text(),'" + unitVal + "')])[1]")).click();

			WebElement process1 = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[4]"));
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", process1);
			Thread.sleep(500);
			process1.click();
			Thread.sleep(1000);

			driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys(process1Val);
			Thread.sleep(1000);
			driver.findElement(By.xpath("(//*[contains(text(),'" + process1Val + "')])[1]")).click();
			Thread.sleep(2000);

			WebElement sfgOutput = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[5]"));
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", sfgOutput);
			Thread.sleep(500);
			sfgOutput.click();
			Thread.sleep(1000);

			driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys(sfg1Val);
			Thread.sleep(1000);
			driver.findElement(By.xpath("(//*[contains(text(),'" + sfg1Val + "')])[1]")).click();

			WebElement qty = driver.findElement(By.xpath("(//input[@placeholder=\"Qty\" and @type=\"number\"])[1]"));
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", qty);
			Thread.sleep(500);
			qty.click();
			qty.clear();
			qty.sendKeys(qty1Val);

			WebElement sfgUnit = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[6]"));
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", sfgUnit);
			sfgUnit.click();
			Thread.sleep(800);

			driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys(sfgUnit1Val);
			Thread.sleep(1000);

			java.util.List<WebElement> pcOptions = driver.findElements(By.xpath("//*[normalize-space()='" + sfgUnit1Val + "']"));
			js.executeScript("arguments[0].click();", pcOptions.get(pcOptions.size() - 1));
			Thread.sleep(500);

			closeDropdown();

			String[] itemCodes = { rm1, rm2, rm3 };

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
			
			driver.findElement(By.xpath("//input[@placeholder=\"Duration\"]")).sendKeys(duration1Val);

			WebElement durationUnit = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[10]"));
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", durationUnit);
			Thread.sleep(500);
			js.executeScript("arguments[0].click();", durationUnit);
			Thread.sleep(800);

			driver.findElement(By.xpath("(//*[normalize-space()='" + durationUnit1Val + "'])[1]")).click();
			closeDropdown();

			driver.findElement(By.xpath("(//input[@class=\"production-plan-control form-control\"])[5]")).sendKeys(controlVal5);
			
			Thread.sleep(3000);
			WebElement addProcessBtn = driver.findElement(By.xpath("//button[normalize-space()='+ Add Process']"));
			js.executeScript("arguments[0].scrollIntoView(true);", addProcessBtn);
			Thread.sleep(1000);
			js.executeScript("arguments[0].click();", addProcessBtn);
			
			WebElement process2 = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[13]"));
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", process2);
			Thread.sleep(500);
			process2.click();
			Thread.sleep(1000);

			driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys(process2Val);
			Thread.sleep(1000);
			driver.findElement(By.xpath("//div[contains(text(),'" + process2Val + "')]")).click();
			Thread.sleep(2000);
			
			WebElement sfgOutput1 = driver.findElement(By.xpath("(//div[@class=\"searchable-select-control\"])[14]"));
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", sfgOutput1);
			Thread.sleep(500);
			sfgOutput1.click();
			Thread.sleep(1000);

			driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys(sfg2Val);
			Thread.sleep(1000);
			driver.findElement(By.xpath("//div[contains(text(),'" + sfg2Val + "')]")).click();
			Thread.sleep(1000);
			
			WebElement qty1 = driver.findElement(By.xpath("(//input[@placeholder=\"Qty\"])[3]"));
			js.executeScript("arguments[0].scrollIntoView({block:'center'});", qty1);
			Thread.sleep(500);
			qty1.click();
			qty1.clear();
			qty1.sendKeys(qty2Val);
			Thread.sleep(2000);
			
			driver.findElement(By.xpath("//span[text()='Search unit...']")).click();
			Thread.sleep(1000);

			WebElement searchBox = driver.findElement(By.xpath("//input[@placeholder='Search...']"));
			searchBox.sendKeys(sfgUnit2Val);
			Thread.sleep(1000);

			driver.findElement(By.xpath("//div[contains(@style, 'background-color')]//div[text()='" + sfgUnit2Val + "']")).click();
			Thread.sleep(2000);
			
			driver.findElement(By.xpath("//span[text()='Search item...']")).click();
			Thread.sleep(1000);

			WebElement searchBox1 = driver.findElement(By.xpath("(//input[@placeholder='Search...'])[last()]"));
			searchBox1.sendKeys(item2Val);
			Thread.sleep(1000);

			driver.findElement(By.xpath("(//div[normalize-space()='" + item2Val + "'])[last()]")).click();
			Thread.sleep(1000);
			Thread.sleep(1000);
			driver.findElement(By.xpath("(//input[@placeholder=\"Duration\"])[2]")).sendKeys(duration2Val);
			Thread.sleep(2000);
			driver.findElement(By.xpath("(//input[@class=\"production-plan-control form-control\"])[8]")).sendKeys(controlVal8);
			Thread.sleep(1000);

			WebElement saveBtn = driver.findElement(By.xpath("//button[text()=\"Save Production Plan\"]"));
			js.executeScript("arguments[0].scrollIntoView(true);", saveBtn);
			Thread.sleep(1000);
			driver.findElement(By.xpath("//button[text()=\"Save Production Plan\"]")).click();
		}

		workbook.close();
		fis.close();
		
		System.out.println("All Script is working perfectly with Excel Data!");
		Thread.sleep(8000);
		driver.quit();
	}

	private static String getCellData(Row row, int colIndex) {
		Cell cell = row.getCell(colIndex);
		if (cell == null) {
			return "";
		}
		return cell.toString().trim();
	}

	private static void closeDropdown() {
		// Helper method placeholder
	}
}