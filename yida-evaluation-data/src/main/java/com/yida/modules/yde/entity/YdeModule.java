/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 测评模块Entity
 * @author Kevin
 * @version 2019-05-02
 */
@Table(name="yde_module", alias="a", columns={
		@Column(name="id", attrName="id", label="测评模块ID", isPK=true, isInsert=false, isUpdate=false),
		@Column(name="name", attrName="name", label="测评模块", isQuery=false),
		@Column(name="start_time", attrName="startTime", label="开始时间", isQuery=false),
		@Column(name="end_time", attrName="endTime", label="结束时间", isQuery=false),
		@Column(name="is_evaluation_limited", attrName="isEvaluationLimited", label="测评套题限制", isQuery=false),
		@Column(name="introduction", attrName="introduction", label="测评模块介绍", isQuery=false),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
@ApiModel(description = "测评模块")
@Data
@JsonIgnoreProperties(value = {"status", "createBy", "createDate", "updateBy", "updateDate", "remarks", "isNewRecord"})
public class YdeModule extends DataEntity<YdeModule> {
	
	private static final long serialVersionUID = 1L;

	@Length(max=100, message="测评模块 长度不能超过100 个字符")
	@ApiModelProperty(value = "测评模块", position = 1, example = "职业方向")
	private String name;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "开始时间", position = 2, example = "2019-04-30 10:00:00")
	private Date startTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "结束时间", position = 3, example = "2019-05-01 10:00:00")
	private Date endTime;

	@ApiModelProperty(value = "是否限制测评套题", position = 4, example = "true")
	private Boolean isEvaluationLimited;

	@Length(min=0, max=10000, message="测评模块介绍 长度不能超过10000 个字符")
	@ApiModelProperty(value = "测评模块介绍", position = 4, example = "<p>职业方向有如下两种测评，分别为霍兰德职业兴趣测评及MBTI职业性格测评。</p>" +
			"<p>主要是测评洞察人才、识别人才的发展倾向 ，将人才安置到契合的岗位，为员工的职业定位和职业发展规划提供标准和指导，从而做到用人所长并充分保留并激励企业关键人才。</p>")
	private String introduction;

	
	public YdeModule() {
		this(null);
	}

	public YdeModule(String id){
		super(id);
	}
	
}