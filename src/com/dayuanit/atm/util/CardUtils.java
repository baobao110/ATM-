package com.dayuanit.atm.util;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Pattern;

import com.dayuanit.atm.exception.BizException;

public class CardUtils {
	
	public static String createCardNum() {
		
		Random random = new Random();
		
		StringBuilder sx = new StringBuilder();
		
		for (int i = 0; i < 4; i ++) {
			int num = random.nextInt(10);
			sx.append(num);
		}
		
		return sx.toString();
	}
	
	public static String checkAmountAndFormat(String amount) {
		if (null == amount || "".equals(amount)) {
			throw new BizException("请输入金额");
		}
		
		if (amount.length() > 10) {
			throw new BizException("金额不合法");
		}
		
		if (-1 != amount.indexOf(".")) {
			if (amount.substring(amount.indexOf(".") + 1, amount.length()).length() > 2) {
				throw new BizException("金额精确小数后两位");
			}
		}
		
		if (Double.parseDouble(amount) <= 0) {
			throw new BizException("金额必须大于零");
		}
		
		DecimalFormat df = new DecimalFormat("#0.00");
		amount = df.format(Double.parseDouble(amount));
		
		return amount;
	}
	
	public static void main(String[] args) {
//		System.out.println(CardUtils.createCardNum());
//		String num1 = "0.";
//		String num2 = "2.10";
//		
//		DecimalFormat df = new DecimalFormat("#0.00");
//		num1 = df.format(Double.parseDouble(num1));
//		System.out.println(num1);
//		
//		System.out.println(Double.parseDouble(num1) + Double.parseDouble(num2));
		
//		String amount = "2.";
//		if (-1 != amount.indexOf(".")) {
//			String[] amountArray = amount.split("\\.");
//			if (amountArray.length < 2) {
//				throw new BizException("金额精确小数后两位");
//			}
//			
//			String scal = amount.split("\\.")[1];
//			if (scal.length() > 2) {
//				throw new BizException("金额精确小数后两位");
//			}
//		}
		
//		String s = "2.";
//		String xx = s.substring(s.indexOf(".") + 1, s.length());
//		System.out.println(xx.length());
		
		String amount = "1110.1";
		
		String regex = "([1-9]{1}\\d*|\\d{1})[.]{0,1}\\d{0,2}";
		
		boolean flag = Pattern.matches(regex, amount);
		System.out.println(flag);
	}

}
