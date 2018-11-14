package cn.succy.alarm.common;

/**
 * 定义日志常量
 * @author ranzhonggeng
 *
 * 2018年11月13日
 */
public interface OptlogConstant {
	/**
	 * 操作日志ID
	 */
	String OPTLOG_ID = "id";
	/**
	 * 操作日志名称
	 */
	String OPTLOG_Name = "name";
	/**
	 * 设备IP地址
	 */
	String OPTLOG_DEVICE_HOST = "device_host";
	/**
	 * 服务器IP地址
	 */
	String OPTLOG_SERVICE_HOST = "service_host";
	/**
	 * 操作类型
	 */
	String OPTLOG_TYPE = "type";
	/**
	 * 操作人名称
	 */
	String OPTLOG_USER_NAME = "user_name";
	/**
	 * 操作人id
	 */
	String OPTLOG_USER_ID = "user_id";
	/**
	 * 操作时间
	 */
	String OPTLOG_DAYE_TIME = "date_time";
	/**
	 * 所操作的对象名称
	 */
	String OPTLOG_OBJECT_NAME = "object_name";
	/**
	 * 所操作的对象id
	 */
	String OPTLOG_OBJECT_ID = "object_id";
	/**
	 * 操作结果
	 */
	String OPTLOG_RESULT = "result";
	/**
	 * 操作详情
	 */
	String OPTLOG_CONTENT= "content";
	
	//操作类型定义
	/**
	 * 添加
	 */
	String OPTLOG_TYPE_ADD = "add";
	/**
	 * 修改
	 */
	String OPTLOG_TYPE_MODIFY = "modify";
	/**
	 * 删除
	 */
	String OPTLOG_TYPE_DELETE = "delete";
	/**
	 * 登录
	 */
	String OPTLOG_TYPE_LOGIN = "login";
	
	//操作结果定义
	/**
	 * 成功
	 */
	String OPRLOG_RESULT_SUCCESS = "success";
	/**
	 * 失败
	 */
	String OPTLOG_RESULT_FAILED = "failed";
}
