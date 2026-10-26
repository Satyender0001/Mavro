package tests;

import base.BaseTest;
import utils.ExcelUtils;
import pages.LoginPage;
import pages.JournalVoucherPage;

import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.openqa.selenium.JavascriptExecutor;

public class JournalVoucherTest extends BaseTest {

    public static void main(String[] args) {
        String excelPath = "C:\\Users\\Satyender STPL\\OneDrive - Saisatwik Technologies Private Limited\\Documents\\MyWorks\\Jounral Voucher AR.xlsx";

        try {
            ExcelUtils.loadExcel(excelPath);
            int totalRows = ExcelUtils.sheet.getLastRowNum();

            XSSFRow headerRow = ExcelUtils.sheet.getRow(0);
            int cPostingKey = ExcelUtils.getColumnIndex(headerRow, "Posting Key", 6);
            int cGlCode     = ExcelUtils.getColumnIndex(headerRow, "GL Code", 7);
            int cTransCode  = ExcelUtils.getColumnIndex(headerRow, "Transaction Code", 8);
            int cItemRef    = ExcelUtils.getColumnIndex(headerRow, "Item Ref", 9);
            int cAmount     = ExcelUtils.getColumnIndex(headerRow, "Amount", 10);
            int cResource   = ExcelUtils.getColumnIndex(headerRow, "Resource Id", 11);
            int cCustomer   = ExcelUtils.getColumnIndex(headerRow, "Customer", 12);
            int cVendor     = ExcelUtils.getColumnIndex(headerRow, "Vendor", 13);
            int cAsset      = ExcelUtils.getColumnIndex(headerRow, "Asset", 14);
            int cCurrency   = ExcelUtils.getColumnIndex(headerRow, "Currency", 15);

            initializeDriver();
            JavascriptExecutor js = (JavascriptExecutor) driver;

            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.loginToApp("amradmin", "Welcome@123");
            loginPage.navigateToCreateJV();

            JournalVoucherPage jvPage = new JournalVoucherPage(driver, wait, js);

            // Header details
            XSSFRow first = ExcelUtils.sheet.getRow(1);
            try {
                jvPage.fillHeader(
                    ExcelUtils.val(first, 0), 
                    ExcelUtils.val(first, 1), 
                    ExcelUtils.val(first, 2), 
                    ExcelUtils.val(first, 3), 
                    ExcelUtils.val(first, 4), 
                    ExcelUtils.val(first, 5)
                );
            } catch (IllegalStateException e) {
                System.out.println("STOPPED in HEADER: " + e.getMessage());
                ExcelUtils.closeExcel();
                tearDown();
                return;
            }

            // Line items loop
            int line = 0;
            for (int i = 1; i <= totalRows; i++) {
                XSSFRow row = ExcelUtils.sheet.getRow(i);
                if (row == null || ExcelUtils.val(row, cPostingKey).isEmpty()) continue;
                line++;
                try {
                    jvPage.addLineItem(line);
                    org.openqa.selenium.WebElement tr = jvPage.getTableRow(line);

                    if (line == 1) jvPage.printColumnNames(tr);

                    jvPage.fillLineData(
                        tr,
                        ExcelUtils.val(row, cPostingKey),
                        ExcelUtils.val(row, cGlCode),
                        ExcelUtils.val(row, cTransCode),
                        ExcelUtils.val(row, cItemRef),
                        ExcelUtils.val(row, cAmount),
                        ExcelUtils.val(row, cResource),
                        ExcelUtils.val(row, cCustomer),
                        ExcelUtils.val(row, cVendor),
                        ExcelUtils.val(row, cAsset),
                        ExcelUtils.val(row, cCurrency)
                    );
                } catch (IllegalStateException e) {
                    System.out.println("STOPPED at Excel row " + (i + 1) + " (line item " + line + "): " + e.getMessage());
                    ExcelUtils.closeExcel();
                    tearDown();
                    return;
                }
            }

            // Calls submitVoucher after all Excel rows & line items are successfully filled
            jvPage.submitVoucher();
            
            ExcelUtils.closeExcel();
            System.out.println("Journal Voucher processed with " + line + " line item(s) and submitted successfully!");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            tearDown();
        }
    }
}