package com.itheima.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;

public class JDBCUtils {

	static ComboPooledDataSource cpds = new ComboPooledDataSource();

	// 获取连接
	public static Connection getConnection() throws Exception {
		return cpds.getConnection();
	}

	// 获得连接池
	public static DataSource getDataSource() {
		return cpds;
	}

}
