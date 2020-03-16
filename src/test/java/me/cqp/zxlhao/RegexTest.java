package me.cqp.zxlhao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.junit.jupiter.api.Test;


public class RegexTest
{
//	@Test
//	public static void main(String[] args) 
//	{
////		Properties config = new Properties();
////		config.setProperty("driverClass", "com.mysql.jdbc.Driver");
////		config.setProperty("url", "jdbc:mysql://127.0.0.1:3306/CQ_Message?rewriteBatchedStatements=true&useSSL=false");
////		config.setProperty("username", "root");
////		config.setProperty("password", "zxl0504.");
////
////		JDBCMysqlTools.SetMysql(config.getProperty("driverClass"), config.getProperty("url"),
////				config.getProperty("username"), config.getProperty("password"));
////
////		Connection connection = JDBCMysqlTools.getConnection();
////		connection.setAutoCommit(false);
////		Statement statement = connection.createStatement();
////		String sql = "insert into MissionCountList (M_Title,M_Class,M_List,M_Groupnum,M_Status,M_Type,M_Own,M_Text,M_Regex,M_Reply,M_StartTime,M_EndTime,M_RemindTime,M_SaveText) valuse "
////				+ "('测试','2',);";
////		
////		
////		
//		
//	}
	@Test
	public void test2() throws UnsupportedEncodingException
	{
		
		//String encode = URLEncoder.encode("查《诗人之死》出自俄国天才诗人__________之笔","utf-8");
		//String msg = "查 中国特色社会主义制度";
		//System.out.println(encode);
		//System.out.println(msg.substring(1).trim());
	}
	
	@Test
	public void test1() throws InterruptedException
	{
		//System.out.println("执行完成");
		MsgManage.main(new String[0]);
	}
}
