package me.cqp.zxlhao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCMysqlTools
{
	private static String driverClass = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://127.0.0.1:3306/shop?rewriteBatchedStatements=true&useSSL=false";
	private static String username = "root";
	private static String password = "root";

//	/**
//	 * 数据库操纵类
//	 * @param driverClass	驱动类
//	 * @param url			连接地址
//	 * @param username		用户名
//	 * @param password		密码
//	 */
//	public JDBCMysqlTools(String driverClass, String url, String username, String password)
//	{
//		super();
//		this.driverClass = driverClass;
//		this.url = url;
//		this.username = username;
//		this.password = password;
//	}

	/**
	 * 修改配置
	 * 
	 * @param driverClass 驱动类
	 * @param url         连接地址
	 * @param username    用户名
	 * @param password    密码
	 */
	public static void SetMysql(String driverClass, String url, String username, String password)
	{
		JDBCMysqlTools.driverClass = driverClass;
		JDBCMysqlTools.url = url;
		JDBCMysqlTools.username = username;
		JDBCMysqlTools.password = password;
	}

	private JDBCMysqlTools()
	{

	}
//	/**
//	 * 从连接池内获取Connection连接
//	 * 
//	 * @return 一个连接
//	 * @throws SQLException
//	 */
//	public static Connection getConnection() throws SQLException
//	{
//		Connection conn = dds.getConnection();
//		return conn;
//	}

	/**
	 * 释放资源
	 * 
	 * @param statement
	 * @param conn      连接
	 */
	public static void release(Statement stmt, Connection conn)
	{
		if (stmt != null)
		{
			try
			{
				stmt.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			stmt = null;
		}
		if (conn != null)
		{
			try
			{
				conn.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			conn = null;
		}
	}

	/**
	 * 释放资源
	 * 
	 * @param rs   结果集
	 * @param stmt
	 * @param conn
	 */
	public static void release(ResultSet rs, Statement stmt, Connection conn)
	{
		if (rs != null)
		{
			try
			{
				rs.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			rs = null;
		}
		if (stmt != null)
		{
			try
			{
				stmt.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			stmt = null;
		}
		if (conn != null)
		{
			try
			{
				conn.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			conn = null;
		}
	}

	/**
	 * 释放资源
	 * 
	 * @param rs   结果集
	 */
	public static void release(ResultSet rs)
	{
		if (rs != null)
		{
			try
			{
				rs.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			rs = null;
		}
	}
	
//	public static DataSource getDataSource()
//	{
//		return dds;
//	}

	/**
	 * 已过时 注册jdbc驱动
	 */

	public static void loadDrive()
	{
		// 注册驱动
		try
		{
			Class.forName(driverClass);
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 已过时 获取Connection
	 * 
	 * @return Connection
	 */
	public static Connection getConnection()
	{
		Connection conn = null;
		try
		{
			loadDrive();
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return conn;
	}

}
