/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 测评设置Entity
 *
 * @author Kevin
 * @version 2019-05-02
 */
@Table(name = "yde_evaluation", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "测评ID", isPK = true, isInsert = false, isUpdate = false),
        @Column(name = "name", attrName = "name", label = "测评名称", queryType = QueryType.LIKE),
        @Column(name = "module_id", attrName = "moduleId", label = "所属测评模块ID"),
        @Column(name = "duration", attrName = "duration", label = "测评时长", isQuery = false),
        @Column(name = "background", attrName = "background", label = "测评背景内容", isQuery = false),
        @Column(name = "question_count", attrName = "questionCount", label = "测评题数", isQuery = false),
        @Column(name = "icon", attrName = "icon", label = "测评图标", isQuery = false),
        @Column(name = "is_part", attrName = "part", label = "是否分部分", isQuery = false),
        @Column(name = "is_limited_once", attrName = "limitedOnce", label = "限定次数", comment = "限定次数(表示只能做一次,不限定表示无数次)", isQuery = false),
        @Column(name = "is_enabled", attrName = "enabled", label = "显示状态 true", comment = "显示状态 true:激活,表示该套测评将显示在前台中;false:冻结,表示隐藏,看不到该测评", isQuery = true),
        @Column(includeEntity = DataEntity.class),
}, joinTable = {
        @JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = YdeRule.class, attrName = "rule", alias = "b",
                on = "b.evaluation_id = a.id",
                columns = {
                        @Column(includeEntity = YdeRule.class),
                }),
        @JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = YdeModule.class, attrName = "module", alias = "c",
                on = "a.module_id = c.id",
                columns = {
                        @Column(name = "name", attrName = "name", label = "测评模块", isQuery = false),
                })
},
        orderBy = "a.update_date DESC"
)
@ApiModel(description = "测评设置")
@Data
@JsonIgnoreProperties(value = {"status", "createBy", "createDate", "updateBy", "updateDate", "remarks", "isNewRecord"})
public class YdeEvaluation extends DataEntity<YdeEvaluation> {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "测评名称 不能为空")
    @Length(max = 100, message = "测评名称 长度不能超过100 个字符")
    @ApiModelProperty(value = "测评名称", position = 1, example = "霍兰德职业兴趣测评")
    private String name;

    @ApiModelProperty(value = "显示状态, true: 激活, false: 冻结", position = 2, example = "fa fa-question-circle")
    private String icon;

    @ApiModelProperty(value = "所属测评模块", position = 3, example = "1")
    private Long moduleId;

    @ApiModelProperty(value = "所属测评模块名称", position = 4, example = "职业方向")
    private String moduleName;

    @ApiModelProperty(value = "测评时长(分钟）", position = 5, example = "30")
    private Long duration;

    @Length(max = 10000, message = "测评背景内容 长度不能超过10000 个字符")
    @ApiModelProperty(value = "<p>测评背景内容", position = 6, example = "一、本测验共60道题，作答参考时间为30分钟，时间请参考本界面上的倒计时。</p>" +
            "<p>二、计时测验没有暂停或离开功能，点击【正式答题】计时开始，若中途离开，依然计时，倒计时为0时系统自动收卷；不计时测验需手动交卷。</p>" +
            "<p>三、请你根据自己在实际情况，从每道题目中的选项中选出你的答案：\"是\"或者\"否\"。</p>" +
            "<p>四、本测验交卷前，可以点击”上一题“进行修改。</p>")
    private String background;

    @ApiModelProperty(value = "是否分部分， 1: 是, 0: 否", position = 7, example = "1")
    private String part;

    @ApiModelProperty(value = "限制次数, 1: 是, 0: 否", position = 8, example = "1")
    private String limitedOnce;

    @ApiModelProperty(value = "显示状态, 1: 激活, 0: 冻结", position = 9, example = "1")
    private String enabled;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private YdeRule rule;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private YdeModule module;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer ruleType;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer reportType;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String result;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String rulesJson;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer statisticsMethod;            //统计方法

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer pointsSource;                //分值来源

    @ApiModelProperty(hidden = true)
    private Integer questionCount;              //测评题数

    public YdeEvaluation() {
        this(null);
    }

    public YdeEvaluation(String id) {
        super(id);
    }

}