package com.bograntex.db;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

public class DBConnectERP {
	
	private String server = "192.168.1.7";
    private String database = "dbsystex";
    
//    private String server = "192.168.1.9";
//    String database = "TESTE";
    
    private String user = "bg";
    private String passwd = "oracle";
    private Connection con = null;
	
	public Connection getInstance() throws SQLException {
		if(con == null) {
			OracleDataSource ods = new OracleDataSource();
			ods.setUser(user);
			ods.setPassword(passwd);
			ods.setServerName(server);
			ods.setPortNumber(1521);
			ods.setServiceName(database);
			ods.setDriverType("thin");
			con = ods.getConnection();
		}
		return con;
	}

}
