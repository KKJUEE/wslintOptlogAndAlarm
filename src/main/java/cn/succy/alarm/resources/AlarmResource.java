package cn.succy.alarm.resources;

import java.sql.Date;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;

/**
 * 告警实体信息
 * 获取告警的字段信息
 * 包括id，级别，日期，详细信息，
 * @author ranzhonggeng
 *
 * 2018年11月9日
 */
public class AlarmResource {
	private static final long serialVersionUID = -6201793338937462437L;
	/**
     * 警报id
     */
    private String id;
    /**
     * 警报名称
     */
    private String alarmName;

    /**
     * 告警模块名称
     */
    private String appName;
    /**
     * 警报级别
     */
    private String level;
    /**
     * 告警的主机
     */
    private String host;
    /**
     * 报警时间
     */
    private Date dateTime;
    /**
     * 报警的内容
     */
    private String content;
    /**
     * 报错方法的堆栈，方便开发人员快速定位异常
     */
    private String traceStack;
    /**
     * 异常堆栈
     */
    private String exception;
    /**
     * 状态：0：异常，1：已恢复（未推送），2：历史异常（已解决）
     */
    private String status;
    
	public String getAlarmName() {
		return alarmName;
	}
	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateDB) {
		this.dateTime = dateDB;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTraceStack() {
		return traceStack;
	}
	public void setTraceStack(String traceStack) {
		this.traceStack = traceStack;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
    }

    public Map<String, Object> toMap() {
        return BeanUtil.beanToMap(this);
    }
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public AlarmResource(String alarmName, String level, String host, Date dateTime, String content) {
        this.alarmName = alarmName;
        this.level = level;
        this.host = host;
        this.dateTime = dateTime;
        this.content = content;
    }
	
	public AlarmResource() {
    }
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
