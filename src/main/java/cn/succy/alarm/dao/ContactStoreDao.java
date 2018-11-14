package cn.succy.alarm.dao;

import cn.succy.alarm.receiver.Contact;
import cn.succy.alarm.receiver.ContactGroup;

/**
 * 提供web端配置告警通知联系人
 * @author ranzhonggeng
 *
 * 2018年11月10日
 */
public interface ContactStoreDao {
	
	/**
	 * 添加企业群组
	 * @param contactGroup
	 * @return boolean
	 */
	public boolean addContactGroup(ContactGroup contactGroup);
	
	/**
	 * 添加联系人
	 * @param contact
	 * @return boolean
	 */
	public boolean addContact(Contact contact);
	
	/**
	 * 添加联系人-群组关联
	 * @param contact
	 * @param contactGroup
	 * @return boolean
	 */
	public boolean relateContactAndGroup(Contact contact, ContactGroup contactGroup);

}
