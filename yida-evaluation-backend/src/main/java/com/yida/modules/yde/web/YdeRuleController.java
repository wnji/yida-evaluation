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
import com.yida.modules.yde.entity.YdeRule;
import com.yida.modules.yde.service.YdeRuleService;

/**
 * 测评设置Controller
 * @author Kevin
 * @version 2019-05-01
 */
@Controller
@RequestMapping(value = "${adminPath}/yde/ydeRule")
public class YdeRuleController extends BaseController {

	@Autowired
	private YdeRuleService ydeRuleService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public YdeRule get(String id, boolean isNewRecord) {
		return ydeRuleService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("yde:ydeRule:view")
	@RequestMapping(value = {"list", ""})
	public String list(YdeRule ydeRule, Model model) {
		model.addAttribute("ydeRule", ydeRule);
		return "modules/yde/ydeRuleList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("yde:ydeRule:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<YdeRule> listData(YdeRule ydeRule, HttpServletRequest request, HttpServletResponse response) {
		ydeRule.setPage(new Page<>(request, response));
		Page<YdeRule> page = ydeRuleService.findPage(ydeRule);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("yde:ydeRule:view")
	@RequestMapping(value = "form")
	public String form(YdeRule ydeRule, Model model) {
		model.addAttribute("ydeRule", ydeRule);
		return "modules/yde/ydeRuleForm";
	}

	/**
	 * 保存测评设置
	 */
	@RequiresPermissions("yde:ydeRule:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated YdeRule ydeRule) {
		ydeRuleService.save(ydeRule);
		return renderResult(Global.TRUE, text("保存测评设置成功！"));
	}
	
	/**
	 * 删除测评设置
	 */
	@RequiresPermissions("yde:ydeRule:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(YdeRule ydeRule) {
		ydeRuleService.delete(ydeRule);
		return renderResult(Global.TRUE, text("删除测评设置成功！"));
	}


	/**
	 * 批量删除测评设置
	 */
	@RequiresPermissions("yde:ydeRule:edit")
	@RequestMapping(value = "batchDelete")
	@ResponseBody
	public String batchDelete(String[] ids) {
		for(String id : ids){
			YdeRule ydeRule = new YdeRule();
			ydeRule.setId(id);
			ydeRuleService.delete(ydeRule);
		}
		return renderResult(Global.TRUE, text("批量删除测评设置成功！"), ids);
	}
	
}