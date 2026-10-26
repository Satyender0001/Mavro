package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
    public static XSSFWorkbook workbook;
    public static XSSFSheet sheet;

    public static void loadExcel(String excelPath) throws IOException {
        File file = new File(excelPath);
        FileInputStream fis = new FileInputStream(file);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet("Sheet1");
    }

    public static int getColumnIndex(XSSFRow headerRow, String name, int fallback) {
        if (headerRow != null) {
            for (int c = 0; c < headerRow.getLastCellNum(); c++) {
                if (getCellValue(headerRow.getCell(c)).trim().equalsIgnoreCase(name)) return c;
            }
        }
        return fallback;
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

    public static String val(XSSFRow row, int col) {
        return getCellValue(row.getCell(col));
    }

    public static void closeExcel() throws IOException {
        if (workbook != null) workbook.close();
    }
}