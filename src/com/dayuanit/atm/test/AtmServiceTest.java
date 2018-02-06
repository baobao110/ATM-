package com.dayuanit.atm.test;

import com.dayuanit.atm.service.AtmService;
import com.dayuanit.atm.service.impl.AtmServiceImpl;

public class AtmServiceTest {
	
	private static final AtmService service = new AtmServiceImpl();
	
	public static void main(String[] args) {
//		testOpenAccount();
		
//		testDeposit();
		
//		testDraw();
		
		testTransfer();
		
//		testListFlow();
//		System.out.println(1.2+2.4);
	}
	
	public static void testOpenAccount() {
		for (int i = 0 ; i < 1000; i ++) {
			service.openAccount("111111");
			System.out.println(i);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void testDeposit() {
		service.deposit("55.000", "1000", "2222");
	}
	
	public static void testDraw() {
		service.draw("123.00", "1234", "111");
	}
	
	public static void testTransfer() {
		service.transfer("200.00", "5555", "1000", "2222");
	}
	
	public static void testListFlow() {
		service.queryFlow("1234", "111");
	}
	
}
