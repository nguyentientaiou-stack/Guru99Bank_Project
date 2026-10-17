package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExcelHelper.class);
  private static XSSFWorkbook workbook;
  private static XSSFSheet sheet;
  private static XSSFCell cell;

  public static void setExcelFile(String excelFilePath, String sheetName) {
    try {
      LOGGER.info("Loading Excel File");
      workbook = new XSSFWorkbook(excelFilePath);
      sheet = workbook.getSheet(sheetName);
      LOGGER.info("Excel File loaded");
    } catch (Exception e) {
      LOGGER.error("Failed to set Excel file '{}'. Root cause: {}", excelFilePath, e.getMessage());
    }
  }

  public static XSSFWorkbook getWorkbook(String excelFilePath) {
    try {
      LOGGER.info("Getting Excel Workbook from Excel File {}", excelFilePath);
      XSSFWorkbook workbook1 = new XSSFWorkbook(excelFilePath);
      LOGGER.info("Excel Workbook Loaded");
      return workbook1;
    } catch (Exception e) {
      LOGGER.error("Failed to get Excel Workbook from Excel File {}. Root cause: {}", excelFilePath,
          e.getMessage());
    }
    return null;
  }

  public static XSSFSheet getSheet(String sheetName) {
    try {
      LOGGER.info("Getting Excel Sheet {}", sheetName);
      XSSFSheet sheet = workbook.getSheet(sheetName);
      LOGGER.info("Excel Sheet Loaded");
      return sheet;
    } catch (Exception e) {
      LOGGER.error("Failed to get Excel Sheet {}. Root cause: {}", sheetName, e.getMessage());
    }
    return null;
  }

  public static XSSFSheet getSheet(XSSFWorkbook workbook, String sheetName) {
    try {
      LOGGER.info("Getting Excel Sheet {}", sheetName);
      XSSFSheet sheet = workbook.getSheet(sheetName);
      LOGGER.info("Excel Sheet Loaded");
      return sheet;
    } catch (Exception e) {
      LOGGER.error("Failed to get Excel Sheet {}. Root cause: {}", sheetName, e.getMessage());
    }
    return null;
  }

  public static String getCellValue(XSSFSheet sheet, int rowIndex, int colIndex) {
    String cellValue = "";
    try {
      XSSFCell cell = sheet.getRow(rowIndex).getCell(colIndex);
      if (cell == null) {
        return "";
      }
      switch (cell.getCellType()) {
        case STRING:
          cellValue = cell.getStringCellValue();
          break;
        case NUMERIC:
          cellValue = String.valueOf(cell.getNumericCellValue());
          break;
        case BOOLEAN:
          cellValue = String.valueOf(cell.getBooleanCellValue());
          break;
        case FORMULA:
          cellValue = cell.getCellFormula();
          break;
        default:
          cellValue = "";
      }
    } catch (Exception e) {
      LOGGER.error("Failed to get Cell Value from Excel Sheet {}. Root cause: {}", sheet,
          e.getMessage());
    }
    return cellValue;
  }

  public static Cell findFirstCellWithStringValue(XSSFSheet sheet, String value) {
    for(Row row : sheet) {
      for(Cell cell : row) {
        if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals(value)) {
          return cell;
        }
      }
    }
    return null;
  }

  public static Cell findLastCellWithStringValue(XSSFSheet sheet, String value) {
    Cell lastCell = null;
    for(Row row : sheet) {
      for(Cell cell : row) {
        if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals(value)) {
          lastCell = cell;
        }
      }
    }
    return lastCell;
  }

  public static int getRowFromCell(Cell cell) {
    return cell.getRow().getRowNum();
  }

  public static int getColFromCell(Cell cell) {
    return cell.getColumnIndex();
  }

  public static XSSFRow getRowByIndex(XSSFSheet sheet, int rowIndex) {
    return sheet.getRow(rowIndex);
  }

  // [{firstName: John}, {lastName: Doe}]
  public static List<HashMap<String, String>> getExcelData(String excelFilePath, String sheetName, String testCaseId) {
    List<HashMap<String, String>> data = new ArrayList<>();
    XSSFWorkbook workbook = ExcelHelper.getWorkbook(excelFilePath);
    if (workbook == null) {
      LOGGER.error("Excel file {} not found", excelFilePath);
      return data;
    }
    XSSFSheet sheet = ExcelHelper.getSheet(workbook, sheetName);
    if (sheet == null) {
      LOGGER.error("Sheet {} not found", sheetName);
      return data;
    }

    Cell startCell = ExcelHelper.findFirstCellWithStringValue(sheet, testCaseId);
    if (startCell == null) {
      LOGGER.error("Test case id '{}' not found in sheet '{}'", sheetName, sheetName);
      return data;
    }

    Cell endCell = ExcelHelper.findLastCellWithStringValue(sheet, testCaseId);

    int startRow = ExcelHelper.getRowFromCell(startCell);
    int endRow = ExcelHelper.getRowFromCell(endCell);
    int startCol = ExcelHelper.getColFromCell(startCell);
    int endCol = ExcelHelper.getColFromCell(endCell);

    if (startRow != -1) {
      XSSFRow headerRow = ExcelHelper.getRowByIndex(sheet, startRow);
      if (headerRow == null) {
        LOGGER.error("Header row for test case '{}' is not found in sheet '{}'", testCaseId, sheetName);
        return data;
      }

      for (int i = startRow + 1; i <= endRow; i++) {
        HashMap<String, String> row = new HashMap<>();
        XSSFRow dataRow = ExcelHelper.getRowByIndex(sheet, i);
        if (dataRow != null) {
          for (int j = startCol + 1; j < endCol; j++) {
            String header = ExcelHelper.getCellValue(sheet, startRow, j);
            String cellValue = ExcelHelper.getCellValue(sheet, i, j);
            if (header != null && !header.isEmpty()) {
              row.put(header, cellValue);
            }
          }
        }
        data.add(row);
      }
    }
    try {
      workbook.close();
    } catch (Exception e) {
      LOGGER.error("Failed to close workbook. Root cause: {}", e.getMessage());
    }
    return data;
  }
}
