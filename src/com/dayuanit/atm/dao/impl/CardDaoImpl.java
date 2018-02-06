package com.dayuanit.atm.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dayuanit.atm.dao.CardDao;
import com.dayuanit.atm.domain.BankCard;
import com.dayuanit.atm.util.ConnectionThreadLocal;

public class CardDaoImpl implements CardDao {

	@Override
	public int addCard(BankCard bankCard) {
		String sql = "INSERT into atm_back_card(card_num, balance, password,create_time, modify_time) value(?, ?, ?, now(), now());";
		try {
			PreparedStatement pst = ConnectionThreadLocal.getConnection().prepareStatement(sql);
			pst.setString(1, bankCard.getCardNum());
			pst.setString(2, bankCard.getBalance());
			pst.setString(3, bankCard.getPassword());
			return pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public int modifyBalance(String cardNum, String balance) {
		
		String sql = "UPDATE atm_back_card set balance=?, version = version + 1, modify_time=now() where card_num=?;";
		System.out.println(sql);
		
		try {
			
			PreparedStatement pst = ConnectionThreadLocal.getConnection().prepareStatement(sql);
			pst.setString(1, balance);
			pst.setString(2, cardNum);
			
			return pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public BankCard getBankCard(String cardNum) {
		String sql = "SELECT id,card_num, balance, password, version from atm_back_card where card_num=?;";
		
		try {
			
			PreparedStatement pst = ConnectionThreadLocal.getConnection().prepareStatement(sql);
			pst.setString(1, cardNum);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				BankCard bc = new BankCard();
				bc.setBalance(rs.getString("balance"));
				bc.setCardNum(rs.getString("card_num"));
				bc.setId(rs.getInt("id"));
				bc.setPassword(rs.getString("password"));
				bc.setVersion(rs.getInt("version"));
				return bc;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public BankCard getBankCard4Lock(String cardNum) {
		String sql = "SELECT id,card_num, balance, password, version from atm_back_card where card_num=? for update;";
		
		try {
			
			PreparedStatement pst = ConnectionThreadLocal.getConnection().prepareStatement(sql);
			pst.setString(1, cardNum);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				BankCard bc = new BankCard();
				bc.setBalance(rs.getString("balance"));
				bc.setCardNum(rs.getString("card_num"));
				bc.setId(rs.getInt("id"));
				bc.setPassword(rs.getString("password"));
				bc.setVersion(rs.getInt("version"));
				return bc;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public int modifyBalance(String cardNum, String balance, int version) {
		String sql = "UPDATE atm_back_card set balance=?, version = version + 1, modify_time=now() where card_num=? and version=?;";
		System.out.println(sql);
		
		try {
			
			PreparedStatement pst = ConnectionThreadLocal.getConnection().prepareStatement(sql);
			pst.setString(1, balance);
			pst.setString(2, cardNum);
			pst.setInt(3, version);
			
			return pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

}
