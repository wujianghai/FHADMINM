package com.fh.service.system.fhresource;

import java.util.List;

import com.fh.entity.Page;
import com.fh.util.PageData;

/** 
 * 说明： 操作日志记录接口
 * 创建人：FH Q313596790
 * 创建时间：2016-05-10
 * @version
 */
public interface FHresManager{

	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
}

