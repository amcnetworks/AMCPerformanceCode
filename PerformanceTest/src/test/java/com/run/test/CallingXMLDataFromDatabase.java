package com.run.test;

import org.testng.annotations.Test;

import com.xmlutility.test.XMLfromDatabase;

public class CallingXMLDataFromDatabase {
	
	XMLfromDatabase xml= new XMLfromDatabase();
	
	
	@Test(priority=1)
	public void xmlfromDatabaseWopTest() throws Exception
		{
		
				xml.ConnectToDBAndStoreXML("WOP", 2);
		}

	
	@Test(priority=2)
	public void xmlfromDatabaseMediaPulseTest() throws Exception
		{
				xml.ConnectToDBAndStoreXML("MP", 3);
		
		}
	@Test(priority=3)
	public void xmlfromDatabaseMediatorTest() throws Exception
		{
				xml.ConnectToDBAndStoreXML("MEDIATOR", 4);
		
		}
	
}
