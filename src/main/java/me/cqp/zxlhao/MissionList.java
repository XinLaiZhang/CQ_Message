package me.cqp.zxlhao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于管理任务清单
 * 
 * @author XinLa
 *
 */
public class MissionList
{
	// 任务表名

	public static final String M_INTERVAL = "M_Interval";
	public static final String M_I = "M_I";
	public static final String M_EVERY_NUM = "M_EveryNum";
	public static final String M_TEXT = "M_Text"; // 提醒内容
	public static final String M_GROUPNUM = "M_Groupnum"; // 群号
	public static final String M_SAVE_TEXT = "M_SaveText"; // 保存文本信息
	public static final String M_REMIND_TIME = "M_RemindTime"; // 提醒时间
	public static final String M_END_TIME = "M_EndTime"; // 结束时间
	public static final String M_START_TIME = "M_StartTime"; // 开始时间
	public static final String M_REPLY = "M_Reply"; // 回复内容
	public static final String M_REGEX = "M_Regex"; // 正则表达式
	public static final String M_TYPE = "M_Type"; // 任务收集方式
	public static final String M_STATUS = "M_Status"; // 任务状态
	public static final String M_LIST = "M_list"; // 名单
	public static final String M_CLASS = "M_Class"; // 任务类型
	public static final String M_TITLE = "M_Title"; // 任务标题
	public static final String M_ID = "M_ID"; // 任务ID

	// 任务清单
	private ArrayList<Map<String, String>> MissionCountListBefore = new ArrayList<Map<String, String>>();// 未开始任务
	private ArrayList<Map<String, String>> MissionCountListOn = new ArrayList<Map<String, String>>(); // 进行中任务
	private ArrayList<Map<String, String>> MissionCountListOther = new ArrayList<Map<String, String>>(); // 结束任务
	private Map<String, PersonList> personList = new HashMap<String, PersonList>(); // 名单

	private ArrayList<Map<String, String>> MissionRemindListBefore = new ArrayList<Map<String, String>>(); // 未开始任务
	private ArrayList<Map<String, String>> MissionRemindListFinish = new ArrayList<Map<String, String>>();// 结束任务
	// 时间戳
	private long timestamp;
	// 任务类型
	public static final byte M_CLASS_MISSION_COUNT = 0; // 统计任务
	public static final byte M_CLASS_MISSION_REMINDCOUNT = 1; // 提醒并统计任务
	public static final byte M_CLASS_MISSION_REMIND = 2; // 仅提醒
	public static final byte M_CLASS_MISSION_FORCOUNT = 3; // 循环提醒统计
	public static final byte M_CLASS_MISSION_FORREMIND = 4; // 循环提醒

	// 任务状态
	public static final byte M_STATUS_MISSION_BEFORE = 0; // 未开始
	public static final byte M_STATUS_MISSION_ON = 1; // 进行中
	public static final byte M_STATUS_MISSION_SUCCESS = 2; // 任务完成
	public static final byte M_STATUS_MISSION_FAIL = 3; // 任务未完成
	// 任务进行类型
	public static final byte M_TYPE_MISSION_PRIVATE = 0; // 私聊
	public static final byte M_TYPE_MISSION_GROUP = 1; // 群
	public static final byte M_TYPE_MISSION_DISSCUSS = 2; // 私聊

	/**
	 * 从数据库中获取任务清单并进行管理，调用前请配置好配置文件及数据库
	 * 
	 * 
	 * @throws SQLException
	 */
	public MissionList() throws SQLException
	{
		super();
		System.out.println("[MissionList]:开始读取任务数据....");
		getData();
		System.out.println("[MissionList]:注册时间戳....");
		timestamp = new Date().getTime();
	}

	/**
	 * 获取时间戳
	 * 
	 * @return
	 */
	public long getTimestamp()
	{
		System.out.println("[MissionList]:获取时间戳....");
		return timestamp;
	}

	/**
	 * 从数据库获取数据，用于处理
	 * 
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	private void getData() throws SQLException, NumberFormatException
	{
		Connection conn = JDBCMysqlTools.getConnection();
		// 获取CountList
		String sql = "select * from MissionCountList where M_Status = 0 or M_Status = 1 or M_StartTime > ";
		Statement state = conn.createStatement();
		ResultSet resultSet = state.executeQuery(sql + new Date().getTime() + " order by M_StartTime");
		System.out.println("[MissionList]:获取统计类任务....");
		while (resultSet.next())
		{
			// System.out.println(resultSet.get);
			Map<String, String> map = new HashMap<String, String>();
			map.put(M_ID, resultSet.getString(M_ID).trim());
			map.put(M_TITLE, resultSet.getString(M_TITLE).trim());
			map.put(M_CLASS, resultSet.getString(M_CLASS).trim());
			map.put(M_LIST, resultSet.getString(M_LIST).trim());
			map.put(M_STATUS, resultSet.getString(M_STATUS).trim());
			map.put(M_TYPE, resultSet.getString(M_TYPE).trim());
			map.put(M_REGEX, resultSet.getString(M_REGEX).trim());
			String tmp = resultSet.getString(M_REPLY).trim();
			tmp = tmp.equals("[]") ? "" : tmp.substring(1, tmp.length() - 1);
			map.put(M_REPLY, tmp);
			map.put(M_START_TIME, resultSet.getString(M_START_TIME).trim());
			map.put(M_END_TIME, resultSet.getString(M_END_TIME).trim());
			map.put(M_REMIND_TIME, resultSet.getString(M_REMIND_TIME).trim());
			tmp = resultSet.getString(M_SAVE_TEXT).trim();
			tmp = tmp.equals("[]") ? "" : tmp.substring(1, tmp.length() - 1);
			map.put(M_SAVE_TEXT, tmp);
			map.put(M_GROUPNUM, resultSet.getString(M_GROUPNUM));
			map.put(M_TEXT, resultSet.getString(M_TEXT));
			if (Integer.parseInt(map.get(M_STATUS)) == MissionList.M_STATUS_MISSION_BEFORE)
				MissionCountListBefore.add(map);
			else
			{
				MissionCountListOn.add(map);
				// 获取名单
				personList.put(map.get(M_ID), new PersonList(map.get(M_LIST)));
			}
		}
		// 获取RemindList
		sql = "select * from MissionRemindList where M_Status = 0 or M_StartTime > ";
		JDBCMysqlTools.release(resultSet);
		resultSet = state.executeQuery(sql + new Date().getTime() + " order by M_StartTime");
		System.out.println("[MissionList]:获取提醒类任务....");
		while (resultSet.next())
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put(M_ID, resultSet.getString(M_ID));
			map.put(M_TITLE, resultSet.getString(M_TITLE));
			map.put(M_CLASS, resultSet.getString(M_CLASS));
			map.put(M_LIST, resultSet.getString(M_LIST));
			map.put(M_STATUS, resultSet.getString(M_STATUS));
			map.put(M_TYPE, resultSet.getString(M_TYPE));
			map.put(M_START_TIME, resultSet.getString(M_START_TIME));
			map.put(M_GROUPNUM, resultSet.getString(M_GROUPNUM));
			map.put(M_TEXT, resultSet.getString(M_TEXT));
			map.put(M_EVERY_NUM, resultSet.getString(M_EVERY_NUM));
			map.put(M_I, resultSet.getString(M_I));
			map.put(M_INTERVAL, resultSet.getString(M_INTERVAL));
			MissionRemindListBefore.add(map);
		}
		JDBCMysqlTools.release(resultSet, state, conn);
	}

	public ArrayList<Map<String, String>> getMissionCountListBefore()
	{
		System.out.println("[MissionList]:获取MissionCountListBefore....");
		return MissionCountListBefore;
	}

	public ArrayList<Map<String, String>> getMissionCountListOn()
	{
		System.out.println("[MissionList]:获取MissionCountListOn....");
		return MissionCountListOn;
	}

	public ArrayList<Map<String, String>> getMissionCountListOther()
	{
		System.out.println("[MissionList]:获取MissionCountListOther....");
		return MissionCountListOther;
	}

	public Map<String, PersonList> getPersonList()
	{
		System.out.println("[MissionList]:获取PersonList....");
		return personList;
	}

	public ArrayList<Map<String, String>> getMissionRemindListBefore()
	{
		System.out.println("[MissionList]:获取MissionRemindListBefore....");
		return MissionRemindListBefore;
	}

	public ArrayList<Map<String, String>> getMissionRemindListFinish()
	{
		System.out.println("[MissionList]:获取MissionRemindListFinish....");
		return MissionRemindListFinish;
	}

	/**
	 * 更新信息到数据库同时清空过期任务
	 * 
	 * @return
	 * @throws SQLException
	 */
	public boolean saveMission() throws SQLException
	{
		System.out.println("[MissionList]:可持久化存储任务列表....");
		Connection conn = JDBCMysqlTools.getConnection();
		// 开启事务
		conn.setAutoCommit(false);
		Statement state = conn.createStatement();
		String sql;
		System.out.println("[MissionList]:开始存储MissionCountListOn....");
		for (Map<String, String> map : MissionCountListOn)
		{
			sql = "update MissionList set " + MissionList.M_STATUS + "=\'" + map.get(MissionList.M_STATUS)
					+ "\' where M_ID=" + map.get(M_ID);
			//System.out.println(sql);
			state.addBatch(sql);
			personList.get(map.get(M_ID)).saveList(state);
		}
		System.out.println("[MissionList]:开始存储MissionCountListOther....");
		for (Map<String, String> map : MissionCountListOther)
		{
			sql = "update MissionList set " + MissionList.M_STATUS + "=\'" + map.get(MissionList.M_STATUS)
					+ "\' where M_ID=" + map.get(M_ID);
			//System.out.println(sql);
			state.addBatch(sql);
			PersonList list = personList.get(M_ID);
			if (list != null)
			{
				list.saveList(state);
				// 过期任务移除
				personList.remove(M_ID);
			}
		}
		System.out.println("[MissionList]:开始存储MissionRemindListFinish....");
		for (Map<String, String> map : MissionRemindListFinish)
		{
			sql = "update MissionList set " + MissionList.M_STATUS + "=\'" + map.get(MissionList.M_STATUS)
					+ "\' where M_ID=" + map.get(M_ID);
			//System.out.println(sql);
			state.addBatch(sql);
		}
		state.executeBatch();
		conn.commit();
		conn.setAutoCommit(true);
		JDBCMysqlTools.release(state, conn);
		// 清空完成任务
		System.out.println("[MissionList]:清空完成任务列表....");
		MissionCountListOther.clear();
		MissionRemindListFinish.clear();
		return true;
	}

	/**
	 * 更新任务列表，使用前请储存数据到数据库
	 * 
	 * @return
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	public boolean updateMission() throws NumberFormatException, SQLException
	{
		System.out.println("[MissionList]:更新任务列表，清空任务列表....");
		MissionCountListBefore.clear();
		MissionCountListOn.clear();
		MissionCountListOther.clear();
		MissionRemindListBefore.clear();
		MissionRemindListFinish.clear();
		personList.clear();
		System.out.println("[MissionList]:获取数据....");
		getData();
		System.out.println("[MissionList]:记录时间戳....");
		timestamp = new Date().getTime();
		return true;
	}

	/**
	 * 用于检查统计类型任务清单，将到达时间任务添加至任务列表
	 * 
	 * @param nowTime
	 * 
	 * @return
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	public boolean checkMission(long nowTime) throws NumberFormatException, SQLException
	{
		// 未开始任务检查
		System.out.println("[MissionList]:检查任务....");
		ArrayList<Map<String, String>> needRemove = new ArrayList<Map<String, String>>();
		for (Map<String, String> map : MissionCountListBefore)
		{
			if (Long.parseLong(map.get(M_START_TIME)) <= nowTime)
			{
				// 修改状态
				map.put(M_STATUS, M_STATUS_MISSION_ON + "");
				// 加入运行时任务
				MissionCountListOn.add(map);
				// 获取名单
				personList.put(map.get(M_ID), new PersonList(map.get(M_LIST)));
				// 导致bug
				// MissionCountListBefore.remove(map);
				needRemove.add(map);
				if (map.get(MissionList.M_TEXT) != null)
					com.sobte.cqp.jcq.event.JcqApp.CQ.sendGroupMsg(Long.parseLong(map.get(M_GROUPNUM)),
							com.sobte.cqp.jcq.event.JcqApp.CC.at(-1) + map.get(M_TEXT));
			} else
				break;
		}
		MissionCountListBefore.removeAll(needRemove);
		needRemove.clear();
		// 运行中任务检查
		for (Map<String, String> map : MissionCountListOn)
		{
			if (Long.parseLong(map.get(M_END_TIME)) <= nowTime)
			{
				// 修改状态
				if (personList.get(map.get(M_ID)).isSuccess())
					map.put(M_STATUS, "" + M_STATUS_MISSION_SUCCESS);
				else
					map.put(M_STATUS, "" + M_STATUS_MISSION_FAIL);
				// 加入完成列表
				MissionCountListOther.add(map);
				needRemove.add(map);
				// MissionCountListOn.remove(map);
			} else
				break;
		}
		MissionCountListOn.removeAll(needRemove);
		if ((this.timestamp - nowTime) / 60000 >= 5)
		{
			this.saveMission();
			this.updateMission();
		}
		return true;
	}

}
