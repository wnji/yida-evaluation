/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 模块测评对应关系Entity
 * @author Kevin
 * @version 2019-05-01
 */
@Table(name="yde_module_evaluation", alias="a", columns={
		@Column(name="id", attrName="id", label="ID", isPK=true, isInsert=false, isUpdate=false),
		@Column(name="module_id", attrName="moduleId", label="模块ID"),
		@Column(name="evaluation_id", attrName="evaluationId", label="测评ID"),
	}, orderBy="a.id DESC"
)
@ApiModel(description = "模块测评对应关系")
@Data
@JsonIgnoreProperties(value = {"status", "createBy", "createDate", "updateBy", "updateDate", "remarks", "isNewRecord"})
public class YdeModuleEvaluation extends DataEntity<YdeModuleEvaluation> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "模块ID", position = 0, example = "1")
	private Long moduleId;

	@ApiModelProperty(value = "测评ID", position = 0, example = "1")
	private Long evaluationId;

	
	public YdeModuleEvaluation() {
		this(null);
	}

	public YdeModuleEvaluation(String id){
		super(id);
	}
	
}