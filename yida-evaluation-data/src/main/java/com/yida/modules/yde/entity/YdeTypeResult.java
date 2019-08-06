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
 * 分批统计评分结果Entity
 * @author Kevin
 * @version 2019-05-01
 */
@Table(name="yde_type_result", alias="a", columns={
		@Column(name="id", attrName="id", label="分批统计评分结果ID", isPK=true, isInsert=false, isUpdate=false),
		@Column(name="rule_id", attrName="ruleId", label="评分规则ID"),
		@Column(name="type1", attrName="type1", label="类型1"),
		@Column(name="type2", attrName="type2", label="类型2"),
		@Column(name="type3", attrName="type3", label="类型3"),
		@Column(name="type4", attrName="type4", label="类型4"),
		@Column(name="belong_to", attrName="belongTo", label="属于"),
		@Column(name="analysis", attrName="analysis", label="分析类型组合详情"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
@ApiModel(description = "分批统计评分结果")
@Data
@JsonIgnoreProperties(value = {"status", "createBy", "createDate", "updateBy", "updateDate", "remarks", "isNewRecord"})
public class YdeTypeResult extends DataEntity<YdeTypeResult> {
	
	private static final long serialVersionUID = 1L;

	@NotNull(message="评分规则ID 不能为空")
	@ApiModelProperty(value = "评分规则ID", position = 1, example = "1")
	private Long ruleId;

	@NotBlank(message="类型1 不能为空")
	@Length(max=64, message="类型1 长度不能超过64 个字符")
	@ApiModelProperty(value = "类型1", position = 2, example = "现实型")
	private String type1;

	@NotBlank(message="类型2 不能为空")
	@Length(max=64, message="类型2 长度不能超过64 个字符")
	@ApiModelProperty(value = "类型2", position = 3, example = "研究型")
	private String type2;

	@NotBlank(message="类型3 不能为空")
	@Length(max=64, message="类型3 长度不能超过64 个字符")
	@ApiModelProperty(value = "类型3", position = 4, example = "艺术型")
	private String type3;

	@NotBlank(message="类型4 不能为空")
	@Length(max=64, message="类型4 长度不能超过64 个字符")
	@ApiModelProperty(value = "类型4", position = 4, example = "艺术型")
	private String type4;

	@NotBlank(message="属于 不能为空")
	@Length( max=100, message="属于 长度不能超过100 个字符")
	@ApiModelProperty(value = "属于", position = 5, example = "RIA")
	private String belongTo;

	@Length(min=0, max=256, message="分析类型组合详情 长度不能超过256 个字符")
	@ApiModelProperty(value = "分析类型组合详情", position = 6, example = "<p>特征:</p>" +
			"<p>     1、具有顺从、坦率、谦虚、自然、坚毅、实际、有礼、害羞、稳健、节俭。</p>" +
			"<p>     2、具有分析、谨慎、批评、好奇、独立、聪明、内向、条理、谦逊、精确、理发、保守。</p>" +
			"<p>     3、具有复杂、想象、冲动、独立、直觉、无秩序、情绪化、理想化、不顺从、有创意、富有表情、不重实际</p>")
	private String analysis;

	
	public YdeTypeResult() {
		this(null);
	}

	public YdeTypeResult(String id){
		super(id);
	}
	
}