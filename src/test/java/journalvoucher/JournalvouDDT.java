package journalvoucher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class JournalvouDDT {

    // every line item = one <tr> inside the table body
    static final String ROWS = "//table[contains(@class,'jv-entry-table')]//tbody/tr";
    static JavascriptExecutor js;

    public static void main(String[] args) throws IOException, InterruptedException {

        String excelPath = "C:\\Users\\Satyender STPL\\OneDrive - Saisatwik Technologies Private Limited\\Documents\\MyWorks\\Jounral Voucher AR.xlsx";
        File file = new File(excelPath);
        if (!file.exists()) {
            System.out.println("Error: Excel file not found at path: " + excelPath);
            return;
        }

        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheet("Sheet1");
        int totalRows = sheet.getLastRowNum();

        // ---------- Excel columns are found by HEADER NAME (fallback = old fixed number) ----------
        XSSFRow headerRow = sheet.getRow(0);
        int cPostingKey = col(headerRow, "Posting Key", 6);
        int cGlCode     = col(headerRow, "GL Code", 7);
        int cTransCode  = col(headerRow, "Transaction Code", 8);
        int cItemRef    = col(headerRow, "Item Ref", 9);
        int cAmount     = col(headerRow, "Amount", 10);
        int cResource   = col(headerRow, "Resource Id", 11);
        int cCustomer   = col(headerRow, "Customer", 12);
        int cVendor     = col(headerRow, "Vendor", 13);
        int cAsset      = col(headerRow, "Asset", 14);
        int cCurrency   = col(headerRow, "Currency", 15);
        
        System.out.println("Excel columns -> PostingKey=" + cPostingKey + ", GL=" + cGlCode + ", TransCode=" + cTransCode
                + ", ItemRef=" + cItemRef + ", Amount=" + cAmount + ", Resource=" + cResource
                + ", Customer=" + cCustomer + ", Vendor=" + cVendor + ", Asset=" + cAsset + ", Currency=" + cCurrency);

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        js = (JavascriptExecutor) driver;
        driver.manage().window().maximize();
        driver.get("https://mavro.app/");

        // Login
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type=\"text\"]"))).sendKeys("amradmin");
        driver.findElement(By.xpath("//input[@type=\"password\"]")).sendKeys("Welcome@123");
        driver.findElement(By.xpath("//button[@class=\"login-button\"]")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder=\"Search menu...\"]"))).sendKeys("Journal Voucher");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//span[@class=\"flyout-menu-item-text\"]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()=\"Create JV\"]"))).click();

        // ---------- Header (Excel row 2) ----------
        XSSFRow first = sheet.getRow(1);
        WebElement comcode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//select)[1]")));
        Thread.sleep(1000);
        try {
            pickCode(comcode, val(first, 0));                                                        // Company Code
            pickCode(driver.findElement(By.xpath("(//select)[2]")), val(first, 1));                  // FY Variant
            setDate(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@type=\"date\"])[1]"))), val(first, 2)); // Posting Date
            setDate(driver.findElement(By.xpath("(//input[@type=\"date\"])[2]")), val(first, 3));    // Document Date
            pickCode(driver.findElement(By.xpath("(//select)[3]")), val(first, 4));                  // Document Type
        } catch (IllegalStateException e) { 
            System.out.println("STOPPED in HEADER (Excel row 2): " + e.getMessage());
            System.out.println("Voucher NOT submitted. Fix that Excel value and run again.");
            workbook.close();
            fis.close();
            return;
        }
        driver.findElement(By.xpath("(//label[@class=\"material-master-field\"])[7]")).sendKeys(val(first, 5));

        // ---------- Accounting items: one Excel row = one table row ----------
        int line = 0; // number of line items filled so far
        for (int i = 1; i <= totalRows; i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null || val(row, cPostingKey).isEmpty()) continue; // skip blank Excel rows
            line++;
            try {

                if (line > 1) { // add a new empty row and wait until it exists
                    WebElement addItem = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[text()=\"+ Add Item\"]")));
                    js.executeScript("arguments[0].click();", addItem); 
                    wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath(ROWS), line - 1));
                }

                WebElement tr = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(" + ROWS + ")[" + line + "]")));
                js.executeScript("arguments[0].scrollIntoView({block:'center'});", tr);

                if (line == 1) printColumnNames(driver, tr);

                pick(tr.findElement(By.xpath("(.//select)[1]")), val(row, cPostingKey));   // Posting Key
                pickCode(tr.findElement(By.xpath("(.//select)[2]")), val(row, cGlCode));   // GL Code
                pick(tr.findElement(By.xpath("(.//select)[3]")), val(row, cTransCode));    // Transaction Code
                fill(tr.findElement(By.xpath("(.//input)[1]")), val(row, cItemRef));       // Item Ref
                fill(tr.findElement(By.xpath(".//input[@type='number']")), val(row, cAmount)); // Amount
                pickChecked(tr.findElement(By.xpath("(.//select)[4]")), "Resource", val(row, cResource), true); // Resource

                // Updated with correct fallback indices matching Excel columns (Customer=12, Vendor=13, Asset=14)
                pickCode(selectByHeader(driver, tr, "Customer", 12), val(row, cCustomer));  
                pickCode(selectByHeader(driver, tr, "Vendor", 13), val(row, cVendor));        
                pickCode(selectByHeader(driver, tr, "Asset", 14), val(row, cAsset));           
                pickCode(selectByHeader(driver, tr, "Currency", 15), val(row, cCurrency)); 

            } catch (IllegalStateException e) { 
                System.out.println("STOPPED at Excel row " + (i + 1) + " (line item " + line + "): " + e.getMessage());
                System.out.println("Voucher NOT submitted. Fix that Excel value and run again.");
                workbook.close();
                fis.close();
                return;
            }
        }

        // all lines are filled -> wait 5 sec, then click Submit JV
        Thread.sleep(5000);
        WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type=\"submit\"]")));
        js.executeScript("arguments[0].click();", submit);

        workbook.close();
        fis.close();
        System.out.println("Journal Voucher processed with " + line + " line item(s).");
    }

    // Prints only the COLUMN HEADING names of the line-item table (no values / no option lists)
    static void printColumnNames(WebDriver driver, WebElement tr) {
        java.util.List<WebElement> ths = driver.findElements(By.xpath("//table[contains(@class,'jv-entry-table')]//thead//th"));
        java.util.List<WebElement> tds = tr.findElements(By.xpath("./td"));
        System.out.println("========== COLUMNS FILLED (line item 1) ==========");
        for (int k = 0; k < tds.size() && k < ths.size(); k++) {
            java.util.List<WebElement> fields = tds.get(k).findElements(By.xpath(".//select | .//input"));
            if (fields.isEmpty()) continue;
            String name = ths.get(k).getAttribute("textContent").trim().replaceAll("\\s+", " ");
            System.out.println("  [" + name + "]");
        }
        System.out.println("==================================================");
    }

    static int col(XSSFRow headerRow, String name, int fallback) {
        if (headerRow != null) {
            for (int c = 0; c < headerRow.getLastCellNum(); c++) {
                if (getCellValue(headerRow.getCell(c)).trim().equalsIgnoreCase(name)) return c;
            }
        }
        return fallback;
    }

    static void pickChecked(WebElement el, String label, String wanted, boolean isCode) throws InterruptedException {
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

    static WebElement selectByHeader(WebDriver driver, WebElement tr, String header, int fallbackIndex) {
        WebElement select = null;
        try {
            java.util.List<WebElement> ths = driver.findElements(By.xpath("//table[contains(@class,'jv-entry-table')]//thead//th"));
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

    static void fill(WebElement input, String value) {
        input.clear();
        input.sendKeys(value);
    }

    static void pick(WebElement element, String text) {
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

    static void pickCode(WebElement element, String code) throws InterruptedException {
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

    static void setDate(WebElement input, String value) {
        if (value.matches("\\d{2}-\\d{2}-\\d{4}")) {
            value = value.substring(6) + "-" + value.substring(3, 5) + "-" + value.substring(0, 2);
        }
        js.executeScript(
            "Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype,'value').set.call(arguments[0],arguments[1]);" +
            "arguments[0].dispatchEvent(new Event('input',{bubbles:true}));" +
            "arguments[0].dispatchEvent(new Event('change',{bubbles:true}));", input, value);
    }

    static String val(XSSFRow row, int col) {
        return getCellValue(row.getCell(col));
    }

    public static String getCellValue(XSSFCell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
            }
            double v = cell.getNumericCellValue();
            return v == (long) v ? String.valueOf((long) v) : String.valueOf(v);
        }
        return cell.toString().trim();
    }
}