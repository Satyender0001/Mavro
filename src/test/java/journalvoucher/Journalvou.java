package journalvoucher;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Journalvou {

	public static void main(String[] args) throws InterruptedException {

		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://mavro.app/");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		driver.findElement(By.xpath("//input[@type=\"text\"]")).sendKeys("amradmin");
		driver.findElement(By.xpath("//input[@type=\"password\"]")).sendKeys("Welcome@123");
		driver.findElement(By.xpath("//button[@class=\"login-button\"]")).click();	
		
		driver.findElement(By.xpath("//input[@placeholder=\"Search menu...\"]")).sendKeys("Journal Voucher");
		driver.findElement(By.xpath("//span[@class=\"flyout-menu-item-text\"]")).click();
		Thread.sleep(1000);
		
		WebElement createJvOption = driver.findElement(By.xpath("//span[text()=\"Create JV\"]"));
		createJvOption.click();
		Thread.sleep(1000);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// 1. Company Code Dropdown (9300)
		WebElement comcode = driver.findElement(By.xpath("(//select)[1]"));
		js.executeScript(
			"var sel = arguments[0]; var val = arguments[1];" +
			"for (var i = 0; i < sel.options.length; i++) {" +
			"    if (sel.options[i].text.includes(val) || sel.options[i].value.includes(val)) {" +
			"        sel.selectedIndex = i;" +
			"        sel.dispatchEvent(new Event('change', { bubbles: true }));" +
			"        break;" +
			"    }" +
			"}", comcode, "9300"
		);

		// 2. FY Dropdown (2026)
		WebElement fyvar = driver.findElement(By.xpath("(//select)[2]"));
		js.executeScript(
			"var sel = arguments[0]; var val = arguments[1];" +
			"for (var i = 0; i < sel.options.length; i++) {" +
			"    if (sel.options[i].text.includes(val) || sel.options[i].value.includes(val)) {" +
			"        sel.selectedIndex = i;" +
			"        sel.dispatchEvent(new Event('change', { bubbles: true }));" +
			"        break;" +
			"    }" +
			"}", fyvar, "2026"
		);

		// Posting Date
		WebElement postingDate = driver.findElement(By.xpath("(//input[@type=\"date\"])[1]"));
		js.executeScript(
		    "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
		    "nativeInputValueSetter.call(arguments[0], '2026-10-10');" +
		    "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
		    "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", 
		    postingDate
		);

		// Document Date
		WebElement documentDate = driver.findElement(By.xpath("(//input[@type=\"date\"])[2]"));
		js.executeScript(
		    "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
		    "nativeInputValueSetter.call(arguments[0], '2026-10-10');" +
		    "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
		    "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", 
		    documentDate
		);

		// 3. Document Type (07)
		WebElement doctype = driver.findElement(By.xpath("(//select)[3]"));
		js.executeScript(
			"var sel = arguments[0]; var val = arguments[1];" +
			"for (var i = 0; i < sel.options.length; i++) {" +
			"    if (sel.options[i].text.includes(val) || sel.options[i].value.includes(val)) {" +
			"        sel.selectedIndex = i;" +
			"        sel.dispatchEvent(new Event('change', { bubbles: true }));" +
			"        break;" +
			"    }" +
			"}", doctype, "07"
		);

		// Reference Number
		driver.findElement(By.xpath("(//label[@class=\"material-master-field\"])[7]")).sendKeys("37");

		// --- INITIAL DYNAMIC SCROLL TO ACCOUNTING ITEMS ---
		js.executeScript("window.scrollTo(0, document.body.scrollHeight / 3);");


		// --- FIRST ROW ---
		// 4. Postkey
		WebElement postkey = driver.findElement(By.xpath("(//select)[4]"));
		js.executeScript("arguments[0].value='M2'; arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", postkey);

		// 5. GLCODE
		WebElement glcode = driver.findElement(By.xpath("(//select)[5]"));
		js.executeScript("arguments[0].value='1000000100'; arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", glcode);

		// 6. Transaction Code
		WebElement trancode = driver.findElement(By.xpath("(//select)[6]"));
		js.executeScript("arguments[0].value='C1'; arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", trancode);

		// ITEM REF
		driver.findElement(By.xpath("(//table[contains(@class, 'jv-entry-table')]//input)[1]")).sendKeys("34534546");

		// AMOUNT
		driver.findElement(By.xpath("//input[@type=\"number\"]")).sendKeys("345");

		// Resource for Row 1
		try {
			WebElement resource = driver.findElement(By.xpath("(//select)[7]"));
			if (resource.isEnabled()) {
				js.executeScript(
					"var sel = arguments[0]; var val = arguments[1];" +
					"for (var i = 0; i < sel.options.length; i++) {" +
					"    if (sel.options[i].text.includes(val) || sel.options[i].value.includes(val)) {" +
					"        sel.selectedIndex = i;" +
					"        sel.dispatchEvent(new Event('change', { bubbles: true }));" +
					"        break;" +
					"    }" +
					"}", resource, "AMR1"
				);
			}
		} catch (Exception e) {
			System.out.println("Resource row 1 skipped.");
		}

		// Currency (Row 1)
		WebElement currency = driver.findElement(By.xpath("(//select)[11]"));
		js.executeScript("arguments[0].value='INR'; arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", currency);


		// --- ADD SECOND ITEM ROW TO BALANCE AMOUNT ---
		WebElement addItemBtn = driver.findElement(By.xpath("//button[text()=\"+ Add Item\"]"));
		addItemBtn.click();
		Thread.sleep(1000); 

		// --- DYNAMIC SCROLL DOWN TO THE NEWLY ADDED ROW AND TOTALS ---
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");


		// --- SECOND ROW FIELDS ---
		// Postkey (Row 2)
		WebElement postkey2 = driver.findElement(By.xpath("(//select)[12]"));
		js.executeScript("arguments[0].value='E1'; arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", postkey2);

		// GL Code (Row 2)
		WebElement glcode2 = driver.findElement(By.xpath("(//select)[13]"));
		js.executeScript("arguments[0].value='1000000204'; arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", glcode2);

		// Transaction Code (Row 2)
		WebElement trancode2 = driver.findElement(By.xpath("(//select)[14]"));
		js.executeScript("arguments[0].value='E1'; arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", trancode2);

		// Item Ref (Row 2)
		driver.findElement(By.xpath("(//table[contains(@class, 'jv-entry-table')]//input)[3]")).sendKeys("33");

		// Amount (Row 2)
		driver.findElement(By.xpath("(//input[@type=\"number\"])[2]")).sendKeys("345");

		// Resource for Row 2
		try {
			WebElement resource2 = driver.findElement(By.xpath("(//select)[15]"));
			if (resource2.isEnabled()) {
				js.executeScript(
					"var sel = arguments[0]; var val = arguments[1];" +
					"for (var i = 0; i < sel.options.length; i++) {" +
					"    if (sel.options[i].text.includes(val) || sel.options[i].value.includes(val)) {" +
					"        sel.selectedIndex = i;" +
					"        sel.dispatchEvent(new Event('change', { bubbles: true }));" +
					"        break;" +
					"    }" +
					"}", resource2, "AMR1236"
				);
			}
		} catch (Exception e) {
			System.out.println("Resource row 2 skipped.");
		}

		// Currency (Row 2)
		try {
			WebElement currency2 = driver.findElement(By.xpath("(//select)[19]"));
			js.executeScript("arguments[0].value='INR'; arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", currency2);
		} catch (Exception e) {
			System.out.println("Row 2 currency handled.");
		}

		// --- FINAL DYNAMIC SCROLL TO BOTTOM TO VIEW TOTALS & SUBMIT BUTTON ---
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

		// 5 Seconds Wait before clicking Submit
		Thread.sleep(5000);

		// Submit
		driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();

		System.out.println("Journal Voucher created Successfully");
	}
}