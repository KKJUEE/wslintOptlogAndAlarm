package cn.succy.alarm.common;

/**
 * @author Succy
 */
public interface Constant {
    // 涉及到的表名
    String TABLE_RECEIVER = "receiver";
    String TABLE_GROUP = "contact_group";
    String TABLE_CONTACT = "contact";
    String TABLE_GROUP_CONTACT = "group_rel_contact";

    // 涉及到的字段
    String FIELD_GROUP_NAME = "group_name";
    String FIELD_GROUP_ID = "group_id";
    String FIELD_CONTACT_ID = "contact_id";
    String FIELD_NAME = "name";
    String FIELD_EMAIL = "email";
    String FIELD_PHONE = "phone";
    String FIELD_WECHAT = "wechat";
    
    //涉及到的alarm表名
    String FIELD_ALARM_ID = "id";
    String FIELD_ALARM_NAME = "alarm_name";
    String FIELD_ALARM_APP_NAME = "app_name";
    String FIELD_ALARM_LEVEL = "level";
    String FIELD_ALARM_HOST = "host";
    String FIELD_ALARM_DATE_TIME = "date_time";
    String FIELD_ALARM_CONTENT = "content";
    String FIELD_ALARM_TRACE_STACK = "trace_stack";
    String FIELD_ALARM_EXCEPTION = "exception";
    String FIELD_ALARM_STATUS = "status";
    
    String ALAEM_QUEUE_NAME = "alarm_queue";
}
