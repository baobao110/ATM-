package com.dayuanit.atm.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.dayuanit.atm.dao.FlowDao;
import com.dayuanit.atm.domain.Flow;
import com.dayuanit.atm.util.ConnectionThreadLocal;

public class FlowDaoImpl implements FlowDao {

	@Override
	public int addFlow(Flow flow) {
		String sql = "INSERT into atm_flow(card_num, amount, flow_type, descript, create_time) value(?, ?, ?, ?, now());";
		
		try {
			PreparedStatement pst = ConnectionThreadLocal.getConnection().prepareStatement(sql);
			pst.setString(1, flow.getCardNum());
			pst.setString(2, flow.getAmount());
			pst.setInt(3, flow.getFlowType());
			pst.setString(4, flow.getDescript());
			
			return pst.executeUpdate();
			
		} catch(Exception e) {
			
		}
		
		return 0;
	}

	@Override
	public List<Flow> listFlow(String cardNum) {
		String sql = "SELECT id, card_num, amount, flow_type, descript from atm_flow where card_num=?;";
		List<Flow> list = new ArrayList<Flow>();
		
		try {
			PreparedStatement pst = ConnectionThreadLocal.getConnection().prepareStatement(sql);
			pst.setString(1, cardNum);
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				Flow flow = new Flow();
				flow.setAmount(rs.getString("amount"));
				flow.setCardNum(rs.getString("card_num"));
				flow.setDescript(rs.getString("descript"));
				flow.setFlowType(rs.getInt("flow_type"));
				flow.setId(rs.getInt("id"));
				
				list.add(flow);
			}
			
		} catch(Exception e) {
			
		}
		
		return list;
	}

}
