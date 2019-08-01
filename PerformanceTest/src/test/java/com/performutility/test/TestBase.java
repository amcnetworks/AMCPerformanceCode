
// Packages Used For Performance Test
package com.performutility.test;
import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.yandex.qatools.allure.annotations.Step;

public class TestBase {
	
	//====Class Level Variables===
		public XSSFWorkbook workbook;
		public XSSFSheet sheet;
		public XSSFRow row = null;
		public XSSFCell cell = null;
		public String[][] excelData = null;
		public int colCount = 0;
		public int rowCount = 0;
		public int lastRow;
		public String[][] testData;
		public String path = System.getProperty("user.dir");
		
	
	// ============Method For Reading Excel File and data================
	
		public String[][] readingexcelFiles(String sheetname) throws Exception {

			try {
				String FilePath = path + "/ExcelFile/API_inputs.xlsx";
				
				FileInputStream finputStream = new FileInputStream(new File(FilePath));
				
				workbook = new XSSFWorkbook(finputStream);
				
				sheet = workbook.getSheet(sheetname);
				
				colCount = sheet.getRow(0).getPhysicalNumberOfCells();
			
				rowCount = sheet.getPhysicalNumberOfRows();
			
				lastRow = sheet.getLastRowNum();
				
				excelData = new String[rowCount][colCount];
				
				for (int Nrow = 0; Nrow < rowCount; Nrow++) {
					
					row = sheet.getRow(Nrow);
					
					for (int Ncolumn = 0; Ncolumn < colCount; Ncolumn++) {
						
						cell = sheet.getRow(Nrow).getCell(Ncolumn);
						DataFormatter df = new DataFormatter();
						excelData[Nrow][Ncolumn] = df.formatCellValue(cell);
					}
				}
			} catch (Exception e) {
			}

			return excelData;
		}

		// ========Method For Reading Excel Sheet Based on SheetName=========
		
		public String[][] readingExcel(String sheetName) {
			
			logStep("Started reading data from Excel");
			try {
				
				testData = readingexcelFiles(sheetName);

			} catch (Exception e) {
				
				e.printStackTrace();
			}
			return testData;
		}

		
		// ====This method works for generating the allure reports====
		
		@Step("{0}")
		public void logStep(String stepName) {
		}
}
