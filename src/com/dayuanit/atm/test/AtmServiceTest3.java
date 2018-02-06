package com.dayuanit.atm.test;

import com.dayuanit.atm.service.AtmService;
import com.dayuanit.atm.service.impl.AtmServiceImpl;
import com.dayuanit.atm.service.impl.AtmServiceImpl2;

public class AtmServiceTest3 {
	
//	private static final AtmService service = new AtmServiceImpl();
	private static final AtmService service = new AtmServiceImpl2();
	
	public static void main(String[] args) {
		
		for (int i = 0; i < 20; i ++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					testdraw();
				}
			}, "testdraw").start();
		}
		
		for (int i = 0; i < 20; i ++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					testDeposit();
				}
			}, "testDeposit").start();
		}
		
	}
	
	public static void testDeposit() {
		service.deposit("1.00", "1000", "2222");
	}
	
	public static void testdraw() {
		service.draw("1.00", "1000", "2222");
	}
	
}
