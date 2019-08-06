/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;

import com.jeesite.common.mybatis.annotation.JoinTable;
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
 * 用户表Entity
 * @author James
 * @version 2019-05-07
 */
@Table(name="yde_user", alias="a", columns={
		@Column(name="id", attrName="id", label="用户ID", isPK=true, isInsert=false, isUpdate=false),
		@Column(name="yida_user_no", attrName="yidaUserNo", label="对应益达云平台schooluser表的no，登录时的username"),
		@Column(name="real_name", attrName="realName", label="姓名", queryType=QueryType.LIKE),
		@Column(name="school", attrName="school", label="学校"),
		@Column(name="school_year", attrName="schoolYear", label="学届"),
		@Column(name="major_name", attrName="majorName", label="专业"),
		@Column(name="class_name", attrName="className", label="班级"),
		@Column(name="wx_openid", attrName="wxOpenid", label="微信openid", isQuery=false),

		@Column(name="photo", attrName="photo", label="头像"),

		@Column(includeEntity=DataEntity.class),
	},
	joinTable={
			@JoinTable(type= JoinTable.Type.LEFT_JOIN, entity=YdeEvaluationModules.class, attrName="this", alias="b",
					on="b.user_id = a.id",
					columns={
							@Column(name="evaluation_modules", attrName="evaluationModules", label="测评套题"),
					}),
			@JoinTable(type= JoinTable.Type.LEFT_JOIN, entity=YdeEvaluationTimes.class, attrName="this", alias="c",
					on="c.user_id = a.id",
					columns={
							@Column(name="evaluation_times", attrName="evaluationTimes", label="测评次数"),
					}),
    },
	orderBy="a.update_date DESC"
)
@ApiModel(description = "用户表")
@Data
public class YdeUser extends DataEntity<YdeUser> {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "对应益达云平台schooluser表的no，登录时的username", position = 0, example = "")
	private String yidaUserNo;

	@Length(min=0, max=64, message="姓名 长度不能超过64 个字符")
	@ApiModelProperty(value = "姓名", position = 0, example = "")
	private String realName;

	@Length(min=0, max=128, message="学校 长度不能超过128 个字符")
	@ApiModelProperty(value = "学校", position = 0, example = "")
	private String school;

	@Length(min=0, max=16, message="学届 长度不能超过16 个字符")
	@ApiModelProperty(value = "学届", position = 0, example = "")
	private String schoolYear;

	@Length(min=0, max=64, message="专业 长度不能超过64 个字符")
	@ApiModelProperty(value = "专业", position = 0, example = "")
	private String majorName;

	@Length(min=0, max=64, message="班级 长度不能超过64 个字符")
	@ApiModelProperty(value = "班级", position = 0, example = "")
	private String className;

	@Length(min=0, max=80, message="微信openid 长度不能超过80 个字符")
	@ApiModelProperty(value = "微信openid", position = 0, example = "")
	private String wxOpenid;

	private String evaluationTimes;
	private String evaluationModules;

	private String photo;
	
	public YdeUser() {
		this(null);
	}

	public YdeUser(String id){
		super(id);
	}
	
}