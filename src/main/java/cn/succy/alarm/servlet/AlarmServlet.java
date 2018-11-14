package cn.succy.alarm.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import cn.succy.alarm.common.Constant;
import cn.succy.alarm.common.Level;
import cn.succy.alarm.config.AlarmConfig;
import cn.succy.alarm.config.ConfigManager;
import cn.succy.alarm.dao.impl.AlarmReceiverDaoImpl;
import cn.succy.alarm.resources.AlarmResource;
import cn.succy.alarm.template.TemplateModel;
//import net.sf.json.JSONObject;
 
 /**
  * 拦截器
  * @author ranzhonggeng
  *
  */
 
public class AlarmServlet extends HttpServlet {
 
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	this.doPost(req, resp);
    }
 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("********** begin request *************");
    	
		try{
			
			AlarmReceiverDaoImpl alarmResourceDaoImpl = new AlarmReceiverDaoImpl();
			AlarmResource alarmRes = new AlarmResource();
			TemplateModel model = new TemplateModel();
			
			//获取请求方法
	        System.out.println("Method:"+req.getMethod());
	        System.out.println("URL:"+req.getRequestURL() + " URI:"+ req.getRequestURI());
	        //请求的协议版本比如http1.1
	        System.out.println("httpProtocolVersion:" + req.getProtocol());
	        //请求头字段名称
	        @SuppressWarnings("unchecked")
	        Enumeration<String> enumeration = (Enumeration<String>)req.getHeaderNames();
	        while (enumeration.hasMoreElements()) {
	            String headerName = enumeration.nextElement();
	            System.out.println("(Header) " + headerName + " : " + req.getHeader(headerName));
	        }
	        //获取实体内容
	        System.out.println("实体内容"+req.getInputStream());
			//处理get和post请求
	        if(req.getMethod().equalsIgnoreCase("get")) {
	        	String resource = alarmResourceDaoImpl.getAlarmResource();
	        	System.out.println("2");
		        resp.setContentType("text/html;charset=UTF-8");
		        System.out.println("GET RETURN");
		        PrintWriter out = resp.getWriter();
		        System.out.println(resp);
		        out.write(resource);
		        out.flush();
		        out.close();
	        } else {
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));

				String str = "";
				String wholeStr = "";
				while((str = reader.readLine()) != null){//一行一行的读取body体里面的内容；
					wholeStr += str;
				}
				System.out.println("wholeStr:"+wholeStr);
				JSONObject  alarmParams = JSONObject.parseObject(wholeStr);//转化成json对象
				System.out.println("alarmParams:"+alarmParams);
		        //时间处理
		        String date = (String) alarmParams.get(Constant.FIELD_ALARM_DATE_TIME);
		        System.out.println("date before:"+date);
		        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // your template here
		        Date dateStr = formatter.parse(date);
		        System.out.println("date dealing:"+dateStr);
		        java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());
		        System.out.println("date after:"+dateDB);
		        
		        //解析post请求 
		        alarmRes.setId((String) alarmParams.get(Constant.FIELD_ALARM_ID));
	    		alarmRes.setAlarmName((String) alarmParams.get(Constant.FIELD_ALARM_NAME));
	    		alarmRes.setAppName((String) alarmParams.get(Constant.FIELD_ALARM_APP_NAME));
	    		alarmRes.setLevel((String) alarmParams.get(Constant.FIELD_ALARM_LEVEL));
	    		alarmRes.setHost((String) alarmParams.get(Constant.FIELD_ALARM_HOST));
	    		alarmRes.setDateTime(dateDB);
	    		alarmRes.setContent((String) alarmParams.get(Constant.FIELD_ALARM_CONTENT));
	    		alarmRes.setTraceStack((String) alarmParams.get(Constant.FIELD_ALARM_TRACE_STACK));
	    		alarmRes.setException((String) alarmParams.get(Constant.FIELD_ALARM_EXCEPTION));
	    		alarmRes.setStatus((String) alarmParams.get(Constant.FIELD_ALARM_STATUS));
	    		
	    		model.setAlarmName((String) alarmParams.get(Constant.FIELD_ALARM_NAME));
	            model.setAppName((String) alarmParams.get(Constant.FIELD_ALARM_APP_NAME));
	            model.setHost((String) alarmParams.get(Constant.FIELD_ALARM_HOST));
	            model.setDateTime(dateDB);
	            model.setContent((String) alarmParams.get(Constant.FIELD_ALARM_CONTENT));
	            model.setTraceStack((String) alarmParams.get(Constant.FIELD_ALARM_TRACE_STACK));
	            model.setContent((String) alarmParams.get(Constant.FIELD_ALARM_CONTENT));
	            
	            switch((String) alarmParams.get(Constant.FIELD_ALARM_LEVEL)) {
		        	case "debug": 
		        		model.setLevel(Level.DEBUG);
		        		break;
		        	case "info": 
		        		model.setLevel(Level.INFO);
		        		break;
		        	case "warn": 
		        		model.setLevel(Level.WARN);
		        		break;
		        	case "error": 
		        		model.setLevel(Level.ERROR);
		        		break;
		        }
		        
		        System.out.println(model.getLevel());
		        
		        //发送实体
		        alarmResourceDaoImpl.sendAlarm(model);
	            //存储实体
		        alarmResourceDaoImpl.addAlarmResource(alarmRes);

		        System.out.println("1");
		        resp.setContentType("text/html;charset=UTF-8");
		        System.out.println("POST RETURN");
		        PrintWriter out = resp.getWriter();
		        System.out.println(resp);
		        out.println("it is OK");
		        out.flush();
		        out.close();
	        }
		} catch(Exception e) {
			
		}
    }
 
}

