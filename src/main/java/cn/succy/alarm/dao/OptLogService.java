package cn.succy.alarm.dao;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.succy.alarm.common.OptlogConstant;
import cn.succy.alarm.resources.OptlogAddResource;
import cn.succy.alarm.resources.OptlogResource;
import cn.succy.alarm.resources.OptlogSearchResource;

/**
 * 提供日志接口
 * @author ranzhonggeng
 *
 * 2018年11月13日
 */
public interface OptLogService {

	/**
	 * 获取所有的日志信息
	 * @return OptlogConstant List
	 */
	List<OptlogResource> getOptlogAll();
	
	/**
	 * 外部调用添加日志
	 * @param req
	 * @param optlogAddResource
	 * @return boolean
	 * @throws IOException 
	 */
	boolean handleOptlog(HttpServletRequest req, OptlogAddResource optlogAddResource) throws IOException;

	List<OptlogResource> getOptlogSearch(OptlogSearchResource optlogSearch);
	
}
