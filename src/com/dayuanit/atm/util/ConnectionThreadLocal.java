package com.dayuanit.atm.util;

import java.sql.Connection;

import com.dayuanit.atm.db.DataBase;

public class ConnectionThreadLocal {
	
	private static final ThreadLocal<Connection> connectionLocal = new ThreadLocal<Connection>();
	
	public static Connection getConnection() {
		return connectionLocal.get();
	}
	
	public static void setConnection(Connection conn) {
		connectionLocal.set(conn);
	}
	
	public static void removeConnection() {
		Connection conn = getConnection();
		DataBase.close(conn);
		connectionLocal.remove();
	}

}
