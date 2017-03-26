package com.github.lamsadetools.conferences;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;
public class ConnectionDataBase {
	
	private static String url = "jdbc:h2:~/conferences";   
	private static String username = "sa";   
    private static String password = "sa";
    private static Connection conn;
    private static JdbcConnectionPool cp;

		
    public static Connection getConnection() throws SQLException{
    	
    	ConnectionDataBase.setCp(JdbcConnectionPool.create(ConnectionDataBase.getUrl(),ConnectionDataBase.getUsername(), ConnectionDataBase.getPassword()));
    	
    	ConnectionDataBase.setConn(ConnectionDataBase.getCp().getConnection());
    	
    	return ConnectionDataBase.getConn();
    }
    
    public static void closeAndDisposeConnection() throws SQLException{
    	
    	ConnectionDataBase.getConn().close();
    	ConnectionDataBase.getCp();
    	
    }
    
    public static void sqlQuery( String query) throws SQLException{
    	
    	ConnectionDataBase.getConn().createStatement().execute(query);
    }


	public static String getUrl() {
		return url;
	}


	public static void setUrl(String url) {
		ConnectionDataBase.url = url;
	}


	public static String getUsername() {
		return username;
	}


	public static void setUsername(String username) {
		ConnectionDataBase.username = username;
	}


	public static String getPassword() {
		return password;
	}


	public static void setPassword(String password) {
		ConnectionDataBase.password = password;
	}


	public static Connection getConn() {
		return conn;
	}


	public static void setConn(Connection conn) {
		ConnectionDataBase.conn = conn;
	}


	public static JdbcConnectionPool getCp() {
		return cp;
	}


	public static void setCp(JdbcConnectionPool cp) {
		ConnectionDataBase.cp = cp;
	}

}
