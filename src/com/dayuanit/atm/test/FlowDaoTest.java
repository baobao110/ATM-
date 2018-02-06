package com.dayuanit.atm.test;

import java.util.List;

import com.dayuanit.atm.dao.FlowDao;
import com.dayuanit.atm.dao.impl.FlowDaoImpl;
import com.dayuanit.atm.domain.Flow;

public class FlowDaoTest {
	
	private static final FlowDao flowDao = new FlowDaoImpl();
	
	public static void main(String[] args) {
//		testAddFlow();
		
		testList();
	}
	
	public static void testAddFlow() {
		Flow flow = new Flow();
		flow.setAmount("300");
		flow.setCardNum("1234");
		flow.setDescript(" ’»Î");
		flow.setFlowType(1);
		int rows = flowDao.addFlow(flow);
		System.out.println(1 == rows);
	}
	
	public static void testList() {
		List<Flow> list = flowDao.listFlow("1234");
		System.out.println(list.size());
	}

}
