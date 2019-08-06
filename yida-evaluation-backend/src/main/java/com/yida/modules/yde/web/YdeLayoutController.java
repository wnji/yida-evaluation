/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yida.modules.yde.entity.YdeModule;
import com.yida.modules.yde.service.YdeModuleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.yida.modules.yde.entity.YdeLayout;
import com.yida.modules.yde.service.YdeLayoutService;

import java.util.List;

/**
 * 展示模式Controller
 * @author Kevin
 * @version 2019-05-02
 */
@Controller
@RequestMapping(value = "${adminPath}/yde/ydeLayout")
public class YdeLayoutController extends BaseController {

	@Autowired
	private YdeLayoutService ydeLayoutService;

	@Autowired
	private YdeModuleService ydeModuleService;

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public YdeLayout get(String id, boolean isNewRecord) {
		YdeLayout param = new YdeLayout();
		List<YdeLayout> layoutList = ydeLayoutService.findList(param);
		if(layoutList.size()>0){
			return layoutList.get(0);
		}

		YdeLayout defaultLayout = new YdeLayout();
		defaultLayout.setLayoutType(4L);

		return defaultLayout;
	}

	@ModelAttribute("moduleList")
	public List<YdeModule> getModuleList() {
		YdeModule param = new YdeModule();

		List<YdeModule> ret = ydeModuleService.findList(param);

		return ret;
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("yde:ydeLayout:view")
	@RequestMapping(value = {"list", ""})
	public String list(YdeLayout ydeLayout, Model model) {
		model.addAttribute("ydeLayout", ydeLayout);

		Long[] selectedModules = new Long[8];
		for(int i=0; i< selectedModules.length; i++){
			selectedModules[i] = 0L;
		}

		if(ydeLayout.getModuleIds()!=null){
			String[] modules = ydeLayout.getModuleIds().split(",");

			for(int i=0; i<modules.length; i++){
				selectedModules[i] = Long.valueOf(modules[i]);
			}
		}

		model.addAttribute("selectedModules", selectedModules);

		return "modules/yde/ydeLayoutList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("yde:ydeLayout:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<YdeLayout> listData(YdeLayout ydeLayout, HttpServletRequest request, HttpServletResponse response) {
		ydeLayout.setPage(new Page<>(request, response));
		Page<YdeLayout> page = ydeLayoutService.findPage(ydeLayout);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("yde:ydeLayout:view")
	@RequestMapping(value = "form")
	public String form(YdeLayout ydeLayout, Model model) {
		model.addAttribute("ydeLayout", ydeLayout);
		return "modules/yde/ydeLayoutForm";
	}

	/**
	 * 保存展示模式
	 */
	@RequiresPermissions("yde:ydeLayout:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated YdeLayout ydeLayout, String[] evlModule) {
		StringBuffer moduleIds = new StringBuffer("");
		if(evlModule!=null){
			for(String id: evlModule){

				if(StringUtils.isNotBlank(id) && !StringUtils.equals(id,"0")) {
					if (moduleIds.length() == 0) {
						moduleIds.append(id);
					} else {
						moduleIds.append(",").append(id);
					}
				}
			}
		}

		ydeLayout.setModuleIds(moduleIds.toString());

		ydeLayoutService.save(ydeLayout);
		return renderResult(Global.TRUE, text("保存展示模式成功！"));
	}
	
	/**
	 * 删除展示模式
	 */
	@RequiresPermissions("yde:ydeLayout:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(YdeLayout ydeLayout) {
		ydeLayoutService.delete(ydeLayout);
		return renderResult(Global.TRUE, text("删除展示模式成功！"));
	}


	/**
	 * 批量删除展示模式
	 */
	@RequiresPermissions("yde:ydeLayout:edit")
	@RequestMapping(value = "batchDelete")
	@ResponseBody
	public String batchDelete(String[] ids) {
		for(String id : ids){
			YdeLayout ydeLayout = new YdeLayout();
			ydeLayout.setId(id);
			ydeLayoutService.delete(ydeLayout);
		}
		return renderResult(Global.TRUE, text("批量删除展示模式成功！"), ids);
	}
}