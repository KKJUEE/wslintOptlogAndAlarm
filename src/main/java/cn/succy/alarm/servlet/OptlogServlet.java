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
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.json.JSONException;
import cn.succy.alarm.common.Constant;
import cn.succy.alarm.dao.impl.AlarmContactDaoImpl;
import cn.succy.alarm.dao.impl.ContactStoreDaoImpl;
import cn.succy.alarm.dao.impl.OptLogServiceImpl;
import cn.succy.alarm.receiver.Contact;
import cn.succy.alarm.receiver.ContactGroup;
import cn.succy.alarm.receiver.GroupRelContact;
import cn.succy.alarm.resources.OptlogResource;
import cn.succy.alarm.resources.OptlogSearchResource;
import cn.succy.alarm.sender.impl.EmailSenderImpl;

/**
 * 实现日志查询
 * @author ranzhonggeng
 *
 * 2018年11月12日
 */
public class OptlogServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(OptlogServlet.class);
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	try {
    		
    		logger.debug("********** optlog get request *************");
        	BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));

			String str = "";
			String wholeStr = "";
			while((str = reader.readLine()) != null){	//一行一行的读取body体里面的内容；
				wholeStr += str;
			}
			JSONObject  contactJson = JSONObject.parseObject(wholeStr);	//转化成json对象
			
			OptLogServiceImpl optLogServiceImpl = new OptLogServiceImpl();
			List<OptlogResource> optlogResource = new ArrayList<OptlogResource>();
			
    		StringBuffer url = req.getRequestURL();
//    		//查看是否存在名称参数
    		OptlogSearchResource optlogSearch = new OptlogSearchResource();
    		if(req.getQueryString() != null && req.getQueryString().length() != 0) {
    			//存在条件查询
    			optlogSearch.setName(req.getParameter("name"));
    			optlogSearch.setBeginTime(req.getParameter("beginTime"));
    			optlogSearch.setEndTime(req.getParameter("endTime"));
    			optlogResource= optLogServiceImpl.getOptlogSearch(optlogSearch);
    		} else {
    			//查询所有
    			optlogResource= optLogServiceImpl.getOptlogAll();
    		}
    		
        	//创建JSON对象
    	    JSONObject jsonObject = new JSONObject();
    	    
    	    jsonObject.put("code", 200);
    	    jsonObject.put("log", optlogResource);
	        resp.setContentType("text/html;charset=UTF-8");
	        PrintWriter out = resp.getWriter();
	        out.write(JSONArray.toJSON(jsonObject).toString());
	        out.flush();
	        out.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
	        	
	    		boolean isAddContact = contactStoreDaoImpl.addContact(contact);
	    		boolean isAddRelation = contactStoreDaoImpl.relateContactAndGroup(contact, contactGroup);
	    		
	    		//创建JSON对象
	    	    JSONObject jsonObject = new JSONObject();
	    	    
	    	    if(isAddContact && isAddRelation) {
	    	    	jsonObject.put("code", 200);
		    	    jsonObject.put("msg", "add contact and relations success");
	    	    } else {
	    	    	jsonObject.put("code", 500);
		    	    jsonObject.put("msg", "add contact and relations failed");
		    	    //有回滚操作
	    	    }
	    	    
	    	    
	    		
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
