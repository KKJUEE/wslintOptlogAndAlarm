package cn.succy.alarm.dao;

import java.sql.SQLException;
import java.util.List;

import cn.succy.alarm.receiver.Contact;
import cn.succy.alarm.receiver.ContactGroup;
import cn.succy.alarm.receiver.GroupRelContact;
 
/**
 * 联系人基础操作写入接口
 * @author ranzhonggeng
 *
 */
public interface AlarmContactDao {
	/**
	 * 获取所有联系人
	 * @return Contact联系人列表
	 */
    public List<Contact> getContactAll();
    /**
     * 添加一天联系人
     * @param contactCase
     * @return boolean
     */
    public boolean addContact(Contact contactCase);
    /**
     * 删除联系人
     * @param id
     * @return boolean
     */
    public boolean deleteContact(String id) ;
    /**
     * 修改联系人信息
     * @param contactCase
     * @return boolean
     */
    public boolean updateContact(Contact contactCase);
    /**
     * 获取企业群组列表
     * @return ContactGroup List
     */
    public List<ContactGroup> getGroupAll();
    /**
     * 获取联系人-企业群组关联
     * @return group_rel_contact List
     */
    public List<GroupRelContact> getGroupRelContact();
    /**
     * 更新联系人-企业群组关联
     * @param contact
     * @param contactGroup
     * @return boolean
     * @throws SQLException
     */
    public boolean updateRelation(Contact contact, ContactGroup contactGroup) throws SQLException;
    /**
     * 删除联系人-企业群组关联
     * @param contactId
     * @return boolean
     */
    public boolean deleteRelation(String contactId);
    /**
     * 根据微信Id模糊查询
     * @param contact_name
     * @return Contact List
     */
    public List<Contact> getContactSearch(String contact_name);
 
}
