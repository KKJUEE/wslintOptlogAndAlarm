package cn.succy.alarm.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.succy.alarm.DB.DBconn;
import cn.succy.alarm.dao.AlarmContactDao;
import cn.succy.alarm.receiver.Contact;
import cn.succy.alarm.receiver.ContactGroup;
import cn.succy.alarm.receiver.GroupRelContact;
 

/**数据库操作接口实现
 * 联系人相关查询接口、更新与删除功能
 * @author ranzhonggeng
 *
 * 2018年11月9日
 */
 
public class AlarmContactDaoImpl implements AlarmContactDao {
    private PreparedStatement ptmt = null;  
    private ResultSet rs = null;
    //获取所有联系人
    @Override
    public List<Contact> getContactAll() {
        // TODO Auto-generated method stub
        List<Contact> list = new ArrayList<Contact>();
        try {
            DBconn.init();
            ResultSet rs = DBconn.selectSql("select * from contact");
            while(rs.next()){
            	Contact contact=new Contact();
            	contact.setContactId(rs.getString("contact_id"));
            	contact.setName(rs.getString("name"));
            	contact.setEmail(rs.getString("email"));
            	contact.setPhone(rs.getString("phone"));
            	contact.setWechat(rs.getString("wechat"));
                list.add(contact);
            }
            DBconn.closeConn();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
        
    }
    //根据名称获取联系人
    @Override
    public List<Contact> getContactSearch(String wechat) {
        // TODO Auto-generated method stub
        List<Contact> list = new ArrayList<Contact>();
        try {
            DBconn.init();
            String sql = "select * from contact" + 
            		" where wechat like \"%" +wechat+ "%\"";
            System.out.println(sql);
            ResultSet rs = DBconn.selectSql(sql);
            
            while(rs.next()){
            	Contact contact=new Contact();
            	contact.setContactId(rs.getString("contact_id"));
            	contact.setName(rs.getString("name"));
            	contact.setEmail(rs.getString("email"));
            	contact.setPhone(rs.getString("phone"));
            	contact.setWechat(rs.getString("wechat"));
                list.add(contact);
            }
            DBconn.closeConn();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
        
    }
    //获取群组
    @Override
    public List<ContactGroup> getGroupAll() {
    	List<ContactGroup> groupList = new ArrayList<ContactGroup>();
    	try {
            DBconn.init();
            ResultSet rs = DBconn.selectSql("select * from contact_group");
            while(rs.next()){
            	ContactGroup contact_group = new ContactGroup();
            	contact_group.setGroupId(rs.getString("group_id"));
            	contact_group.setGroupName(rs.getString("group_name"));
            	groupList.add(contact_group);
            }
            DBconn.closeConn();
            return groupList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return null;
    }
    //获取联系人-群组关联
    @Override
    public List<GroupRelContact> getGroupRelContact() {
    	List<GroupRelContact> groupRelContact = new ArrayList<GroupRelContact>();
    	try {
            DBconn.init();
            ResultSet rs = DBconn.selectSql("select * from group_rel_contact");
            while(rs.next()){
            	GroupRelContact group_rel_contact = new GroupRelContact();
            	group_rel_contact.setGroupId(rs.getString("group_id"));
            	group_rel_contact.setContactId(rs.getString("contact_id"));
            	groupRelContact.add(group_rel_contact);
            }
            DBconn.closeConn();
            return groupRelContact;
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return null;
    }
    
    
    //添加联系人
    @Override
    public boolean addContact(Contact contact) {
        // TODO Auto-generated method stub
        boolean flag = false;
        DBconn.init();
        int i =DBconn.addUpdDel("insert into contact(contact_id,name,email,phone,wechat) " +
                "values('"+contact.getContactId()+"','"+contact.getName()+"','"+contact.getEmail()+"','"+contact.getPhone()+"','"+contact.getWechat()+"')");
        if(i>0){
            flag = true;
        }
        DBconn.closeConn();
        return flag;
    }
    
    //删除联系人
    @Override
    public boolean deleteContact(String contactId) {
        // TODO Auto-generated method stub
        boolean flag = false;
        DBconn.init();
        String sql = "delete from contact where contact_id="+"'" + contactId + "'";
        
        System.out.println(sql);
        int i =DBconn.addUpdDel(sql);
        if(i>0){
            flag = true;
        }
        DBconn.closeConn();
        return flag;
    }
    
  //删除关联关系
    @Override
    public boolean deleteRelation(String contactId) {
        // TODO Auto-generated method stub
        boolean flag = false;
        DBconn.init();
        String sql = "delete from group_rel_contact where contact_id="+ "'" + contactId + "'";
        System.out.println(sql);
        int i =DBconn.addUpdDel(sql);
        if(i>0){
            flag = true;
        }
        DBconn.closeConn();
        return flag;
    }
    
    //更新联系人
    @Override
    public boolean updateContact(Contact contact) {
		boolean flag = false;
	    DBconn.init();
	    String sql = "update contact set name= '" + contact.getName() + "'," + 
	    		"email= '" + contact.getEmail() + "'," + 
	    		"phone= '" + contact.getPhone() + "'," + 
	    		"wechat= '" + contact.getWechat() + 
	    		"' where contact_id = '" + contact.getContactId() + "'";
	    System.out.println(sql);
	    int i =DBconn.addUpdDel(sql);
	    if(i>0){
	        flag = true;
	    }
	    DBconn.closeConn();
	    return flag;
    }
    
    //更新组与联系人关联
    @Override
    public boolean updateRelation(Contact contact, ContactGroup contactGroup) throws SQLException {
		boolean flag = false;
	    DBconn.init();
	    String sql = "";
	    //需要判断关联关系表中是否存在关联
	    if(isExistRelation(contact)) {
	    	sql = "update group_rel_contact set group_id ='" + contactGroup.getGroupId() + 
	    			"' where contact_id = '" + contact.getContactId() +"'";
	    } else {
	    	sql = "insert into group_rel_contact(contact_id,group_id) " +
	                "values('"+contact.getContactId() + "','" + contactGroup.getGroupId() + "')";
	    }
	    int i =DBconn.addUpdDel(sql);
	    if(i>0){
	        flag = true;
	    }
	    DBconn.closeConn();
	    return flag;
    }
    
  //判断关联关系表中是否存在关联
    private boolean isExistRelation(Contact contact) throws SQLException {
	    DBconn.init();
	    
	    String exit_sql = "select * from group_rel_contact " +
	    		"where contact_id = '"+ contact.getContactId() +"'";
	    System.out.println("group_id " + exit_sql);
	    
	    ResultSet rs = DBconn.selectSql(exit_sql);
        while(rs.next()){
        	if(rs.getString("group_id").length() == 0) {
        		return false;
        	} else {
        		return true;
        	}
        }
		return false;
    }
}
