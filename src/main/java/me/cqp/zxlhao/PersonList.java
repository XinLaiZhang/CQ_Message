package me.cqp.zxlhao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 用于存储人员列表
 * 
 * @author XinLa
 *
 */
public class PersonList
{
	public static final String N_SAVE_TEXT = "N_SaveText";
	public static final String N_STATUS = "N_Status";
	public static final String N_QQ = "N_QQ";
	public static final String N_NAME = "N_Name";
	public static final String N_ID = "N_ID";
	private Map<Long, Map<String, String>> List = new LinkedHashMap<Long, Map<String, String>>();
	// private ArrayList<Map<String, String>> List = new ArrayList<Map<String,
	// String>>();
	private String ListName;
	public static final byte M_STUTAS_PERSON_UNFINISH = 0; // 未完成
	public static final byte M_STUTAS_PERSON_FINISH = 1; // 完成

	/**
	 * 获取名单
	 * 
	 * @param ListName 名单表名
	 * @throws SQLException
	 */
	public PersonList(String ListName) throws SQLException
	{
		super();

		this.ListName = ListName;

		Connection conn = JDBCMysqlTools.getConnection();

		Statement state = conn.createStatement();
		String sql = "select * from " + ListName;

		ResultSet resultSet = state.executeQuery(sql);
		while (resultSet.next())
		{
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put(N_ID, resultSet.getString(N_ID).trim());
			map.put(N_NAME, resultSet.getString(N_NAME).trim());
			map.put(N_QQ, resultSet.getString(N_QQ).trim());
			map.put(N_STATUS, resultSet.getString(N_STATUS).trim());
			String tmp = resultSet.getString(N_SAVE_TEXT).trim();
			tmp = tmp.equals("[]") ? "" : tmp.substring(1, tmp.length() - 1);
			map.put(N_SAVE_TEXT, tmp);
			List.put(Long.parseLong(map.get(N_QQ)), map);
		}

		JDBCMysqlTools.release(resultSet, state, conn);
	}

	/**
	 * 
	 */

	public Map<Long, Map<String, String>> getList()
	{
		return List;
	}

	public Map<String, String> getList(long qq)
	{
		return List.get(qq);
	}

	/**
	 * 将表单转存到数据库内
	 * 
	 * @return 是否成功
	 */
	public boolean saveList()
	{

		String sql;
		Connection conn = JDBCMysqlTools.getConnection();
		try
		{
			// 开启事务
			conn.setAutoCommit(false);
			Statement state = conn.createStatement();
			Set<Long> keySet = this.List.keySet();
			for (long qq : keySet)
			{
				Map<String, String> map = this.List.get(qq);
				sql = "update " + this.ListName + " set "+N_STATUS+"=\'" + map.get(N_STATUS) + "\',"+N_SAVE_TEXT+"=\'["
						+ map.get(N_SAVE_TEXT) + "]\' where N_ID=" + map.get(N_ID);
				state.addBatch(sql);
			}
			state.executeBatch();
			conn.commit();
			JDBCMysqlTools.release(state, conn);
		} catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 将数据添加到事件内
	 * 
	 * @param state
	 * @return
	 */
	public boolean saveList(Statement state)
	{
		String sql;
		Set<Long> keySet = this.List.keySet();
		try
		{
			for (long qq : keySet)
			{
				Map<String, String> map = this.List.get(qq);
				sql = "update " + this.ListName + " set "+N_STATUS+"=\'" + map.get(N_STATUS) + "\',"+N_SAVE_TEXT+"=\'["
						+ map.get(N_SAVE_TEXT) + "]\' where N_ID=" + map.get(N_ID);
				state.addBatch(sql);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 检查任务是否完成
	 * 
	 * @return true 完成
	 */
	public boolean isSuccess()
	{
		for (long qq : this.List.keySet())
		{
			Map<String, String> map = this.List.get(qq);
			if (Byte.parseByte(map.get(N_STATUS)) != M_STUTAS_PERSON_FINISH)
				return false;
		}
		return true;
	}

}
