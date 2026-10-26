package pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class JournalVoucherPage {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;
    public static final String ROWS = "//table[contains(@class,'jv-entry-table')]//tbody/tr";

    public JournalVoucherPage(WebDriver driver, WebDriverWait wait, JavascriptExecutor js) {
        this.driver = driver;
        this.wait = wait;
        this.js = js;
    }

    public void fillHeader(String compCode, String fyVar, String postDate, String docDate, String docType, String headerText) throws InterruptedException {
        WebElement comcode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//select)[1]")));
        Thread.sleep(1000);
        
        pickCode(comcode, compCode);
        pickCode(driver.findElement(By.xpath("(//select)[2]")), fyVar);
        setDate(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@type=\"date\"])[1]"))), postDate);
        setDate(driver.findElement(By.xpath("(//input[@type=\"date\"])[2]")), docDate);
        pickCode(driver.findElement(By.xpath("(//select)[3]")), docType);
        
        driver.findElement(By.xpath("(//label[@class=\"material-master-field\"])[7]")).sendKeys(headerText);
    }

    public void addLineItem(int line) {
        if (line > 1) {
            WebElement addItem = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[text()=\"+ Add Item\"]")));
            js.executeScript("arguments[0].click();", addItem); 
            
            try {
                Thread.sleep(1500); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(ROWS), line));
        }
    }

    public WebElement getTableRow(int line) {
        WebElement tr = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(" + ROWS + ")[" + line + "]")));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", tr);
        return tr;
    }

    public void fillLineData(WebElement tr, String postKey, String glCode, String transCode, String itemRef, String amount, String resource, String customer, String vendor, String asset, String currency) throws InterruptedException {
        pick(tr.findElement(By.xpath("(.//select)[1]")), postKey);
        pickCode(tr.findElement(By.xpath("(.//select)[2]")), glCode);
        pick(tr.findElement(By.xpath("(.//select)[3]")), transCode);
        fill(tr.findElement(By.xpath("(.//input)[1]")), itemRef);
        fill(tr.findElement(By.xpath(".//input[@type='number']")), amount);
        pickChecked(tr.findElement(By.xpath("(.//select)[4]")), "Resource", resource, true);

        pickCode(selectByHeader(driver, tr, "Customer", 12), customer);  
        pickCode(selectByHeader(driver, tr, "Vendor", 13), vendor);        
        pickCode(selectByHeader(driver, tr, "Asset", 14), asset);           
        pickCode(selectByHeader(driver, tr, "Currency", 15), currency);
    }

    // Submit JV button click handler using the submit xpath from screenshot
    public void submitVoucher() throws InterruptedException {
        Thread.sleep(3000);
        WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type=\"submit\"]")));
        js.executeScript("arguments[0].click();", submit);
    }

    public void printColumnNames(WebElement tr) {
        List<WebElement> ths = driver.findElements(By.xpath("//table[contains(@class,'jv-entry-table')]//thead//th"));
        List<WebElement> tds = tr.findElements(By.xpath("./td"));
        System.out.println("========== COLUMNS FILLED (line item 1) ==========");
        for (int k = 0; k < tds.size() && k < ths.size(); k++) {
            List<WebElement> fields = tds.get(k).findElements(By.xpath(".//select | .//input"));
            if (fields.isEmpty()) continue;
            String name = ths.get(k).getAttribute("textContent").trim().replaceAll("\\s+", " ");
            System.out.println("  [" + name + "]");
        }
        System.out.println("==================================================");
    }

    public WebElement selectByHeader(WebDriver driver, WebElement tr, String header, int fallbackIndex) {
        WebElement select = null;
        try {
            List<WebElement> ths = driver.findElements(By.xpath("//table[contains(@class,'jv-entry-table')]//thead//th"));
            for (int k = 0; k < ths.size(); k++) {
                String t = ths.get(k).getAttribute("textContent").trim();
                if (t.toLowerCase().contains(header.toLowerCase())) {
                    select = tr.findElement(By.xpath("./td[" + (k + 1) + "]//select"));
                    break;
                }
            }
        } catch (org.openqa.selenium.NoSuchElementException e) {
            select = null;
        }
        if (select == null) {
            select = tr.findElement(By.xpath("(.//select)[" + fallbackIndex + "]"));
        }
        js.executeScript("arguments[0].scrollIntoView({block:'center',inline:'center'});", select);
        return select;
    }

    public void fill(WebElement input, String value) {
        input.clear();
        input.sendKeys(value);
    }

    public void pick(WebElement element, String text) {
        if (text.isEmpty()) return;
        Select select = new Select(element);
        for (WebElement option : select.getOptions()) {
            String optText = option.getText().trim();
            String optVal = option.getAttribute("value").trim();
            if (optText.equalsIgnoreCase(text) || optText.contains(text) || optVal.equalsIgnoreCase(text)) {
                option.click();
                return;
            }
        }
        throw new IllegalStateException("Dropdown option not found for: '" + text + "'");
    }

    public void pickCode(WebElement element, String code) throws InterruptedException {
        if (code.isEmpty()) return;
        long end = System.currentTimeMillis() + 10000;
        while (true) {
            Select select = new Select(element);
            WebElement exact = null, partial = null;
            for (WebElement o : select.getOptions()) {
                String t = o.getText().trim();
                String c = t.split("\\s+-\\s+")[0].trim();
                if (t.equalsIgnoreCase(code) || c.equalsIgnoreCase(code) || o.getAttribute("value").trim().equalsIgnoreCase(code)) { exact = o; break; }
                if (partial == null && t.toLowerCase().contains(code.toLowerCase())) partial = o;
            }
            WebElement hit = exact != null ? exact : partial;
            if (hit != null) { hit.click(); return; }
            if (System.currentTimeMillis() > end) {
                StringBuilder sb = new StringBuilder();
                for (WebElement o : select.getOptions()) sb.append("\n   ").append(o.getText().trim());
                throw new IllegalStateException("Code '" + code + "' not found in dropdown. Available options:" + sb);
            }
            Thread.sleep(500);
        }
    }

    public void pickChecked(WebElement el, String label, String wanted, boolean isCode) throws InterruptedException {
        if (wanted.isEmpty()) {
            System.out.println("   [" + label + "] Excel value is EMPTY -> skipped");
            return;
        }
        if (!el.isEnabled()) {
            System.out.println("   [" + label + "] dropdown is DISABLED in the app -> skipped");
            return;
        }
        if (isCode) pickCode(el, wanted); else pick(el, wanted);
    }

    public void setDate(WebElement input, String value) {
        if (value.matches("\\d{2}-\\d{2}-\\d{4}")) {
            value = value.substring(6) + "-" + value.substring(3, 5) + "-" + value.substring(0, 2);
        }
        js.executeScript(
            "Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype,'value').set.call(arguments[0],arguments[1]);" +
            "arguments[0].dispatchEvent(new Event('input',{bubbles:true}));" +
            "arguments[0].dispatchEvent(new Event('change',{bubbles:true}));", input, value);
    }
}