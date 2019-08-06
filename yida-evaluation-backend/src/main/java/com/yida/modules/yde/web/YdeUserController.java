/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.EmployeeService;
import com.jeesite.modules.sys.utils.UserUtils;
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
import com.yida.modules.yde.entity.YdeUser;
import com.yida.modules.yde.service.YdeUserService;

/**
 * 用户表Controller
 * @author James
 * @version 2019-05-07
 */
@Controller
@RequestMapping(value = "${adminPath}/yde/ydeUser")
public class YdeUserController extends BaseController {

	@Autowired
	private YdeUserService ydeUserService;

	@Autowired
	private EmployeeService employeeService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public YdeUser get(String id, boolean isNewRecord) {
		if(StringUtils.isNotBlank(id)) {
			return ydeUserService.get(String.valueOf(id), isNewRecord);
		}

		return new YdeUser();
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("yde:ydeUser:view")
	@RequestMapping(value = {"list", ""})
	public String list(YdeUser ydeUser, Model model) {
		model.addAttribute("ydeUser", ydeUser);

		User sysUser = UserUtils.getUser(); // 取到当前登录用户的信息

		model.addAttribute("sysUser", sysUser);

		return "modules/yde/ydeUserList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("yde:ydeUser:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<YdeUser> listData(YdeUser ydeUser, HttpServletRequest request, HttpServletResponse response) {
		ydeUser.setPage(new Page<>(request, response));

		User sysUser = UserUtils.getUser(); // 取到当前登录用户的信息

		if(!StringUtils.equals("none", sysUser.getUserType())){
			Employee employeeParam = new Employee();
			employeeParam.setEmpCode(sysUser.getRefCode());
			Employee employee = employeeService.get(employeeParam);

			if(employee!=null && employee.getOffice()!=null) {
				ydeUser.setSchool(employee.getOffice().getOfficeName());
			}
		}

		Page<YdeUser> page = ydeUserService.findPage(ydeUser);
		return page;
	}

	/**
	 * 用户画像
	 */
	@RequiresPermissions("yde:ydeUser:view")
	@RequestMapping(value = {"/evaluation/result"})
	public String evaluationResult(YdeUser ydeUser, Model model) {
		this.ydeUserService.evaluationResult(ydeUser, model);

		return "modules/yde/ydeUserResult";
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("yde:ydeUser:view")
	@RequestMapping(value = "form")
	public String form(YdeUser ydeUser, Model model) {
		model.addAttribute("ydeUser", ydeUser);
		User sysUser = UserUtils.getUser(); // 取到当前登录用户的信息
		model.addAttribute("sysUser", sysUser);

		return "modules/yde/ydeUserForm";
	}

	/**
	 * 保存用户表
	 */
	@RequiresPermissions("yde:ydeUser:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated YdeUser ydeUser) {
		ydeUserService.save(ydeUser);
		return renderResult(Global.TRUE, text("保存用户表成功！"));
	}
	
	/**
	 * 删除用户表
	 */
	@RequiresPermissions("yde:ydeUser:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(YdeUser ydeUser) {
		ydeUserService.delete(ydeUser);
		return renderResult(Global.TRUE, text("删除用户表成功！"));
	}


	/**
	 * 批量删除用户表
	 */
	@RequiresPermissions("yde:ydeUser:edit")
	@RequestMapping(value = "batchDelete")
	@ResponseBody
	public String batchDelete(String[] ids) {
		for(String id : ids){
			YdeUser ydeUser = new YdeUser();
			ydeUser.setId(id);
			ydeUserService.delete(ydeUser);
		}
		return renderResult(Global.TRUE, text("批量删除用户表成功！"), ids);
	}
	
}