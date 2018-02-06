package com.dayuanit.atm.util;

import java.math.BigDecimal;

public class MoneyUtil {
	
	public static String plus(String num1, String num2) {
		BigDecimal b1 = new BigDecimal(num1);
		BigDecimal b2 = new BigDecimal(num2);
		return b1.add(b2).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
	}
	
	public static String sub(String num1, String num2) {
		BigDecimal b1 = new BigDecimal(num1);
		BigDecimal b2 = new BigDecimal(num2);
		return b1.subtract(b2).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
	}
	
	public static String mul(String num1, String num2) {
		BigDecimal b1 = new BigDecimal(num1);
		BigDecimal b2 = new BigDecimal(num2);
		return b1.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
	}
	
	public static String div(String num1, String num2) {
		BigDecimal b1 = new BigDecimal(num1);
		BigDecimal b2 = new BigDecimal(num2);
		
		return b1.divide(b2, 2, BigDecimal.ROUND_HALF_EVEN).toString();
	}
	
	public static void main(String[] args) {
		System.out.println(MoneyUtil.plus("1.211", "2.401"));
		System.out.println(MoneyUtil.sub("1.21", "2.40"));
		System.out.println(MoneyUtil.mul("1.21", "2.40"));
	}

}
