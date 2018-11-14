package cn.succy.alarm.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

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
 * 实现web联系人增、删、改
 * @author ranzhonggeng
 *
 * 2018年11月12日
 */
public class ContactOperation extends HttpServlet{
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	this.doPost(req, resp);
    }
 
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
    	            IOException {
    	try {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));

			String str = "";
			String wholeStr = "";
			while((str = reader.readLine()) != null){	//一行一行的读取body体里面的内容；
				wholeStr += str;
			}
			JSONObject  contactJson = JSONObject.parseObject(wholeStr);	//转化成json对象
			
			AlarmContactDaoImpl alarmContactDaoImpl = new AlarmContactDaoImpl();
			System.out.println((String) contactJson.get(Constant.FIELD_CONTACT_ID));
        	alarmContactDaoImpl.deleteContact((String)contactJson.get(Constant.FIELD_CONTACT_ID));
        	alarmContactDaoImpl.deleteRelation((String)contactJson.get(Constant.FIELD_CONTACT_ID));

        	//创建JSON对象
    	    JSONObject jsonObject = new JSONObject();
    	    
    	    jsonObject.put("code", 200);
    	    jsonObject.put("msg", "delete success");
    		
	        resp.setContentType("text/html;charset=UTF-8");
	        PrintWriter out = resp.getWriter();
	        out.write(JSONArray.toJSON(jsonObject).toString());
        	
	        out.flush();
	        out.close();
    	} catch(Exception e) {
    		
    	}
    	
	}
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
    	Contact contact = new Contact();
		
    	BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));

		String str = "";
		String wholeStr = "";
		while((str = reader.readLine()) != null){	//一行一行的读取body体里面的内容；
			wholeStr += str;
		}
		JSONObject  contactJson = JSONObject.parseObject(wholeStr);	//转化成json对象
		AlarmContactDaoImpl alarmContactDaoImpl = new AlarmContactDaoImpl();
    	ContactGroup contactGroup = new ContactGroup();
    	
    	try {
    		//解析post请求 
    		contact.setContactId((String) contactJson.get(Constant.FIELD_CONTACT_ID));
    		System.out.println((String) contactJson.get(Constant.FIELD_CONTACT_ID));
    		contact.setName((String) contactJson.get(Constant.FIELD_NAME));
    		contact.setEmail((String) contactJson.get(Constant.FIELD_EMAIL));
    		contact.setPhone((String) contactJson.get(Constant.FIELD_PHONE));
    		contact.setWechat((String) contactJson.get(Constant.FIELD_WECHAT));
    		contactGroup.setGroupId((String) contactJson.get(Constant.FIELD_GROUP_ID));
    		
    		alarmContactDaoImpl.updateContact(contact);
    		alarmContactDaoImpl.updateRelation(contact, contactGroup);
    		
    		
    		//创建JSON对象
    	    JSONObject jsonObject = new JSONObject();
    	    
    	    jsonObject.put("code", 200);
    	    jsonObject.put("msg", "modify success");
    		
            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.write(JSONArray.toJSON(jsonObject).toString());
            out.flush();
            out.close();
    	} catch(SQLException e) {
    		// TODO Auto-generated catch block
			e.printStackTrace();
    	}
    	
	}
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("********** contact operation request *************");
    	
		try{
			Contact contact = new Contact();
			
        	BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));

			String str = "";
			String wholeStr = "";
			while((str = reader.readLine()) != null){	//一行一行的读取body体里面的内容；
				wholeStr += str;
			}
			JSONObject  contactJson = JSONObject.parseObject(wholeStr);	//转化成json对象
			
		
			//增加
	        if(req.getMethod().equalsIgnoreCase("post")) {
	        	ContactStoreDaoImpl contactStoreDaoImpl = new ContactStoreDaoImpl();
				ContactGroup contactGroup = new ContactGroup();
				
				//设置用户ID，UUID方式生成
				String contact_id = UUID.randomUUID().toString().replace("-", "").toLowerCase(); 
				//解析post请求 
				contact.setContactId(contact_id);
				contact.setName((String) contactJson.get(Constant.FIELD_NAME));
				contact.setEmail((String) contactJson.get(Constant.FIELD_EMAIL));
				contact.setPhone((String) contactJson.get(Constant.FIELD_PHONE));
				contact.setWechat((String) contactJson.get(Constant.FIELD_WECHAT));
	    		contactGroup.setGroupId((String) contactJson.get(Constant.FIELD_GROUP_ID));
	        	
	    		contactStoreDaoImpl.addContact(contact);
	    		contactStoreDaoImpl.relateContactAndGroup(contact, contactGroup);
	    		
	    		//创建JSON对象
	    	    JSONObject jsonObject = new JSONObject();
	    	    
	    	    jsonObject.put("code", 200);
	    	    jsonObject.put("msg", "add success");
	    		
		        resp.setContentType("text/html;charset=UTF-8");
		        PrintWriter out = resp.getWriter();
		        out.write(JSONArray.toJSON(jsonObject).toString());
		        out.flush();
		        out.close();
	        } 
	        
		} catch(Exception e) {
			
		}
    }
}
