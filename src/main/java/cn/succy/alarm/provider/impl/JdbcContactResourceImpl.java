package cn.succy.alarm.provider.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.db.Entity;
import cn.hutool.db.SqlRunner;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.db.handler.RsHandler;
import cn.succy.alarm.common.Constant;
import cn.succy.alarm.config.ConfigManager;
import cn.succy.alarm.config.ProviderConfig;
import cn.succy.alarm.provider.ResourceProvider;
import cn.succy.alarm.receiver.Contact;
import cn.succy.alarm.receiver.ContactGroup;
import cn.succy.alarm.receiver.Receiver;
import cn.succy.alarm.receiver.ResourceReceiver;
import cn.succy.alarm.resources.AlarmResource;

import java.util.HashMap;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 使用jdbc方式获取告警信息的实现类
 * 
 * @author ranzhonggeng
 *
 */
public class JdbcContactResourceImpl implements ResourceProvider{
	private static final Logger logger = LoggerFactory.getLogger(JdbcContactProviderImpl.class);

    private ProviderConfig config = ConfigManager.getConfig(ProviderConfig.class);
    private DataSource dataSource = new SimpleDataSource(config.getJdbcUrl(), config.getJdbcUsername(), config.getJdbcPassword());
    private SqlRunner runner = SqlRunner.create(dataSource);
    @Override
    public AlarmResource getReceiver() throws ParseException {
    	AlarmResource resource = new AlarmResource();
    	
    	String sql_alarm = "SELECT\n" +
        		"\t*\n" +
                "FROM\n" +
                "\talarm_resource\n" +
                "WHERE\n" +
                "\tstatus = 0;";
    	try {
            List<Entity> alarmResourceEntitys = runner.query(sql_alarm, null);
            List<AlarmResource> alarmList = new ArrayList<AlarmResource>();
            
            for (Entity entity : alarmResourceEntitys) {
                AlarmResource alarmRes = new AlarmResource();
                
                //时间处理
    	        String date = entity.getStr(Constant.FIELD_ALARM_DATE_TIME);
    	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // your template here
    	        Date dateStr = formatter.parse(date);
    	        java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());
                
                resource.setId(entity.getStr(Constant.FIELD_ALARM_ID));
                resource.setAlarmName(entity.getStr(Constant.FIELD_ALARM_NAME));
                resource.setAppName(entity.getStr(Constant.FIELD_ALARM_APP_NAME));
                resource.setLevel(entity.getStr(Constant.FIELD_ALARM_LEVEL));
                resource.setHost(entity.getStr(Constant.FIELD_ALARM_HOST));
                resource.setDateTime(dateDB);
                resource.setContent(entity.getStr(Constant.FIELD_ALARM_CONTENT));
                resource.setTraceStack(entity.getStr(Constant.FIELD_ALARM_TRACE_STACK));
                resource.setException(entity.getStr(Constant.FIELD_ALARM_EXCEPTION));
                resource.setStatus(entity.getStr(Constant.FIELD_ALARM_STATUS));
                alarmList.add(alarmRes);
                
                System.out.println("******************");
                System.out.println(entity.getStr(Constant.FIELD_ALARM_NAME));
                System.out.println(alarmRes);
                System.out.println(alarmList);
                System.out.println("******************");
            }
        } catch (SQLException e) {
            logger.error("get receivers occur error! message: {}", e.getMessage(), e);
            throw new RuntimeException("get receivers occur error", e);
        }

        return resource;
    }
}