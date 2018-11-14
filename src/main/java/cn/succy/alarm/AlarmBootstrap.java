package cn.succy.alarm;

import cn.hutool.core.date.DateTime;
import cn.hutool.db.Entity;
import cn.hutool.db.SqlRunner;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.succy.alarm.common.Constant;
import cn.succy.alarm.common.Level;
import cn.succy.alarm.config.ConfigManager;
import cn.succy.alarm.config.ProviderConfig;
import cn.succy.alarm.dao.impl.AlarmContactDaoImpl;
import cn.succy.alarm.dao.impl.AlarmReceiverDaoImpl;
import cn.succy.alarm.provider.impl.JdbcContactProviderImpl;
import cn.succy.alarm.receiver.Contact;
import cn.succy.alarm.receiver.Receiver;
import cn.succy.alarm.resources.AlarmResource;
import cn.succy.alarm.template.TemplateModel;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 查询告警信息并调用推送接口
 * @author ranzhonggeng
 *
 */

public class AlarmBootstrap {
    private static final Logger logger = LoggerFactory.getLogger(AlarmBootstrap.class);
	private static final Level DEBUG = Level.ERROR;

    private static ProviderConfig config = ConfigManager.getConfig(ProviderConfig.class);
    private static DataSource dataSource = new SimpleDataSource(config.getJdbcUrl(), config.getJdbcUsername(), config.getJdbcPassword());
    private static SqlRunner runner = SqlRunner.create(dataSource);
    
    public static void main(String[] args) throws SQLException, ParseException {
        logger.info("--------- begin listening ----------");
//        AlarmResource resource = new AlarmResource();
        String sql_alarm = "SELECT\n" +
        		"\t*\n" +
                "FROM\n" +
                "\talarm_resource\n" +
                "WHERE\n" +
                "\tstatus = 0;";
        
      
        
        
        List<Entity> alarmResourceEntitys = runner.query(sql_alarm, null);
        List<AlarmResource> alarmList = new ArrayList<AlarmResource>();
        
        for (Entity entity : alarmResourceEntitys) {
            AlarmResource alarmRes = new AlarmResource();
            alarmRes.setId(entity.getStr(Constant.FIELD_ALARM_ID));
            alarmRes.setAlarmName(entity.getStr(Constant.FIELD_ALARM_NAME));
            alarmRes.setAppName(entity.getStr(Constant.FIELD_ALARM_APP_NAME));
            alarmRes.setLevel(entity.getStr(Constant.FIELD_ALARM_LEVEL));
            alarmRes.setHost(entity.getStr(Constant.FIELD_ALARM_HOST));
            
            //时间处理
            String date = entity.getStr(Constant.FIELD_ALARM_DATE_TIME);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // your template here
            Date dateStr = formatter.parse(date);
            java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());
            System.out.println("data:"+dateDB);
            
            alarmRes.setDateTime(dateDB);
            alarmRes.setContent(entity.getStr(Constant.FIELD_ALARM_CONTENT));
            alarmRes.setTraceStack(entity.getStr(Constant.FIELD_ALARM_TRACE_STACK));
            alarmRes.setException(entity.getStr(Constant.FIELD_ALARM_EXCEPTION));
            alarmRes.setStatus(entity.getStr(Constant.FIELD_ALARM_STATUS));
            alarmList.add(alarmRes);
            
            System.out.println("******************");
            System.out.println(entity.getStr(Constant.FIELD_ALARM_NAME));
            System.out.println(alarmRes);
            System.out.println(alarmList);
            System.out.println("******************");
        }
        for (AlarmResource alarm : alarmList) {
        	TemplateModel model = new TemplateModel();
            model.setAlarmName(alarm.getAlarmName());
            model.setAppName(alarm.getAppName());
            model.setContent(alarm.getContent());
            model.setDateTime(alarm.getDateTime());
            model.setLevel(DEBUG);
            
            AlarmReceiverDaoImpl alarmReceiver = new AlarmReceiverDaoImpl();
            //发送
            alarmReceiver.sendAlarm(model);
            //存储
            alarmReceiver.addAlarmResource(alarm);
        }
    }
}
