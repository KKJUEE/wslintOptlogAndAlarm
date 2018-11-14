package cn.succy.alarm.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import cn.succy.alarm.common.OptlogConstant;
import cn.succy.alarm.dao.impl.AlarmContactDaoImpl;
import cn.succy.alarm.dao.impl.ContactStoreDaoImpl;
import cn.succy.alarm.dao.impl.OptLogServiceImpl;
import cn.succy.alarm.receiver.Contact;
import cn.succy.alarm.receiver.ContactGroup;
import cn.succy.alarm.receiver.GroupRelContact;
import cn.succy.alarm.resources.OptlogAddResource;
import cn.succy.alarm.resources.OptlogResource;
import cn.succy.alarm.sender.impl.EmailSenderImpl;

/**
 * 实现日志查询
 * @author ranzhonggeng
 *
 * 2018年11月12日
 */
public class AddOptlogServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(OptlogServlet.class);
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	this.doPost(req, resp);
    }
 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	try{
        	BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));

			String str = "";
			String wholeStr = "";
			while((str = reader.readLine()) != null){	//一行一行的读取body体里面的内容；
				wholeStr += str;
			}
			JSONObject optlogAddJson = JSONObject.parseObject(wholeStr);	//转化成json对象
			
	        /**
	         * 日志参数参数从param中拿
	         */
	        if(req.getMethod().equalsIgnoreCase("post")) {
	        	OptlogAddResource optlogAddResource = new OptlogAddResource();
	        	OptLogServiceImpl optLogServiceImpl = new OptLogServiceImpl();
				
				//设置日志ID，UUID方式生成
				String optlog_id = UUID.randomUUID().toString().replace("-", "").toLowerCase(); 
				//服务器ip
				InetAddress netAddress = getInetAddress();  
		        String optlog_ipAddress = getHostIp(netAddress);
		        //还需要设备IP...从param中拿到
		        
		        //用户信息，id及name，从session中获取
		        /**
		         * session.getAttribute("key");
		         */
		        
		        //当前时间
		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		        String currentDate = df.format(new Date());
				
				//解析post请求 
				optlogAddResource.setId(optlog_id);
				optlogAddResource.setName((String) optlogAddJson.get(OptlogConstant.OPTLOG_Name));
				optlogAddResource.setDeviceHost((String) optlogAddJson.get(OptlogConstant.OPTLOG_DEVICE_HOST));
				optlogAddResource.setServiceHost(optlog_ipAddress);
				optlogAddResource.setType((String) optlogAddJson.get(OptlogConstant.OPTLOG_TYPE));
				optlogAddResource.setUserName((String) optlogAddJson.get(OptlogConstant.OPTLOG_USER_NAME));
				optlogAddResource.setUserId((String) optlogAddJson.get(OptlogConstant.OPTLOG_USER_ID));
				optlogAddResource.setDateTime(currentDate);
				optlogAddResource.setObjectId((String) optlogAddJson.get(OptlogConstant.OPTLOG_OBJECT_ID));
				optlogAddResource.setObjectName((String) optlogAddJson.get(OptlogConstant.OPTLOG_OBJECT_NAME));
				optlogAddResource.setResult((String) optlogAddJson.get(OptlogConstant.OPTLOG_RESULT));
				optlogAddResource.setContent((String) optlogAddJson.get(OptlogConstant.OPTLOG_CONTENT));
	        	
				boolean isAddSuccess = optLogServiceImpl.handleOptlog(req, optlogAddResource);
				//创建JSON对象
	    	    JSONObject jsonObject = new JSONObject();
				if(isAddSuccess) {
					jsonObject.put("code", 200);
		    	    jsonObject.put("msg", "add optlog success");
		    	    logger.info("add optlog success");
	    		} else {
	    			jsonObject.put("code", 500);
		    	    jsonObject.put("msg", "add optlog failed");
		    	    logger.error("add optlog failed");
	    		}
		        resp.setContentType("text/html;charset=UTF-8");
		        PrintWriter out = resp.getWriter();
		        out.write(JSONArray.toJSON(jsonObject).toString());
		        out.flush();
		        out.close();
	        } 
	        
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("add optlog exception" + e);
		}
    }
    
    /**
	 * 获取主机信息
	 * @return InetAddress
	 */
    private InetAddress getInetAddress() {
    	try{  
            return InetAddress.getLocalHost();  
        }catch(UnknownHostException e){  
            System.out.println("unknown host!");  
        }  
        return null;
    	
    }
    /**
     * 获取主机IP
     * @param netAddress
     * @return String IP
     */
    private String getHostIp(InetAddress netAddress){  
        if(null == netAddress){  
            return null;  
        }  
        String ip = netAddress.getHostAddress(); //get the ip address  
        return ip;  
    }  
}
