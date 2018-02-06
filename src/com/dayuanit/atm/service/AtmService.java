package com.dayuanit.atm.service;

import java.util.List;

import com.dayuanit.atm.domain.Flow;

public interface AtmService {

	void openAccount(String password);
	
	void deposit(String amount, String cardNum, String password);
	
	void draw(String amount, String cardNum, String password);
	
	void transfer(String amount, String inCardNum, String outCardNum, String password);
	
	List<Flow> queryFlow(String cardNum, String password);
}
