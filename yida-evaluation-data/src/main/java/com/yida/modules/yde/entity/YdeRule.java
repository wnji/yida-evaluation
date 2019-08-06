/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 测评设置Entity
 * @author Kevin
 * @version 2019-05-01
 */
@Table(name="yde_rule", alias="a", columns={
		@Column(name="id", attrName="id", label="评分规则ID", isPK=true, isInsert=false, isUpdate=false),
		@Column(name="evaluation_id", attrName="evaluationId", label="测评ID", comment="测评ID"),
		@Column(name="rule_type", attrName="ruleType", label="规则类型 ", comment="规则类型 (1:叠加统计, 2:分批统计)"),
		@Column(name="report_type", attrName="reportType", label="展现结果 ", comment="展现结果 (1:文字描述, 2:饼图, 3:雷达图, 4:仪表盘)"),
		@Column(name="points_source", attrName="pointsSource", label="分值来源 ", comment="分值来源, 1: 系统配置, 2: 用户输入"),
		@Column(name="statistics_method", attrName="statisticsMethod", label="统计方法 ", comment="统计方法, 1: 跨组统计, 2: 组内统计"),
		@Column(name="result", attrName="result", label="叠加统计结果分析", comment="叠加统计结果分析(分批统计时为空)"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
@ApiModel(description = "测评设置")
@Data
@JsonIgnoreProperties(value = {"status", "createBy", "createDate", "updateBy", "updateDate", "remarks", "isNewRecord"})
public class YdeRule extends DataEntity<YdeRule> {
	public static final Integer RULE_TYPE_SCORE_SUM = Integer.valueOf(1);	//叠加统计
	public static final Integer RULE_TYPE_GROUP_SUM = Integer.valueOf(2);		//分批统计

	public static final Integer REPORT_TYPE_TEXT = Integer.valueOf(1);			//1:文字描述
	public static final Integer REPORT_TYPE_PIE_CHART = Integer.valueOf(2);		//2:饼图
	public static final Integer REPORT_TYPE_RADAR_CHART = Integer.valueOf(3);	//3:雷达图
	public static final Integer REPORT_TYPE_DASHBOARD = Integer.valueOf(4);		//4:仪表盘

	public static final Integer STATISTICS_METHOD_INTER_GROUP = Integer.valueOf(1);		//1:跨组统计
	public static final Integer STATISTICS_METHOD_INTRA_GROUP = Integer.valueOf(2);		//2:组内统计

	public static final Integer RULE_SOURCE_SYSTEM = Integer.valueOf(1);	//系统配置
	public static final Integer RULE_SOURCE_USER = Integer.valueOf(2);		//用户输入

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "测评ID", position = 1, example = "1")
	private Long evaluationId;

	@ApiModelProperty(value = "规则类型 (1:叠加统计, 2:分批统计)", position = 2, example = "1")
	private Integer ruleType;

	@ApiModelProperty(value = "展现结果 (1:文字描述, 2:饼图, 3:雷达图, 4:仪表盘)", position = 3, example = "1")
	private Integer reportType;

	@ApiModelProperty(value = "叠加统计结果分析(分批统计时为空)", position = 4, example = "")
	private String result;

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private Integer statisticsMethod;			//统计方法

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private Integer pointsSource;				//分值来源
	
	public YdeRule() {
		this(null);
	}

	public YdeRule(String id){
		super(id);
	}
	
}