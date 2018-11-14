package cn.succy.alarm.provider.impl;

import cn.hutool.db.Entity;
import cn.hutool.db.SqlRunner;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.db.handler.RsHandler;
import cn.succy.alarm.common.Constant;
import cn.succy.alarm.config.ConfigManager;
import cn.succy.alarm.config.ProviderConfig;

import cn.succy.alarm.provider.ContactProvider;
import cn.succy.alarm.receiver.Contact;
import cn.succy.alarm.receiver.ContactGroup;
import cn.succy.alarm.receiver.Receiver;
import cn.succy.alarm.resources.AlarmResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 使用jdbc方式获取联系人的实现类
 *
 * @author Succy
 * @date 2017-10-13 17:38
 **/

public class JdbcContactProviderImpl implements ContactProvider {
    private static final Logger logger = LoggerFactory.getLogger(JdbcContactProviderImpl.class);

    private ProviderConfig config = ConfigManager.getConfig(ProviderConfig.class);
    private DataSource dataSource = new SimpleDataSource(config.getJdbcUrl(), config.getJdbcUsername(), config.getJdbcPassword());
    private SqlRunner runner = SqlRunner.create(dataSource);

    @Override
    public Receiver getReceiver() throws ParseException {
        Receiver receiver = new Receiver();
        List<ContactGroup> contactGroupList = new ArrayList<>();
        String sql = "SELECT\n" +
                "\t`group`.*\n" +
                "FROM\n" +
                "\t`contact_group`,\n" +
                "\treceiver\n" +
                "WHERE\n" +
                "\treceiver.group_name = `contact_group`.group_name;";
        String sql_alarm = "SELECT\n" +
        		"\t*\n" +
                "FROM\n" +
                "\talarm_resource\n" +
                "WHERE\n" +
                "\tstatus = 0;";
        try {
            List<Entity> groupNameEntitys = runner.query(sql, null);
            
            List<Entity> alarmResourceEntitys = runner.query(sql_alarm, null);
            List<AlarmResource> alarmList = new ArrayList<AlarmResource>();
            
            for (Entity entity : alarmResourceEntitys) {
                AlarmResource alarmRes = new AlarmResource();
                
                //时间处理
    	        String date = entity.getStr(Constant.FIELD_ALARM_DATE_TIME);
    	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // your template here
    	        Date dateStr = formatter.parse(date);
    	        java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());
                
                alarmRes.setId(entity.getStr(Constant.FIELD_ALARM_ID));
                alarmRes.setAlarmName(entity.getStr(Constant.FIELD_ALARM_NAME));
                alarmRes.setAppName(entity.getStr(Constant.FIELD_ALARM_APP_NAME));
                alarmRes.setLevel(entity.getStr(Constant.FIELD_ALARM_LEVEL));
                alarmRes.setHost(entity.getStr(Constant.FIELD_ALARM_HOST));
                alarmRes.setDateTime(dateDB);
                alarmRes.setContent(entity.getStr(Constant.FIELD_ALARM_CONTENT));
                alarmRes.setTraceStack(entity.getStr(Constant.FIELD_ALARM_TRACE_STACK));
                alarmRes.setException(entity.getStr(Constant.FIELD_ALARM_EXCEPTION));
                alarmRes.setStatus(entity.getStr(Constant.FIELD_ALARM_STATUS));
                alarmList.add(alarmRes);
                
                System.out.println("******************");
                System.out.println(alarmRes);
                System.out.println(alarmList);
                System.out.println("******************");
            }
            
            for (Entity entity : groupNameEntitys) {
                ContactGroup contactGroup = getContactGroup(entity.getStr(Constant.FIELD_GROUP_ID), entity.getStr(Constant.FIELD_GROUP_NAME));
                contactGroupList.add(contactGroup);
            }
        } catch (SQLException e) {
            logger.error("get receivers occur error! message: {}", e.getMessage(), e);
            throw new RuntimeException("get receivers occur error", e);
        }

        receiver.setContactGroupList(contactGroupList);
        return receiver;
    }


    private ContactGroup getContactGroup(String groupId, String groupName) {
        ContactGroup contactGroup = new ContactGroup();
        contactGroup.setGroupId(groupId);
        contactGroup.setGroupName(groupName);
        String sql = "SELECT\n" +
                "\t*\n" +
                "FROM\n" +
                "\tcontact\n" +
                "WHERE\n" +
                "\tcontact_id IN (\n" +
                "\t\tSELECT\n" +
                "\t\t\tcontact_id\n" +
                "\t\tFROM\n" +
                "\t\t\t`contact_group`,\n" +
                "\t\t\t`group_rel_contact`\n" +
                "\t\tWHERE\n" +
                "\t\t\t`contact_group`.group_id = group_rel_contact.group_id\n" +
                "\t\tAND `contact_group`.group_name = ?\n" +
                "\t)";
        try {
            List<Contact> contactList = runner.query(sql, new RsHandler<List<Contact>>() {
                @Override
                public List<Contact> handle(ResultSet rs) throws SQLException {
                    List<Contact> contactList = new ArrayList<Contact>();
                    while (rs.next()) {
                        Contact contact = new Contact();
                        contact.setContactId(rs.getString(Constant.FIELD_CONTACT_ID));
                        contact.setEmail(rs.getString(Constant.FIELD_EMAIL));
                        contact.setName(rs.getString(Constant.FIELD_NAME));
                        contact.setPhone(rs.getString(Constant.FIELD_PHONE));
                        contact.setWechat(rs.getString(Constant.FIELD_WECHAT));

                        contactList.add(contact);
                    }

                    return contactList;
                }
            }, groupName);

            contactGroup.setContactList(contactList);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return contactGroup;
    }
}
