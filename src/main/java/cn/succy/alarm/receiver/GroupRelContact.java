package cn.succy.alarm.receiver;

/**
 * 联系人-关联群组实体类
 * @author ranzhonggeng
 *
 * 2018年11月12日
 */
public class GroupRelContact {
	private String groupId;
    private String contactId;
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
}
