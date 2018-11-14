package cn.succy.alarm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.hutool.json.JSONException;
import cn.succy.alarm.resources.AlarmResource;
import cn.succy.alarm.template.TemplateModel;

/**
 * 告警对外接口
 * @author ranzhonggeng
 *
 * 2018年11月9日
 */
public interface AlarmReceiverDao {
	
	/**
	 * 告警添加数据库
	 * @param contact
	 * @return boolean
	 */
	boolean addAlarmResource(AlarmResource contact);
	
	/**
	 * 发送告警信息到wechat和email
	 * @param model
	 */
	void sendAlarm(TemplateModel model);
	
	/**
	 * 告警获取
	 * @return	Alarm String
	 * @throws JSONException
	 * @throws SQLException
	 */
	String getAlarmResource() throws JSONException, SQLException;

}
