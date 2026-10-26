package productionplan;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Npplan {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		driver.get("https://mavro.app/");

		driver.findElement(By.xpath("//input[@type=\"text\"]")).sendKeys("Test@mavro");
		driver.findElement(By.xpath("//input[@type=\"password\"]")).sendKeys("1234567890");
		driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();
		Thread.sleep(5000);
		
		//click on Menu bar, Production & Production Plan
		driver.findElement(By.xpath("//input[@placeholder=\"Search menu...\"]")).sendKeys("Production");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[span='Production']")).click();
		driver.findElement(By.xpath("//*[span=\"Production Plan\"]")).click();
		driver.findElement(By.xpath("//div[@class=\"production-plan-container container-fluid\"]")).click();
		
		//Name of the Plan
		driver.findElement(By.xpath("//input[@placeholder=\"Plan name\"]")).sendKeys("Rootsma 100");
		Thread.sleep(10000);
		
		//Item Code
		driver.findElement(By.xpath("//span[normalize-space()='Search and select item...']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@placeholder=\"Search...\"]")).sendKeys("SR00001001");
		driver.findElement(By.xpath("//div[text()=\"SR00001001 - Rootsma Hair Oil 100 ml\"]")).click();
		
		//Produciton Quantity
		driver.findElement(By.xpath("//input[@name='productionQuantity']")).sendKeys("1000");
		
		//Unit
		driver.findElement(By.xpath("//div[@class='production-plan-header-card']//div[4]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("PC");
		
		//Office Code
		driver.findElement(By.xpath("//div[@class='production-plan-header-card']//div[5]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("5002");
		driver.findElement(By.xpath("//div[contains(text(),'5002 - Siddhidhata Factory')]")).click();
		
		//Production Process
		driver.findElement(By.xpath("//div[@class='col-md-11']//div[@class='searchable-select-control']")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("Raw Material Processing ");
		driver.findElement(By.xpath("//div[contains(text(),'P02 - Raw Material Processing')]")).click();
		js.executeScript("window.scrollBy(0, 500);");
		Thread.sleep(2000);
		
		//SubProcess Item , Search SFG Item (SFG 1)
		driver.findElement(By.xpath("//body/div[@id='root']/div[@id='main']/div[@class='main-content expand production-plan-wrapper']/div[@class='production-plan-container container-fluid']/form[@class='production-plan-form']/div[@class='production-plan-process-card']/div[@class='mt-3 production-plan-subprocess-section row']/div[@class='col-md-12']/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("Jatamasi Wt-1");
		driver.findElement(By.xpath("//div[contains(text(),'SM00000030 - Jatamasi Wt-1')]")).click();
		
		//Quantity
		driver.findElement(By.xpath("//div[@class='col-md-12']//div[1]//div[2]//div[1]//div[2]//div[2]//input[1]")).sendKeys("1");
		
		//Unit
		driver.findElement(By.xpath("//div[@class='col-md-12']//div[1]//div[2]//div[1]//div[2]//div[3]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("KG");
		driver.findElement(By.xpath("//div[contains(text(),'KG')]")).click();
		js.executeScript("window.scrollBy(0, 400);");
		
		//Item for this SFG, Search Item
		driver.findElement(By.xpath("//div[@class='col-md-12']//div[1]//div[2]//div[1]//div[3]//div[2]//div[1]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("Jatamaasi");
		driver.findElement(By.xpath("//div[contains(text(),'RA00000001 - Jatamaasi (Ball Chad)')]")).click();
		js.executeScript("window.scrollBy(0, 300);");
		Thread.sleep(1000);
		
		//click on the +ADD SFG ITEMS
		driver.findElement(By.xpath("//body/div[@id='root']/div[@id='main']/div[@class='main-content expand production-plan-wrapper']/div[@class='production-plan-container container-fluid']/form[@class='production-plan-form']/div[@class='production-plan-process-card']/div[@class='mt-3 production-plan-subprocess-section row']/div[@class='col-md-12']/div[1]/div[2]/button[1]")).click();	
		Thread.sleep(2000);
		driver.findElement(By.xpath("//body/div[@id='root']/div[@id='main']/div[@class='main-content expand production-plan-wrapper']/div[@class='production-plan-container container-fluid']/form[@class='production-plan-form']/div[@class='production-plan-process-card']/div[@class='mt-3 production-plan-subprocess-section row']/div[@class='col-md-12']/div[@class='production-plan-subprocess-block']/div[@class='production-plan-form-group']/div[2]/div[2]/div[1]/div[1]/div[1]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("Gudahal Wt-1");
		driver.findElement(By.xpath("//div[contains(text(),'SM00000034 - Gudahal wt-1')]")).click();
		js.executeScript("window.scrollBy(0, 150);");
		Thread.sleep(2000);
		
		//Quantity in SFG 2
		driver.findElement(By.xpath("//div[@class='production-plan-form-group']//div[2]//div[2]//div[2]//input[1]")).sendKeys("0.5");
		
		//Unit in the SFG 2
		driver.findElement(By.xpath("//div[@class='production-plan-form-group']//div[2]//div[2]//div[3]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("KG");
		driver.findElement(By.xpath("//div[contains(text(),'KG')]")).click();
		
		//item for this SFG, Search Item
		driver.findElement(By.xpath("//div[@class='production-plan-form-group']//div[2]//div[3]//div[2]//div[1]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("GUDHAL");
		driver.findElement(By.xpath("//div[contains(text(),'RA00000024 - GUDHAL')]")).click();
		
		//Approx Duration (Sub-Process)
		driver.findElement(By.xpath("//div[@class='col-md-12']//div[1]//div[3]//div[1]//div[1]//div[1]//input[1]")).sendKeys("0.15");
		js.executeScript("window.scrollBy(0, 300);");
		Thread.sleep(3000);
		
		//Unit
		driver.findElement(By.xpath("//div[@class='col-md-12']//div[1]//div[3]//div[1]//div[2]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("H");
		driver.findElement(By.xpath("//div[normalize-space()='H']")).click();
		
		//Labour Subprocess, Lubour sub type 
		driver.findElement(By.xpath("//div[@class='col-md-12']//div[1]//div[4]//div[2]//div[1]//div[1]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("MAN");
		driver.findElement(By.xpath("//div[contains(text(),'MAN')]")).click();
		
		//Labour Duration
		driver.findElement(By.xpath("//div[@class='col-md-12']//div[1]//div[4]//div[2]//div[1]//div[2]//div[1]//input[1]")).sendKeys("0.15");
		driver.findElement(By.xpath("//div[@class='col-md-12']//div[1]//div[4]//div[2]//div[1]//div[3]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("H");
		driver.findElement(By.xpath("//div[normalize-space()='H']")).click();
		js.executeScript("window.scrollBy(0, 400);");
		Thread.sleep(2000);
		
		//Weighting 1 Script complete in which we add one line item to give locators for add loops
		
		//Sorting Start from here 
		driver.findElement(By.xpath("//body/div[@id='root']/div[@id='main']/div[@class='main-content expand production-plan-wrapper']/div[@class='production-plan-container container-fluid']/form[@class='production-plan-form']/div[@class='production-plan-process-card']/div[@class='mt-3 production-plan-subprocess-section row']/div[@class='col-md-12']/div[2]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("Jatamansi-SF");
		driver.findElement(By.xpath("//div[contains(text(),'SM00000008 - Jatamansi-SF')]")).click();
		
		//Quantity
		driver.findElement(By.xpath("//div[@class='production-plan-process-card']//div[2]//div[2]//div[1]//div[2]//div[2]//input[1]")).sendKeys("1");
		
		//Unit
		driver.findElement(By.xpath("//div[@class='production-plan-process-card']//div[2]//div[2]//div[1]//div[2]//div[3]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("KG");
		driver.findElement(By.xpath("//div[contains(text(),'KG')]")).click();
		
		//items for this SFG
		driver.findElement(By.xpath("//div[@class='production-plan-process-card']//div[2]//div[2]//div[1]//div[3]//div[2]//div[1]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("Jatamasi wt-1");
		driver.findElement(By.xpath("//div[contains(text(),'SM00000030 - Jatamasi Wt-1')]")).click();
		js.executeScript("window.scrollBy(0, 300);");
		Thread.sleep(2000);
		
		//+ADD SFG Items of Sorting
		driver.findElement(By.xpath("//body/div[@id='root']/div[@id='main']/div[@class='main-content expand production-plan-wrapper']/div[@class='production-plan-container container-fluid']/form[@class='production-plan-form']/div[@class='production-plan-process-card']/div[@class='mt-3 production-plan-subprocess-section row']/div[@class='col-md-12']/div[2]/div[2]/button[1]")).click();
		Thread.sleep(3000);
		
		driver.findElement(By.xpath("(//body/div[@id='root']/div[@id='main']/div[@class='main-content expand production-plan-wrapper']/div[@class='production-plan-container container-fluid']/form[@class='production-plan-form']/div[@class='production-plan-process-card']/div[@class='mt-3 production-plan-subprocess-section row']/div[@class='col-md-12']/div[@class='production-plan-subprocess-block']/div[@class='production-plan-form-group']/div[2]/div[2]/div[1]/div[1]/div[1])[2]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("Gudahal-SF");
		driver.findElement(By.xpath("//div[contains(text(),'SM00000009 - Gudahal-SF')]")).click();
		Thread.sleep(2000);
		
		//Quantity
		driver.findElement(By.xpath("(//div[@class='production-plan-form-group']//div[2]//div[2]//div[2]//input[1])[2]")).sendKeys("0.5");
		Thread.sleep(2000);
		//Search Unit
		driver.findElement(By.xpath("(//div[@class='production-plan-form-group']//div[2]//div[2]//div[3]//div[1]//div[1])[2]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("KG");
		driver.findElement(By.xpath("//div[contains(text(),'KG')]")).click();
		Thread.sleep(2000);
		js.executeScript("window.scrollBy(0, 200);");
		
		//Items for this sfg
		driver.findElement(By.xpath("(//div[@class='production-plan-form-group']//div[2]//div[3]//div[2]//div[1]//div[1]//div[1]//div[1])[2]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("Gudahal Wt-1");
		driver.findElement(By.xpath("//div[contains(text(),'SM00000034 - Gudahal wt-1')]")).click();
		js.executeScript("window.scrollBy(0, 200);");
		Thread.sleep(1000);
		
		//Approx Duration (Sub Process)
		driver.findElement(By.xpath("//div[@class='production-plan-process-card']//div[2]//div[3]//div[1]//div[1]//div[1]//input[1]")).sendKeys("0.5");
		Thread.sleep(1000);
		
		//Unit
		driver.findElement(By.xpath("//div[@class='production-plan-process-card']//div[2]//div[3]//div[1]//div[2]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("H");
		driver.findElement(By.xpath("//div[normalize-space()='H']")).click();
		js.executeScript("window.scrollBy(0, 200);");
		Thread.sleep(2000);
		
		//Labour Subprocess
		driver.findElement(By.xpath("//div[@class='production-plan-process-card']//div[2]//div[4]//div[2]//div[1]//div[1]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("MAN");
		driver.findElement(By.xpath("//div[contains(text(),'MAN')]")).click();
		Thread.sleep(1000);
		
		//Labour Duration
		driver.findElement(By.xpath("//div[@class='production-plan-process-card']//div[2]//div[4]//div[2]//div[1]//div[2]//div[1]//input[1]")).sendKeys("1");
		Thread.sleep(1000);
		
		//Duration Unit
		driver.findElement(By.xpath("//div[@class='production-plan-process-card']//div[2]//div[4]//div[2]//div[1]//div[3]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("H");
		driver.findElement(By.xpath("//div[normalize-space()='H']")).click();
		js.executeScript("window.scrollBy(0, 300);");
		Thread.sleep(2000);
		
		//Weighting 2 search SFG items
		driver.findElement(By.xpath("//body/div[@id='root']/div[@id='main']/div[@class='main-content expand production-plan-wrapper']/div[@class='production-plan-container container-fluid']/form[@class='production-plan-form']/div[@class='production-plan-process-card']/div[@class='mt-3 production-plan-subprocess-section row']/div[@class='col-md-12']/div[3]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("Jatamasi Wt-2");
		driver.findElement(By.xpath("//div[contains(text(),'SM00000031 - Jatamasi Wt-2')]")).click();
		
		//Qunatity
		driver.findElement(By.xpath("//div[@class='mt-3 production-plan-subprocess-section row']//div[3]//div[2]//div[1]//div[2]//div[2]//input[1]")).sendKeys("0.15");
		
		//Unit
		driver.findElement(By.xpath("//div[@class='mt-3 production-plan-subprocess-section row']//div[3]//div[2]//div[1]//div[2]//div[3]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("KG");
		driver.findElement(By.xpath("//div[contains(text(),'KG')]")).click();
		
		//items for this sfg
		driver.findElement(By.xpath("//div[@class='mt-3 production-plan-subprocess-section row']//div[3]//div[2]//div[1]//div[3]//div[2]//div[1]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("Jatamansi-SF");
		driver.findElement(By.xpath("//div[contains(text(),'SM00000008 - Jatamansi-SF')]")).click();
		js.executeScript("window.scrollBy(0, 300);");
		Thread.sleep(2000);
		
		//+ADD SFG Items
		driver.findElement(By.xpath("//body/div[@id='root']/div[@id='main']/div[@class='main-content expand production-plan-wrapper']/div[@class='production-plan-container container-fluid']/form[@class='production-plan-form']/div[@class='production-plan-process-card']/div[@class='mt-3 production-plan-subprocess-section row']/div[@class='col-md-12']/div[3]/div[2]/button[1]")).click();
		
		Thread.sleep(2000);
		
		//SFG 2 search SFG items
		driver.findElement(By.xpath("//body/div[@id='root']/div[@id='main']/div[@class='main-content expand production-plan-wrapper']/div[@class='production-plan-container container-fluid']/form[@class='production-plan-form']/div[@class='production-plan-process-card']/div[@class='mt-3 production-plan-subprocess-section row']/div[@class='col-md-12']/div[3]/div[2]/div[2]/div[2]/div[1]/div[1]/div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("Gudahal wt-2");
		driver.findElement(By.xpath("//div[contains(text(),'SM00000035 - Gudahal wt-2')]")).click();
		
		
		//Quantity
		driver.findElement(By.xpath("//div[@class='mt-3 production-plan-subprocess-section row']//div[3]//div[2]//div[2]//div[2]//div[2]//input[1]")).sendKeys("1");
		Thread.sleep(1000);
		
		//Unit
		driver.findElement(By.xpath("//div[@class='mt-3 production-plan-subprocess-section row']//div[3]//div[2]//div[2]//div[2]//div[3]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("KG");
		driver.findElement(By.xpath("//div[contains(text(),'KG')]")).click();

		//item for this sfg
		driver.findElement(By.xpath("//div[@class='mt-3 production-plan-subprocess-section row']//div[3]//div[2]//div[2]//div[3]//div[2]//div[1]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("Gudahal-SF");
		driver.findElement(By.xpath("//div[contains(text(),'SM00000009 - Gudahal-SF')]")).click();
		js.executeScript("window.scrollBy(0, 300);");
		
		//Approx Duration
		driver.findElement(By.xpath("//div[@class='mt-3 production-plan-subprocess-section row']//div[3]//div[3]//div[1]//div[1]//div[1]//input[1]")).sendKeys("0.15");
		js.executeScript("window.scrollBy(0, 230);");
		Thread.sleep(2000);
		
		//unit
		driver.findElement(By.xpath("//div[@class='mt-3 production-plan-subprocess-section row']//div[3]//div[3]//div[1]//div[2]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("H");
		driver.findElement(By.xpath("//div[normalize-space()='H']")).click();
		
		
		//Labour Type	
		driver.findElement(By.xpath("//div[@class='mt-3 production-plan-subprocess-section row']//div[3]//div[4]//div[2]//div[1]//div[1]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("MAN");
		driver.findElement(By.xpath("//div[contains(text(),'MAN')]")).click();
		js.executeScript("window.scrollBy(0, 200);");
		
		//Labour Duration
		driver.findElement(By.xpath("//div[@class='mt-3 production-plan-subprocess-section row']//div[3]//div[4]//div[2]//div[1]//div[2]//div[1]//input[1]")).sendKeys("0.15");
		
		//Unit
		driver.findElement(By.xpath("//div[@class='mt-3 production-plan-subprocess-section row']//div[3]//div[4]//div[2]//div[1]//div[3]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("H");
		driver.findElement(By.xpath("//div[normalize-space()='H']")).click();
		js.executeScript("window.scrollBy(0, 300);");
		Thread.sleep(2000);
		
		//Grinding
		driver.findElement(By.xpath("//body/div[@id='root']/div[@id='main']/div[@class='main-content expand production-plan-wrapper']/div[@class='production-plan-container container-fluid']/form[@class='production-plan-form']/div[@class='production-plan-process-card']/div[@class='mt-3 production-plan-subprocess-section row']/div[@class='col-md-12']/div[4]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("RM-01");
		driver.findElement(By.xpath("//div[contains(text(),'SM00000101 - RM-01')]")).click();
		
		//Quantity
		driver.findElement(By.xpath("//div[4]//div[2]//div[1]//div[2]//div[2]//input[1]")).sendKeys("100");
		
		//Unit
		driver.findElement(By.xpath("//div[4]//div[2]//div[1]//div[2]//div[3]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("KG");
		driver.findElement(By.xpath("//div[contains(text(),'KG')]")).click();
		Thread.sleep(1000);
		
		//Items for This SFG Items
		driver.findElement(By.xpath("//div[4]//div[2]//div[1]//div[3]//div[2]//div[1]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("Jatamansi-SF");
		driver.findElement(By.xpath("//div[contains(text(),'SM00000008 - Jatamansi-SF')]")).click();
		js.executeScript("window.scrollBy(0, 200);");
		
		//Approx Duration SubProcess
		driver.findElement(By.xpath("//div[4]//div[3]//div[1]//div[1]//div[1]//input[1]")).sendKeys("0.15");
		
		//Unit
		driver.findElement(By.xpath("//div[4]//div[3]//div[1]//div[2]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("H");
		driver.findElement(By.xpath("//div[normalize-space()='H']")).click();
		js.executeScript("window.scrollBy(0, 200);");
		Thread.sleep(2000);
		
		//Labour Subprocess
		driver.findElement(By.xpath("//div[4]//div[4]//div[2]//div[1]//div[1]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("MAN");
		driver.findElement(By.xpath("//div[normalize-space()='MAN']")).click();
		
		//Labour Duration
		driver.findElement(By.xpath("//div[4]//div[4]//div[2]//div[1]//div[2]//div[1]//input[1]")).sendKeys("0.15");
		
		//Duration Unit
		driver.findElement(By.xpath("//div[4]//div[4]//div[2]//div[1]//div[3]//div[1]//div[1]//div[1]")).click();
		driver.findElement(By.xpath("//input[@placeholder='Search...']")).sendKeys("H");
		driver.findElement(By.xpath("//div[normalize-space()='H']")).click();
		js.executeScript("window.scrollBy(0, 200);");
		Thread.sleep(2000);
		
		//Click on Save Production Plan
		driver.findElement(By.xpath("//button[normalize-space()='Save Production Plan']")).click();
		
		
		
		
		
		
		System.out.println("Script Working fine");
		
		
		
		
		
		
	}

}
