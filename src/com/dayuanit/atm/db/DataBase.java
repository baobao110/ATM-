package com.dayuanit.atm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.Properties;

public class DataBase {
	
	private static LinkedList<Connection> conns = new LinkedList<Connection>();
	
	private static final int CONN_MAX = 10;
	
	static {
		try {
			
			Properties pro = new Properties();
			pro.load(DataBase.class.getResourceAsStream("/jdbc.properties"));
			
			Class.forName(pro.getProperty("jdbc.driver"));
			
			for (int i = 0; i < CONN_MAX; i ++) {
				conns.offerLast(DriverManager.getConnection(pro.getProperty("jdbc.url"), pro.getProperty("jdbc.username"), pro.getProperty("jdbc.password")));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized Connection getConnection() {
		System.out.println(conns.size());
		return conns.pollFirst();
	}
	
	public static synchronized void close(Connection conn) {
		if (null == conn) {
			return;
		}
		
		System.out.println("========return conn=============");
		conns.offerLast(conn);
	}
	
}
