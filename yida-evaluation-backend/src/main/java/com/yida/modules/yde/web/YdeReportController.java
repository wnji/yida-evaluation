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
import com.yida.modules.yde.entity.YdeReport;
import com.yida.modules.yde.service.YdeReportService;

/**
 * 测评报告Controller
 * @author Kevin
 * @version 2019-05-01
 */
@Controller
@RequestMapping(value = "${adminPath}/yde/ydeReport")
public class YdeReportController extends BaseController {

	@Autowired
	private YdeReportService ydeReportService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public YdeReport get(String id, boolean isNewRecord) {
		return ydeReportService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("yde:ydeReport:view")
	@RequestMapping(value = {"list", ""})
	public String list(YdeReport ydeReport, Model model) {
		model.addAttribute("ydeReport", ydeReport);
		return "modules/yde/ydeReportList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("yde:ydeReport:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<YdeReport> listData(YdeReport ydeReport, HttpServletRequest request, HttpServletResponse response) {
		ydeReport.setPage(new Page<>(request, response));
		Page<YdeReport> page = ydeReportService.findPage(ydeReport);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("yde:ydeReport:view")
	@RequestMapping(value = "form")
	public String form(YdeReport ydeReport, Model model) {
		model.addAttribute("ydeReport", ydeReport);
		return "modules/yde/ydeReportForm";
	}

	/**
	 * 保存测评报告
	 */
	@RequiresPermissions("yde:ydeReport:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated YdeReport ydeReport) {
		ydeReportService.save(ydeReport);
		return renderResult(Global.TRUE, text("保存测评报告成功！"));
	}
	
	/**
	 * 删除测评报告
	 */
	@RequiresPermissions("yde:ydeReport:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(YdeReport ydeReport) {
		ydeReportService.delete(ydeReport);
		return renderResult(Global.TRUE, text("删除测评报告成功！"));
	}


	/**
	 * 批量删除测评报告
	 */
	@RequiresPermissions("yde:ydeReport:edit")
	@RequestMapping(value = "batchDelete")
	@ResponseBody
	public String batchDelete(String[] ids) {
		for(String id : ids){
			YdeReport ydeReport = new YdeReport();
			ydeReport.setId(id);
			ydeReportService.delete(ydeReport);
		}
		return renderResult(Global.TRUE, text("批量删除测评报告成功！"), ids);
	}
	
}