package utilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import commons.GlobalConstants;

public class ExcelConfig {

private Sheet currentSheet;
private String testDataExcelPath = GlobalConstants.getGlobalConstants().getDataTestPath()+ "timeDta.xlsx" ;
private Map<String, Integer> columns;

public static ExcelConfig getExcelData() {
    return new ExcelConfig();
}

public void switchToSheet(String name) {
    columns = new HashMap<String, Integer>();

    try (var workbooks = WorkbookFactory.create(new File(testDataExcelPath))) {
        currentSheet = workbooks.getSheet(name);
        currentSheet.getRow(0).forEach(cell -> {
            columns.put(cell.getStringCellValue(), cell.getColumnIndex());
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public String getCellData(String columnName, int row) {
    var dataRow = currentSheet.getRow(row);
    return getCellDataAsString(dataRow.getCell(columns.get(columnName)));
}

private String getCellDataAsString(Cell cell) {
    return switch (cell.getCellType()) {
        case STRING -> cell.getStringCellValue();
        case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
        case FORMULA -> cell.getStringCellValue();
        default -> "";
    };
}

}