package cn.succy.alarm.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.json.JSONException;
import cn.succy.alarm.common.Constant;
import cn.succy.alarm.dao.impl.AlarmContactDaoImpl;
import cn.succy.alarm.dao.impl.ContactStoreDaoImpl;
import cn.succy.alarm.receiver.Contact;
import cn.succy.alarm.receiver.ContactGroup;
import cn.succy.alarm.receiver.GroupRelContact;

/**
 * 实现web联系人配置
 * @author ranzhonggeng
 *
 * 2018年11月10日
 */
public class ContactServlet extends HttpServlet{
private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	try {
    		AlarmContactDaoImpl alarmContactDaoImpl = new AlarmContactDaoImpl();
    		List<Contact> contactAll = new ArrayList<Contact>();
    		StringBuffer url = req.getRequestURL();
    		//查看是否存在名称参数
    		if(req.getQueryString() != null && req.getQueryString().length() != 0) {
    			System.out.println("get one!!!");
    			System.out.println("get req!!!" + req.getQueryString().toString());
//    			JSONObject jsStr = JSONObject.parseObject(req.getQueryString().toString());
    			String wechat = req.getQueryString().toString().split("=")[1].replace("&", "");
    			System.out.println("get one!!! " + wechat);
//    					(String) jsStr.get(Constant.FIELD_NAME);
    			contactAll = alarmContactDaoImpl.getContactSearch(wechat);
    		} else {
    			System.out.println("get all!!!");
    			contactAll= alarmContactDaoImpl.getContactAll();
    		}

        	List<ContactGroup> contactGroup= alarmContactDaoImpl.getGroupAll();
        	List<GroupRelContact> groupRelContact= alarmContactDaoImpl.getGroupRelContact();
        	
        	//创建JSON对象
    	    JSONObject jsonObject = new JSONObject();
    	    
    	    jsonObject.put("code", 200);
    	    jsonObject.put("contact", contactAll);
    	    jsonObject.put("contact_group", contactGroup);
    	    jsonObject.put("group_rel_contact", groupRelContact);
    	    
    	    
    	    System.out.println(jsonObject);
        	System.out.println("2");
	        resp.setContentType("text/html;charset=UTF-8");
	        PrintWriter out = resp.getWriter();
	        
	        out.write(JSONArray.toJSON(jsonObject).toString());
	        out.flush();
	        out.close();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("********** begin contact request *************");
    	
		try{
        	ContactStoreDaoImpl contactStoreDaoImpl = new ContactStoreDaoImpl();
			Contact contact = new Contact();
			ContactGroup contactGroup = new ContactGroup();
			
        	BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));

			String str = "";
			String wholeStr = "";
			while((str = reader.readLine()) != null){	//一行一行的读取body体里面的内容；
				wholeStr += str;
			}
			System.out.println("wholeStr:"+wholeStr);
			JSONObject  contactJson = JSONObject.parseObject(wholeStr);	//转化成json对象
			//解析post请求 
			contact.setContactId((String) contactJson.get(Constant.FIELD_CONTACT_ID));
			contact.setName((String) contactJson.get(Constant.FIELD_NAME));
			contact.setEmail((String) contactJson.get(Constant.FIELD_EMAIL));
			contact.setPhone((String) contactJson.get(Constant.FIELD_PHONE));
			contact.setWechat((String) contactJson.get(Constant.FIELD_WECHAT));
    		contactGroup.setGroupId((String) contactJson.get(Constant.FIELD_GROUP_ID));
        	
    		contactStoreDaoImpl.addContact(contact);
    		contactStoreDaoImpl.relateContactAndGroup(contact, contactGroup);
    		
        	System.out.println("2");
	        resp.setContentType("text/html;charset=UTF-8");
	        System.out.println("GET CONTACT RETURN");
	        PrintWriter out = resp.getWriter();
	        System.out.println(resp);
	        out.write("add success");
	        out.flush();
	        out.close();
	        
		} catch(Exception e) {
			
		}
    }
}
