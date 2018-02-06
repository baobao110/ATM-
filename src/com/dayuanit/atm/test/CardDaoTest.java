package com.dayuanit.atm.test;

import java.sql.Connection;

import com.dayuanit.atm.dao.CardDao;
import com.dayuanit.atm.dao.impl.CardDaoImpl;
import com.dayuanit.atm.db.DataBase;
import com.dayuanit.atm.domain.BankCard;

public class CardDaoTest {
	
	private static final CardDao cardDao = new CardDaoImpl();
	
	public static void main(String[] args) {
//		testAddCard();
		
//		testModifyBalance();
		
		testGetCard();
	}
	
	public static void testAddCard() {
		BankCard bankCard = new BankCard();
		bankCard.setBalance("200.00");
		bankCard.setCardNum("3333");
		bankCard.setPassword("2222");
		int rows = cardDao.addCard(bankCard);
		System.out.println(rows == 1);
	}
	
	public static void testModifyBalance() {
		BankCard bc = cardDao.getBankCard("1000");
		bc.setBalance("3000.00");
		
		bc.setCardNum("5555' or 1= 1 or card_num='");
		
		int rows = cardDao.modifyBalance(bc.getCardNum(), bc.getBalance());
		System.out.println(rows == 1);
	}
	
	public static void testGetCard() {
		BankCard bc = cardDao.getBankCard("3333");
		System.out.println(bc != null);
	}

}
