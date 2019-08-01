package com.run.test;

import org.testng.annotations.Test;

import com.performutility.test.ActiveMQ;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;

public class PushingXMLDataToActiveMQ {

	ActiveMQ amq = new ActiveMQ();

	@Features("WOP")
    @Stories("WOP XML")
	@Test(priority = 1)
	@Title("Pushing the WOP XML's to ActiveMQ")
	public void performanceTestOnWOP() throws Exception, Exception {
		
		System.out.println("===========ActiveMQ WOP Started==============");
		amq.readxml("WOP_xml");
		System.out.println("===========ActiveMQ WOP Ended==============");
	}

	@Features("MP")
    @Stories("MP XML")
	@Test(priority = 2)
	@Title("Pushing the MP XML's to ActiveMQ")
	public void performanceTestOnMP() throws Exception, Exception {
		
		System.out.println("===========ActiveMQ MP Started==============");
		amq.readxml("MP_xml");
		System.out.println("===========ActiveMQ MP Ended==============");
	}

	@Features("MEDIATOR")
    @Stories("MEDIATOR XML")
	@Test(priority = 3)
	@Title("Pushing the MEDIATOR XML's to ActiveMQ")
	public void performanceTestOnMEDIATOR() throws Exception, Exception {
		
		System.out.println("===========ActiveMQ MEDIATOR Started==============");
		amq.readxml("MEDIATOR_xml");
		System.out.println("===========ActiveMQ MEDIATOR Ended==============");
	}

}
