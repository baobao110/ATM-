package com.dayuanit.atm.test;

import com.dayuanit.atm.service.AtmService;
import com.dayuanit.atm.service.impl.AtmServiceImpl;
import com.dayuanit.atm.service.impl.AtmServiceImpl2;

public class AtmServiceTest2 {
	
	private static final AtmService service = new AtmServiceImpl();
//	private static final AtmService service = new AtmServiceImpl2();
	
	public static void main(String[] args) {
		
		for (int i = 0; i < 20; i ++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					testTransferUnable();
				}
			}, "unableTransfer").start();
		}
		
		for (int i = 0; i < 20; i ++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					testTransfer();
				}
			}, "enableTransfer").start();
		}
		
//		for (int i = 0; i < 3; i ++) {
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					testTransfer();
//				}
//			}, "enableTransfer").start();
//		}
		
//		testTransfer();
		
	}
	
	public static void testTransfer() {
		service.transfer("1.00", "2222", "1000", "2222");
	}
	
	public static void testTransferUnable() {
		Thread.yield();
		service.transfer("200.00", "33333", "1111", "2222");
	}
	
}
