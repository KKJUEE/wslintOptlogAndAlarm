package cn.succy.alarm.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.succy.alarm.DB.DBconn;
import cn.succy.alarm.dao.ContactStoreDao;
import cn.succy.alarm.receiver.Contact;
import cn.succy.alarm.receiver.ContactGroup;
import cn.succy.alarm.resources.AlarmResource;

/**
 * 告警联系人配置实现接口
 * @author ranzhonggeng
 *
 * 2018年11月10日
 */
public class ContactStoreDaoImpl implements ContactStoreDao{
	private static final Logger logger = LoggerFactory.getLogger(AlarmReceiverDaoImpl.class);
	
	private PreparedStatement ptmt = null;  
    private ResultSet rs = null;
	@Override
	//1、添加组
    public boolean addContactGroup(ContactGroup contactGroup) {
        // TODO Auto-generated method stub
        boolean flag = false;
        DBconn.init();
        int i =DBconn.addUpdDel("insert into contact_group(group_id,group_name) " +
                "values('"+contactGroup.getGroupId() + "','" + contactGroup.getGroupName()+"')");
        if(i>0){
            flag = true;
        }
        System.out.println("addContactGroup success!");
        DBconn.closeConn();
        return flag;
    }
	
	@Override
	//2、添加联系人
    public boolean addContact(Contact contact) {
        // TODO Auto-generated method stub
        boolean flag = false;
        DBconn.init();
        int i =DBconn.addUpdDel("insert into contact(contact_id,name,email,phone,wechat) " +
                "values('"+contact.getContactId() + "','" + contact.getName() + "','" + contact.getEmail() + "','" + contact.getPhone() + "','" + contact.getWechat() + "')");
        if(i>0){
            flag = true;
        }
        System.out.println("addContact success!");
        DBconn.closeConn();
        return flag;
    }
	
	
	@Override
	//3、同时添加了联系人和组群，需要将他们关联
    public boolean relateContactAndGroup(Contact contact, ContactGroup contactGroup) {
        // TODO Auto-generated method stub
        boolean flag = false;
        DBconn.init();
        int i =DBconn.addUpdDel("insert into group_rel_contact(group_id,contact_id) " +
                "values('"+contactGroup.getGroupId() + "','" + contact.getContactId() + "')");
        if(i>0){
            flag = true;
        }
        System.out.println("addContact success!");
        DBconn.closeConn();
        return flag;
    }
}
