package cn.succy.alarm.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.succy.alarm.Alarm;
import cn.succy.alarm.DB.DBconn;
import cn.succy.alarm.dao.AlarmReceiverDao;
import cn.succy.alarm.resources.AlarmResource;
import cn.succy.alarm.template.TemplateModel;

/**
 * 告警对外接口实现
 * 1、获取email、wechat联系人
 * 2、发送告警至email、wechat
 * 3、获取数据库连接
 * 4、保存告警信息到数据库alarm_resource，调用
 * 
 * @author ranzhonggeng
 *
 * 2018年11月9日
 */
public class AlarmReceiverDaoImpl implements AlarmReceiverDao{
	
	
//	private static final Logger logger = LoggerFactory.getLogger(AlarmReceiverDaoImpl.class);
	private PreparedStatement ptmt = null;  
    private ResultSet rs = null;
	@Override
	//4、捕获告警时，添加数据库
    public boolean addAlarmResource(AlarmResource alarm) {
        // TODO Auto-generated method stub
        boolean flag = false;
        DBconn.init();
        int i =DBconn.addUpdDel("insert into alarm_resource(id,alarm_name,app_name,level,host,date_time,content,trace_stack,exception,status) " +
                "values('"+alarm.getId() + "','"+alarm.getAlarmName()+"','"+alarm.getAppName()+"','"+alarm.getLevel() +"','"
        		+alarm.getHost()+ "','" + alarm.getDateTime()+ "','"+alarm.getContent()+"','"+alarm.getTraceStack()+"','" + alarm.getException()+"','"+alarm.getStatus()+"')");
        if(i>0){
            flag = true;
        }
        System.out.println("addAlarmResource success!");
        DBconn.closeConn();
        return flag;
    }
	
	@Override
	//4、查询告警信息
	//根据条件查询AlarmResource alarm
    public String getAlarmResource() throws JSONException, SQLException {
        // TODO Auto-generated method stub
        DBconn.init();
        ResultSet alarmResource =DBconn.selectSql("select * from alarm_resource");
        String resource = resultSetToJson(alarmResource);
        System.out.println("addAlarmResource success! ***" + resource);
        DBconn.closeConn();
        return resource;
    }
	
	//查询结果ResultSet的每一条数据转换成一个json对象，数据中的每一列的列名和值组成键值对，放在对象中，最后把对象组织成一个json数组。
    private String resultSetToJson(ResultSet rs) throws SQLException,JSONException
    {
       // json数组
       JSONArray array = new JSONArray();
      
       // 获取列数
       ResultSetMetaData metaData = rs.getMetaData();
       int columnCount = metaData.getColumnCount();
      
       // 遍历ResultSet中的每条数据
        while (rs.next()) {
            JSONObject jsonObj = new JSONObject();
           
            // 遍历每一列
            for (int i = 1; i <= columnCount; i++) {
                String columnName =metaData.getColumnLabel(i);
                String value = rs.getString(columnName);
                System.out.println(columnName + "columnName: ***" + value);
                jsonObj.put(columnName, value);
            } 
            array.put(jsonObj); 
        }
      
       return array.toString();
    }

	
	//实现告警发送
	@Override
	public void sendAlarm(TemplateModel model) {
		System.out.println("begin sendAlarm!");
//		logger.debug("begin sendAlarm");
		Alarm.send(model);
		System.out.println("end sendAlarm and success!");
//		logger.debug("end sendAlarm and success!");
	}
}
