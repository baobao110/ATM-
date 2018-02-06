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
 * ������ʵ��
 * ��Գ��� dayuanit.com
 * @Description:
 * @author ������
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
					System.out.println("��" + i + "����");
					continue;
				}
				
				cardNum = tempNum;
				
				break;
			}
			
			if (null == cardNum) {
				throw new BizException("�����ظ�������������");
			}
			
			bankCard.setCardNum(cardNum);
			bankCard.setPassword(password);
			
			int rows = cardDao.addCard(bankCard);
			if (1 != rows) {
				throw new BizException("����ʧ��");
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
				throw new BizException("���п��Ų����ڻ����벻��ȷ");
			}
			
			if (!bankCard.getPassword().equals(password)) {
				throw new BizException("���п��Ų����ڻ����벻��ȷ");
			}
			
			amount = CardUtils.checkAmountAndFormat(amount);
			System.out.println("format=" + amount);
			
			bankCard.setBalance(MoneyUtil.plus(bankCard.getBalance(), amount));
			
			int rows = cardDao.modifyBalance(bankCard.getCardNum(), bankCard.getBalance());
			if (1 != rows) {
				throw new BizException("���ʧ��");
			}
			
			Flow flow = new Flow();
			flow.setAmount(amount);
			flow.setCardNum(cardNum);
			flow.setDescript("��Ǯ");
			flow.setFlowType(1);
			
			rows = flowDao.addFlow(flow);
			if (1 != rows) {
				throw new BizException("�����ˮʧ��");
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
				throw new BizException("���п��Ų����ڻ����벻��ȷ");
			}
			
			if (!bankCard.getPassword().equals(password)) {
				throw new BizException("���п��Ų����ڻ����벻��ȷ");
			}
			
			amount = CardUtils.checkAmountAndFormat(amount);
			System.out.println("format=" + amount);
			
			String newBalance = MoneyUtil.sub(bankCard.getBalance(), amount);
			
			if (Double.parseDouble(newBalance) < 0) {
				throw new BizException("����");
			}
			
			bankCard.setBalance(newBalance);
			int rows = cardDao.modifyBalance(bankCard.getCardNum(), bankCard.getBalance());
			if (1 != rows) {
				throw new BizException("ȡ��ʧ��");
			}
			
			Flow flow = new Flow();
			flow.setAmount(amount);
			flow.setCardNum(cardNum);
			flow.setDescript("ȡǮ");
			flow.setFlowType(2);
			
			rows = flowDao.addFlow(flow);
			if (1 != rows) {
				throw new BizException("�����ˮʧ��");
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
			
			//��DAO��ʹ��
			ConnectionThreadLocal.setConnection(conn);
			
			BankCard outCard = cardDao.getBankCard4Lock(outCardNum);
			
			if (null == outCard) {
				throw new BizException("���п��Ų����ڻ����벻��ȷ");
			}
			
			if (!outCard.getPassword().equals(password)) {
				throw new BizException("���п��Ų����ڻ����벻��ȷ");
			}
			
			amount = CardUtils.checkAmountAndFormat(amount);
			System.out.println("format=" + amount);
			
			String newBalance = MoneyUtil.sub(outCard.getBalance(), amount);
			
			if (Double.parseDouble(newBalance) < 0) {
				throw new BizException("���㣬�޷�ת��");
			}
			
			outCard.setBalance(newBalance);
			int rows = cardDao.modifyBalance(outCard.getCardNum(), outCard.getBalance());
			if (1 != rows) {
				throw new BizException("ת��ʧ��");
			}
			
			Flow flow = new Flow();
			flow.setAmount(amount);
			flow.setCardNum(outCardNum);
			flow.setDescript("ת��֧��");
			flow.setFlowType(3);
			
			rows = flowDao.addFlow(flow);
			if (1 != rows) {
				throw new BizException("�����ˮʧ��");
			}
			
			//ת���˻�����
			BankCard inCard = cardDao.getBankCard4Lock(inCardNum);
			if (null == inCard) {
				throw new BizException("���п��Ų�����");
			}
			
			String inBalance =  MoneyUtil.plus(inCard.getBalance(), amount);
			
			inCard.setBalance(inBalance);
			rows = cardDao.modifyBalance(inCard.getCardNum(), inCard.getBalance());
			if (1 != rows) {
				throw new BizException("ת��ʧ��");
			}
			
			flow = new Flow();
			flow.setAmount(amount);
			flow.setCardNum(inCardNum);
			flow.setDescript("ת������");
			flow.setFlowType(4);
			
			rows = flowDao.addFlow(flow);
			if (1 != rows) {
				throw new BizException("�����ˮʧ��");
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
			throw new BizException("���п��Ų����ڻ����벻��ȷ");
		}
		
		if (!bankCard.getPassword().equals(password)) {
			throw new BizException("���п��Ų����ڻ����벻��ȷ");
		}
		
		List<Flow> list = flowDao.listFlow(cardNum);
		for (Flow flow : list) {
			System.out.println(flow.getCardNum() + "" + flow.getDescript());
		}
		
		return list;
	}

}
