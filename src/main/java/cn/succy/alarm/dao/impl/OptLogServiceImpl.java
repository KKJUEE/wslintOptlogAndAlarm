package cn.succy.alarm.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import cn.succy.alarm.DB.DBconn;
import cn.succy.alarm.common.OptlogConstant;
import cn.succy.alarm.dao.OptLogService;
import cn.succy.alarm.receiver.Contact;
import cn.succy.alarm.resources.OptlogAddResource;
import cn.succy.alarm.resources.OptlogResource;
import cn.succy.alarm.resources.OptlogSearchResource;
import cn.succy.alarm.servlet.OptlogServlet;

/**
 * 日志接口实现
 * @author ranzhonggeng
 *
 * 2018年11月13日
 */
public class OptLogServiceImpl implements OptLogService{
	private static final Logger logger = LoggerFactory.getLogger(OptLogServiceImpl.class);
	
	private PreparedStatement ptmt = null;  
    private ResultSet rs = null;
    //获取所有日志
    @Override
    public List<OptlogResource> getOptlogAll() {
        // TODO Auto-generated method stub
        List<OptlogResource> list = new ArrayList<OptlogResource>();
        try {
            DBconn.init();
            ResultSet rs = DBconn.selectSql("select * from optlog");
//            System.out.println("111111111111" + System.getProperty("user.name")); 
            while(rs.next()){
            	OptlogResource optlogConstant=new OptlogResource();
            	optlogConstant.setId(rs.getString(OptlogConstant.OPTLOG_ID));
            	optlogConstant.setName(rs.getString(OptlogConstant.OPTLOG_Name));
            	optlogConstant.setDeviceHost(rs.getString(OptlogConstant.OPTLOG_DEVICE_HOST));
            	optlogConstant.setServiceHost(rs.getString(OptlogConstant.OPTLOG_SERVICE_HOST));
            	optlogConstant.setType(rs.getString(OptlogConstant.OPTLOG_TYPE));
            	optlogConstant.setObjectId(rs.getString(OptlogConstant.OPTLOG_OBJECT_ID));
            	optlogConstant.setObjectName(rs.getString(OptlogConstant.OPTLOG_OBJECT_NAME));
            	optlogConstant.setDateTime(rs.getString(OptlogConstant.OPTLOG_DAYE_TIME));
            	optlogConstant.setUserName(rs.getString(OptlogConstant.OPTLOG_USER_NAME));
            	optlogConstant.setUserId(rs.getString(OptlogConstant.OPTLOG_USER_ID));
            	optlogConstant.setResult(rs.getString(OptlogConstant.OPTLOG_RESULT));
            	optlogConstant.setContent(rs.getString(OptlogConstant.OPTLOG_CONTENT));
            	
                list.add(optlogConstant);
            }
            DBconn.closeConn();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
  	/**
  	 * 日志条件查询
  	 * @param optlogSearch
  	 * @return OptlogResource List
  	 */
    @Override
    public List<OptlogResource> getOptlogSearch(OptlogSearchResource optlogSearch) {
        // TODO Auto-generated method stub
        List<OptlogResource> list = new ArrayList<OptlogResource>();
        try {
            DBconn.init();
            String sql = "select * from optlog";
            String searchInfo = "";
            if(optlogSearch.getName() != null && optlogSearch.getName().length() != 0) {
            	searchInfo += " name like \"%" +optlogSearch.getName()+ "%\"";
            }
            if(optlogSearch.getBeginTime() != null && optlogSearch.getBeginTime().length() != 0 && optlogSearch.getEndTime() != null && optlogSearch.getEndTime().length() != 0) {
            	searchInfo += " date_time between '" +optlogSearch.getBeginTime() + "' and '"+ optlogSearch.getEndTime() + "'";
            }
            
            if(searchInfo.length() != 0) {
            	sql += " where" + searchInfo;
            }
            logger.debug(sql);
            ResultSet rs = DBconn.selectSql(sql);
            while(rs.next()){
            	OptlogResource optlogConstant=new OptlogResource();
            	optlogConstant.setId(rs.getString(OptlogConstant.OPTLOG_ID));
            	optlogConstant.setName(rs.getString(OptlogConstant.OPTLOG_Name));
            	optlogConstant.setDeviceHost(rs.getString(OptlogConstant.OPTLOG_DEVICE_HOST));
            	optlogConstant.setServiceHost(rs.getString(OptlogConstant.OPTLOG_SERVICE_HOST));
            	optlogConstant.setType(rs.getString(OptlogConstant.OPTLOG_TYPE));
            	optlogConstant.setObjectId(rs.getString(OptlogConstant.OPTLOG_OBJECT_ID));
            	optlogConstant.setObjectName(rs.getString(OptlogConstant.OPTLOG_OBJECT_NAME));
            	optlogConstant.setDateTime(rs.getString(OptlogConstant.OPTLOG_DAYE_TIME));
            	optlogConstant.setUserName(rs.getString(OptlogConstant.OPTLOG_USER_NAME));
            	optlogConstant.setUserId(rs.getString(OptlogConstant.OPTLOG_USER_ID));
            	optlogConstant.setResult(rs.getString(OptlogConstant.OPTLOG_RESULT));
            	optlogConstant.setContent(rs.getString(OptlogConstant.OPTLOG_CONTENT));
            	
                list.add(optlogConstant);
            }
            DBconn.closeConn();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //添加日志DB
    public boolean addOptlog(OptlogResource optlogResource) {
        // TODO Auto-generated method stub
    	
    	/**
    	 * 获取当前用户id
    	 */
    	
        boolean flag = false;
        DBconn.init();
        int i =DBconn.addUpdDel("insert into optlog(id,name,device_host,service_host,type,user_name,user_id,date_time,object_name,object_id,result,content) " +
                "values('"+optlogResource.getId() + "','"
        		+ optlogResource.getName() + "','"
                + optlogResource.getDeviceHost() + "','"
        		+ optlogResource.getServiceHost() + "','"
        		+ optlogResource.getType() + "','"
        		+ optlogResource.getUserName() + "','"
				+ optlogResource.getUserId() + "','"
        		+ optlogResource.getDateTime() + "','"
        		+ optlogResource.getObjectName() + "','"
        		+ optlogResource.getObjectId() + "','"
				+ optlogResource.getResult() + "','"
        		+ optlogResource.getContent() + "')");
        if(i>0){
            flag = true;
        }
        DBconn.closeConn();
        return flag;
    }
    
    //处理外部的添加日志
    /**
     * 
     * @param optlogResource	为扩展详细信息，获取参数
     * @param optlogAddResource		添加日志时的参数模板
     * @return
     * @throws IOException 
     */
    @Override
    public boolean handleOptlog(HttpServletRequest req, OptlogAddResource optlogAddResource) throws IOException {
        // TODO Auto-generated method stub
        boolean flag = false;
        DBconn.init();
        BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String str = "";
		String wholeStr = "";
		while((str = reader.readLine()) != null){	//一行一行的读取body体里面的内容；
			wholeStr += str;
		}
		System.out.println("wholeStr:"+wholeStr);
		JSONObject  contactJson = JSONObject.parseObject(wholeStr);	//转化成json对象
        int i =DBconn.addUpdDel("insert into optlog(id,name,device_host,service_host,type,user_name,user_id,date_time,object_name,object_id,result,content) " +
                "values('"+optlogAddResource.getId() + "','"
        		+ optlogAddResource.getName() + "','"
                + optlogAddResource.getDeviceHost() + "','"
        		+ optlogAddResource.getServiceHost() + "','"
        		+ optlogAddResource.getType() + "','"
        		+ optlogAddResource.getUserName() + "','"
				+ optlogAddResource.getUserId() + "','"
        		+ optlogAddResource.getDateTime() + "','"
        		+ optlogAddResource.getObjectName() + "','"
        		+ optlogAddResource.getObjectId() + "','"
				+ optlogAddResource.getResult() + "','"
        		+ optlogAddResource.getContent() + "')");
        if(i>0){
            flag = true;
        }
        DBconn.closeConn();
        return flag;
    }
}
