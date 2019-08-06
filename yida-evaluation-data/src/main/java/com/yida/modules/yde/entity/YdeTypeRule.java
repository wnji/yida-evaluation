/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import java.util.Date;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分批统计评分规则Entity
 * @author Kevin
 * @version 2019-05-01
 */
@Table(name="yde_type_rule", alias="a", columns={
		@Column(name="id", attrName="id", label="分批统计评分规则ID", isPK=true, isInsert=false, isUpdate=false),
		@Column(name="rule_id", attrName="ruleId", label="评分规则ID"),
		@Column(name="group_no", attrName="groupNo", label="组合编号"),
		@Column(name="option_name", attrName="optionName", label="选项名称"),
		@Column(name="type", attrName="type", label="类型"),
		@Column(name="question_ids", attrName="questionIds", label="题号ID列表,逗号分隔"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.group_no ASC, rule_id ASC"
)
@ApiModel(description = "分批统计评分规则")
@Data
@JsonIgnoreProperties(value = {"status", "createBy", "createDate", "updateBy", "updateDate", "remarks", "isNewRecord"})
public class YdeTypeRule extends DataEntity<YdeTypeRule> {
	
	private static final long serialVersionUID = 1L;

	@NotNull(message="评分规则ID 不能为空")
	@ApiModelProperty(value = "评分规则ID", position = 1, example = "1")
	private Long ruleId;

	@NotNull(message="组合编号 不能为空")
	@ApiModelProperty(value = "组合编号", position = 2, example = "1")
	Integer groupNo;

	@NotNull(message="选项名称 不能为空")
	@ApiModelProperty(value = "选项名称", position = 2, example = "A")
	String optionName;

	@NotBlank(message="类型 不能为空")
	@Length(max=64, message="类型 长度不能超过64 个字符")
	@ApiModelProperty(value = "类型", position = 3, example = "研究型")
	private String type;

	@Length(max=256, message="题号ID列表长度不能超过256 个字符")
	@ApiModelProperty(value = "题号ID列表,逗号分隔", position = 4, example = "1,5")
	private String questionIds;

	@ApiModelProperty(hidden = true)
	private String questionNos;
	
	public YdeTypeRule() {
		this(null);
	}

	public YdeTypeRule(String id){
		super(id);
	}
	
}