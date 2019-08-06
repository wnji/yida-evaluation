/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 视图v_evaluation_modules对应的Entity
 * @author James
 * @version 2019-05-07
 */
@Table(name="v_evaluation_modules", alias="b", columns={
		@Column(name="user_id", attrName="userId", label="对应yde_user的id", isPK=true),
		@Column(name="evaluation_modules", attrName="evaluationModules", label="测评套题个数")
	}
)
@ApiModel(description = "用户参加过的测评套题个数")
@Data
public class YdeEvaluationModules extends DataEntity<YdeEvaluationModules> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "对应yde_user的id", position = 0, example = "")
	private String userId;

	//  用户参加过的测评套题个数(沿用之前的变量名)
	private Integer evaluationModules;

	public YdeEvaluationModules() {

	}
}