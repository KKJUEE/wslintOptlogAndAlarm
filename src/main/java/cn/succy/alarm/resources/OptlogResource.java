package cn.succy.alarm.resources;

/**
 * 存放日志的实体信息
 * @author ranzhonggeng
 *
 * 2018年11月13日
 */
public class OptlogResource {
	private static final long serialVersionUID = -6201793338937462437L;
	/**
	 * 操作id
	 */
	private String id;
	/**
	 * 操作名称
	 */
	private String name;
	/**
	 * 设备地址
	 */
	private String deviceHost;
	/**
	 * 服务器地址
	 */
	private String serviceHost;
	/**
	 * 操作类型（增删改登录。。。）
	 */
	private String type;
	/**
	 * 操作人名称
	 */
	private String userName;
	/**
	 * 操作人Id
	 */
	private String userId;
	/**
	 * 操作时间
	 */
	private String dateTime;
	/**
	 * 所操作的对象名称
	 */
	private String objectName;
	/**
	 * 所操作的对象id
	 */
	private String objectId;
	/**
	 * 操作结果
	 */
	private String result;
	/**
	 * 详细信息
	 */
	private String content;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeviceHost() {
		return deviceHost;
	}
	public void setDeviceHost(String deviceHost) {
		this.deviceHost = deviceHost;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getServiceHost() {
		return serviceHost;
	}
	public void setServiceHost(String serviceHost) {
		this.serviceHost = serviceHost;
	}
}
