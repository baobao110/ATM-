package com.dayuanit.atm.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.dayuanit.atm.dao.CardDao;
import com.dayuanit.atm.dao.FlowDao;
import com.dayuanit.atm.dao.impl.CardDaoImpl;
import com.dayuanit.atm.dao.impl.FlowDaoImpl;
import com.dayuanit.atm.db.DataBase;
import com.dayuanit.atm.domain.BankCard;
import com.dayuanit.atm.domain.Flow;
import com.dayuanit.atm.exception.BizException;
import com.dayuanit.atm.service.AtmService;
import com.dayuanit.atm.util.CardUtils;
import com.dayuanit.atm.util.ConnectionThreadLocal;
import com.dayuanit.atm.util.MoneyUtil;

/**
 * 悲观锁实现
 * 大猿软件 dayuanit.com
 * @Description:
 * @author 王夫子
 */
public class AtmServiceImpl implements AtmService {
	
	private static final CardDao cardDao = new CardDaoImpl();
	
	private static final FlowDao flowDao = new FlowDaoImpl();
	
	@Override
	public void openAccount(String password) {
		BankCard bankCard = new BankCard();
		bankCard.setBalance("0.00");
		
		ConnectionThreadLocal.setConnection(DataBase.getConnection());
		
		try {
			String cardNum = null;
			for (int i = 0; i < 3; i ++) {
				String tempNum = CardUtils.createCardNum();
				BankCard existBc = cardDao.getBankCard(tempNum);
				if (null != existBc) {
					System.out.println("第" + i + "机会");
					continue;
				}
				
				cardNum = tempNum;
				
				break;
			}
			
			if (null == cardNum) {
				throw new BizException("卡号重复，请重新输入");
			}
			
			bankCard.setCardNum(cardNum);
			bankCard.setPassword(password);
			
			int rows = cardDao.addCard(bankCard);
			if (1 != rows) {
				throw new BizException("开户失败");
			}
		} finally {
			ConnectionThreadLocal.removeConnection();
		}
		
	}

	@Override
	public void deposit(String amount, String cardNum, String password) {
		
		Connection conn = DataBase.getConnection();
		try {
			
			if (null == conn) {
				System.out.println("========conn is null=========");
				return;
			}
			
			conn.setAutoCommit(false);
			
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		ConnectionThreadLocal.setConnection(conn);
		
		try {
			BankCard bankCard = cardDao.getBankCard4Lock(cardNum);
			if (null == bankCard) {
				throw new BizException("银行卡号不存在或密码不正确");
			}
			
			if (!bankCard.getPassword().equals(password)) {
				throw new BizException("银行卡号不存在或密码不正确");
			}
			
			amount = CardUtils.checkAmountAndFormat(amount);
			System.out.println("format=" + amount);
			
			bankCard.setBalance(MoneyUtil.plus(bankCard.getBalance(), amount));
			
			int rows = cardDao.modifyBalance(bankCard.getCardNum(), bankCard.getBalance());
			if (1 != rows) {
				throw new BizException("存款失败");
			}
			
			Flow flow = new Flow();
			flow.setAmount(amount);
			flow.setCardNum(cardNum);
			flow.setDescript("存钱");
			flow.setFlowType(1);
			
			rows = flowDao.addFlow(flow);
			if (1 != rows) {
				throw new BizException("添加流水失败");
			}
			
			conn.commit();
			
		} catch(Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			ConnectionThreadLocal.removeConnection();
		}
		
	}

	@Override
	public void draw(String amount, String cardNum, String password) {
		
		Connection conn = DataBase.getConnection();
		try {
			if (null == conn) {
				System.out.println("========conn is null=========");
				return;
			}
			
			conn.setAutoCommit(false);
			
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		ConnectionThreadLocal.setConnection(conn);
		
		try {
			BankCard bankCard = cardDao.getBankCard4Lock(cardNum);
			if (null == bankCard) {
				throw new BizException("银行卡号不存在或密码不正确");
			}
			
			if (!bankCard.getPassword().equals(password)) {
				throw new BizException("银行卡号不存在或密码不正确");
			}
			
			amount = CardUtils.checkAmountAndFormat(amount);
			System.out.println("format=" + amount);
			
			String newBalance = MoneyUtil.sub(bankCard.getBalance(), amount);
			
			if (Double.parseDouble(newBalance) < 0) {
				throw new BizException("余额不足");
			}
			
			bankCard.setBalance(newBalance);
			int rows = cardDao.modifyBalance(bankCard.getCardNum(), bankCard.getBalance());
			if (1 != rows) {
				throw new BizException("取款失败");
			}
			
			Flow flow = new Flow();
			flow.setAmount(amount);
			flow.setCardNum(cardNum);
			flow.setDescript("取钱");
			flow.setFlowType(2);
			
			rows = flowDao.addFlow(flow);
			if (1 != rows) {
				throw new BizException("添加流水失败");
			}
			
			conn.commit();
			
		} catch(Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			ConnectionThreadLocal.removeConnection();
		}
		
	}

	@Override
	public void transfer(String amount, String inCardNum, String outCardNum, String password) {
		
		Connection conn = DataBase.getConnection();
		try {
			if (null == conn) {
				System.out.println("=========conn is null==============");
				return;
			}
			
			conn.setAutoCommit(false);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		try {
			
			//给DAO层使用
			ConnectionThreadLocal.setConnection(conn);
			
			BankCard outCard = cardDao.getBankCard4Lock(outCardNum);
			
			if (null == outCard) {
				throw new BizException("银行卡号不存在或密码不正确");
			}
			
			if (!outCard.getPassword().equals(password)) {
				throw new BizException("银行卡号不存在或密码不正确");
			}
			
			amount = CardUtils.checkAmountAndFormat(amount);
			System.out.println("format=" + amount);
			
			String newBalance = MoneyUtil.sub(outCard.getBalance(), amount);
			
			if (Double.parseDouble(newBalance) < 0) {
				throw new BizException("余额不足，无法转账");
			}
			
			outCard.setBalance(newBalance);
			int rows = cardDao.modifyBalance(outCard.getCardNum(), outCard.getBalance());
			if (1 != rows) {
				throw new BizException("转账失败");
			}
			
			Flow flow = new Flow();
			flow.setAmount(amount);
			flow.setCardNum(outCardNum);
			flow.setDescript("转账支出");
			flow.setFlowType(3);
			
			rows = flowDao.addFlow(flow);
			if (1 != rows) {
				throw new BizException("添加流水失败");
			}
			
			//转入账户操作
			BankCard inCard = cardDao.getBankCard4Lock(inCardNum);
			if (null == inCard) {
				throw new BizException("银行卡号不存在");
			}
			
			String inBalance =  MoneyUtil.plus(inCard.getBalance(), amount);
			
			inCard.setBalance(inBalance);
			rows = cardDao.modifyBalance(inCard.getCardNum(), inCard.getBalance());
			if (1 != rows) {
				throw new BizException("转账失败");
			}
			
			flow = new Flow();
			flow.setAmount(amount);
			flow.setCardNum(inCardNum);
			flow.setDescript("转账收入");
			flow.setFlowType(4);
			
			rows = flowDao.addFlow(flow);
			if (1 != rows) {
				throw new BizException("添加流水失败");
			}
			
			conn.commit();
			
		} catch(Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			ConnectionThreadLocal.removeConnection();
		}
		
	}

	@Override
	public List<Flow> queryFlow(String cardNum, String password) {
		BankCard bankCard = cardDao.getBankCard(cardNum);
		if (null == bankCard) {
			throw new BizException("银行卡号不存在或密码不正确");
		}
		
		if (!bankCard.getPassword().equals(password)) {
			throw new BizException("银行卡号不存在或密码不正确");
		}
		
		List<Flow> list = flowDao.listFlow(cardNum);
		for (Flow flow : list) {
			System.out.println(flow.getCardNum() + "" + flow.getDescript());
		}
		
		return list;
	}

}
