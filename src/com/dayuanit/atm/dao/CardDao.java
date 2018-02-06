package com.dayuanit.atm.dao;

import com.dayuanit.atm.domain.BankCard;

public interface CardDao {
	
	int addCard(BankCard bankCard);
	
	int modifyBalance(String cardNum, String balance);
	
	BankCard getBankCard(String cardNum);
	
	BankCard getBankCard4Lock(String cardNum);
	
	int modifyBalance(String cardNum, String balance, int version);

}
