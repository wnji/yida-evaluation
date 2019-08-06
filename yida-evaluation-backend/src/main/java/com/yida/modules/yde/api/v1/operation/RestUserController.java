/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.api.v1.operation;

import com.jeesite.common.web.BaseController;
import com.yida.modules.yde.api.v1.model.DefaultApiResponse;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 益达人才测评模块
 * @author Kevin
 * @version 2019-04-23
 */
@Controller
@RequestMapping(value = "${v1ApiPath}/user")
@Api(value = "益达人才测评", description = "用户接口")
@DefaultApiResponse
public class RestUserController extends BaseController {

	

}