package com.fh.controller.system.fhresource;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.util.Jurisdiction;
import com.fh.service.system.fhresource.FHresManager;

/** 
 * 说明：控制器和方法资源
 * 创建人：FH Q313596790
 * 创建时间：2016-05-10
 */
@Controller
@RequestMapping(value="/fhres")
public class FHresController extends BaseController {
	
	String menuUrl = "fhres/list.do"; //菜单地址(权限用)
	@Resource(name="fhresService")
	private FHresManager fhresService;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表FHresource");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<PageData>	varList = fhresService.list(page);		//列出FHresouce列表
		mv.setViewName("system/fhres/fhres_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());				//按钮权限
		return mv;
	}
	
}
