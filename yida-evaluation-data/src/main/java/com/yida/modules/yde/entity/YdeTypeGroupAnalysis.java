/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 组合分析Entity
 * @author Kevin
 * @version 2019-05-26
 */
@Table(name="yde_type_group_analysis", alias="a", columns={
		@Column(name="id", attrName="id", label="ID", isPK=true, isInsert=false, isUpdate=false),
		@Column(name="rule_id", attrName="ruleId", label="规则ID"),
		@Column(name="type1", attrName="type1", label="类型1", isQuery=false),
		@Column(name="type2", attrName="type2", label="类型2", isQuery=false),
		@Column(name="type3", attrName="type3", label="类型3", isQuery=false),
		@Column(name="type4", attrName="type4", label="类型4", isQuery=false),
		@Column(name="belong_to", attrName="belongTo", label="属于类型", isQuery=false),
		@Column(name="analysis", attrName="analysis", label="分析", isQuery=false),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.id ASC"
)
@ApiModel(description = "组合分析")
@Data
public class YdeTypeGroupAnalysis extends DataEntity<YdeTypeGroupAnalysis> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "评分规则ID", position = 1, example = "1")
	private Long ruleId;

	@Length(max=64, message="类型1 长度不能超过64 个字符")
	@ApiModelProperty(value = "类型1", position = 2, example = "")
	private String type1;

	@Length(max=64, message="类型2 长度不能超过64 个字符")
	@ApiModelProperty(value = "类型2", position = 3, example = "")
	private String type2;

	@Length(max=64, message="类型3 长度不能超过64 个字符")
	@ApiModelProperty(value = "类型3", position = 4, example = "")
	private String type3;

	@Length(max=64, message="类型4 长度不能超过64 个字符")
	@ApiModelProperty(value = "类型4", position = 4, example = "")
	private String type4;

	@Length(max=200, message="属于类型 长度不能超过200 个字符")
	@ApiModelProperty(value = "属于类型", position = 5, example = "")
	private String belongTo;

	@Length(max=10000, message="分析 长度不能超过10000 个字符")
	@ApiModelProperty(value = "分析", position = 6, example = "")
	private String analysis;

	public YdeTypeGroupAnalysis(String id) {
		super(id);
	}

	public YdeTypeGroupAnalysis(){
		this(null);
	}
	
}