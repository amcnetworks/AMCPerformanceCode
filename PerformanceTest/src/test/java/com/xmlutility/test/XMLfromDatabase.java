package com.xmlutility.test;


import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



import ru.yandex.qatools.allure.annotations.Step;

public class XMLfromDatabase {

	public XSSFWorkbook workbook;
	public XSSFSheet sheet;
	public XSSFRow row = null;
	public XSSFCell cell = null;
	public String[][] excelData = null;
	public int colCount = 0;
	public int rowCount = 0;
	public int lastRow;
	public String[][] FDIDNumbers;
	public String path = System.getProperty("user.dir");
	public  String  databaseURL;
	public  String user;
	public  String passWord;
	public int linenumber;
	public String data;
	public static ArrayList<String> ar = new ArrayList<String>();
	public static ArrayList<String> recordon = new ArrayList<String>();
	public static ArrayList<String> armeta = new ArrayList<String>();
	public String FDID;
	public static String  folder;
	public static  Connection con=null;
	public static Statement stmt;

	// ============Method For Reading Excel File and data================
	public String[][] readingexcelFiles(String sheetname) throws Exception {

		try {

			String FilePath = path + "/ExcelFile/API_inputs.xlsx";

			FileInputStream finputStream = new FileInputStream(new File(FilePath));

			workbook = new XSSFWorkbook(finputStream);

			sheet = workbook.getSheet(sheetname);

			colCount = sheet.getRow(0).getPhysicalNumberOfCells();
			// System.out.println("Columns"+ colCount);

			rowCount = sheet.getPhysicalNumberOfRows();
			// System.out.println("Rows"+ rowCount);

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

			FDIDNumbers = readingexcelFiles(sheetName);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FDIDNumbers;
	}

	public void databasetest(String folder,int appNumber) throws Exception, SQLException, Exception {

		ConnectToDBAndStoreXML(folder, appNumber);

	}

	//appNumber -- Every application has a seprate number. 
	// WOP appNumber is 2
	// MP appNumber is 3
	// Mediator appNumber is 4 

	public void ConnectToDBAndStoreXML(String folder,int appNumber) throws Exception {


		readDatabaseCredentials(2);
		
		String[][] FDidList=readingExcel("FDIDNUMBERS");
		
		int lastRowexcel = lastRow;
		
		for (int i = 0; i <= lastRowexcel; i++) 
		{

			FDID = FDidList[i][0].toString();

			System.out.println(FDID);

			String finalQuery = BuildQuery(FDID, appNumber);
			
		
			ConnectToDatabaseAndStoreXML(folder, finalQuery, FDID);

			System.out.println("======================");

		}

	}

	public String BuildQuery(String FDID, int appNumber){

		String queryString = sqlQueries(appNumber);

		String[] querySplits = queryString.split("IDNUMBER");

		String finalQuery = querySplits[0].toString() + FDID + querySplits[1].toString();

		return finalQuery;

	}

	public void ConnectToDatabaseAndStoreXML(String folder, String queryString, String FDID) throws SQLException, ClassNotFoundException, Exception {

	
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");	
		
			
			String URL = databaseURL;			
			String Username = user;
			String Password = passWord;
			System.out.println("Driver Loaded");
			
	
			con = DriverManager.getConnection(URL, Username, Password);

			if (con != null) 
			{
				System.out.println("Connected to the Database...");
			}

			Statement stmt = con.createStatement();

			System.out.println("Connection successfull" + stmt.toString());
		}

		catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e1) {
			e1.getMessage();
		}

		// step3 create the statement object
		Statement stmt = con.createStatement();
		// step4 execute query
		ResultSet rs = null;

		rs = stmt.executeQuery(queryString);
		int i=1;
		while (rs.next()) {

			ResultSetMetaData metaData = rs.getMetaData();
			ar.clear();
			int count = metaData.getColumnCount(); // number of column
			ar.add(rs.getString(i));

			String xmldata =ar.get(0).toString();
			System.out.println("This is Data" + xmldata);
			writeToFile(xmldata,FDID,folder);	

			if(count==i) {
				break;
			}else {
				i=i+1;
			}

		}

		con.close();

	}


	public void writeToFile(String data, String FDID, String folder) {
		String fileName=FDID+"-"+folder;
		try {
			Files.write(Paths.get("C:\\Xmldata\\" + folder + "\\"+ fileName +".xml"), data.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ===========Method For Database Credentials Reading From the Sheet=========

	public void readDatabaseCredentials(int linenumber) {

		String[][] databaseCredentials = readingExcel("DatabaseCredentials");

		for (int inum = 1; inum <= lastRow; inum++) {

			if (linenumber == inum) {

				databaseURL = databaseCredentials[linenumber][0];
				user = databaseCredentials[linenumber][1];
				passWord = databaseCredentials[linenumber][2];

			}

		}
		

	}

	public String sqlQueries(int linenumber) {

		String[][] querieString =readingExcel("Queries");

		for (int num = 1; num <= lastRow; num++) {
			if (linenumber == num) {
				data = querieString[linenumber][0];

			}

		}
		return data;

	}

	@Step("{0}")
	public void logStep(String stepName) {

	}
}
