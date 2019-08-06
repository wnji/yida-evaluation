/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.yida.modules.yde.entity.YdeOption;
import com.yida.modules.yde.service.YdeOptionService;

/**
 * 选项Controller
 * @author Kevin
 * @version 2019-05-02
 */
@Controller
@RequestMapping(value = "${adminPath}/yde/ydeOption")
public class YdeOptionController extends BaseController {

	@Autowired
	private YdeOptionService ydeOptionService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public YdeOption get(String id, boolean isNewRecord) {
		return ydeOptionService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("yde:ydeOption:view")
	@RequestMapping(value = {"list", ""})
	public String list(YdeOption ydeOption, Model model) {
		model.addAttribute("ydeOption", ydeOption);
		return "modules/yde/ydeOptionList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("yde:ydeOption:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<YdeOption> listData(YdeOption ydeOption, HttpServletRequest request, HttpServletResponse response) {
		ydeOption.setPage(new Page<>(request, response));
		Page<YdeOption> page = ydeOptionService.findPage(ydeOption);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("yde:ydeOption:view")
	@RequestMapping(value = "form")
	public String form(YdeOption ydeOption, Model model) {
		model.addAttribute("ydeOption", ydeOption);
		return "modules/yde/ydeOptionForm";
	}

	/**
	 * 保存选项
	 */
	@RequiresPermissions("yde:ydeOption:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated YdeOption ydeOption) {
		ydeOptionService.save(ydeOption);
		return renderResult(Global.TRUE, text("保存选项成功！"));
	}
	
	/**
	 * 删除选项
	 */
	@RequiresPermissions("yde:ydeOption:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(YdeOption ydeOption) {
		ydeOptionService.delete(ydeOption);
		return renderResult(Global.TRUE, text("删除选项成功！"));
	}


	/**
	 * 批量删除选项
	 */
	@RequiresPermissions("yde:ydeOption:edit")
	@RequestMapping(value = "batchDelete")
	@ResponseBody
	public String batchDelete(String[] ids) {
		for(String id : ids){
			YdeOption ydeOption = new YdeOption();
			ydeOption.setId(id);
			ydeOptionService.delete(ydeOption);
		}
		return renderResult(Global.TRUE, text("批量删除选项成功！"), ids);
	}
	
}