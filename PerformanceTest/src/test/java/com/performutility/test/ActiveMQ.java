package com.performutility.test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection; // ActiveMQ Server Connection
import org.apache.activemq.ActiveMQConnectionFactory; // Using ActiveMQConnectionFactory Library
import org.testng.Assert;


public class ActiveMQ extends TestBase {
	
	//====Class Level Variables===
	
	public static ArrayList<String> reqxml = new ArrayList<String>();
	TextMessage message;
	int filesize;
	public String path = System.getProperty("user.dir");
	ActiveMQConnection connection;

	// =========This method works for reading the XML's by it's folder name=======
	
	public void readxml(String foldername) throws Exception {

	// =====Reading the XML count from the XmlDataCountReading sheet=====
		
		readingExcel("XmlDataCountReading");
		
		int executioncount = 0;
		
		int count = 1;
		
		int excelrowcount=lastRow;
		
		for(int j=1;j<=excelrowcount;j++) {
			if(foldername.contains(excelData[j][0].toString())) {
				executioncount=Integer.parseInt(excelData[j][1].toString());
				break;
			}
		}

		// ====Read the file name from the folder===	
		
		String workDir = System.getProperty("user.dir");
		
		String Path = workDir +"\\Performance\\" +  foldername;	
		
		File folder = new File(Path);
		
		//======Take the list of files from the folder====
		File[] listOfFiles = folder.listFiles();

		//File[] listOfFiles = folder.
		String Filename = null;
		
			for (File file : listOfFiles) {
				
				if (file.isFile()) {
					
					//====Read the file name from the folder====
					Filename = file.getName();
					
					System.out.println("XML Name is : " + Filename);
					logStep("Pushed" + Filename + "on AciveMQ");
					System.out.println("===================Pushed XML count on ActiveMQ is :---" + count);
					logStep("Pushed XML count on ActiveMQ :" + count);
					
					@SuppressWarnings("resource")
					BufferedReader r = new BufferedReader(new FileReader(file));
					String line = "";
					
					//Read XML line by line and append data into reqxml StringBuffer 
					while ((line = r.readLine()) != null) {
						reqxml.add(line.toString());
					}
					
					//=====Giving the XML's as Input to ActiveMQ Method=====
					activeMQ(reqxml.toString());
				}
				if(executioncount==count) {
					break;
				}
				count = count + 1;
			}
	}

	//======This method works for push XML's to ActiveMQ server=====

	public void activeMQ(String xml) {
		
		String subject = "AMCN.ESB.ALL.ALL.ALL.XML.QUEUE";
		
		try {
			
			// ====Connecting to ActiveMQ server using ActiveMQConnection Factory======
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					"tcp://dev-adam02.amcnetworks.com:61616/");
			 connection = (ActiveMQConnection) connectionFactory.createConnection();

			//====Connection established for ActiveMQ====
			connection.start();
          
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(subject);

			// =====MessageProducer is used for sending messages to the queue=====
			MessageProducer producer = session.createProducer(destination);

			//====Parsing XML data into ActiveMQ body====
			TextMessage message = session.createTextMessage(xml);

			//======Data send through ActiveMQ====
			producer.send(message);

			System.out.println("JCG printing@@ '" + message.getText() + "'");
			System.out.println("==============================");

			// =======Closes the ActiveMQ connection=====
			connection.close();	

		} catch (Exception e) {
			
			logStep(e.getMessage());
			System.out.println("Active MQ Connection is Failed");
			Assert.assertTrue(connection.isStarted());								   
		}

	}

	

	

}
