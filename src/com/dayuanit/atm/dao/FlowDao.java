package com.dayuanit.atm.dao;

import java.util.List;

import com.dayuanit.atm.domain.Flow;

public interface FlowDao {
	
	int addFlow(Flow flow);
	
	List<Flow> listFlow(String cardNum);

}
