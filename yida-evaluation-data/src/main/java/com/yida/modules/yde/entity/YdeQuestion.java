/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.jsoup.Jsoup;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 题目设置Entity
 *
 * @author Kevin
 * @version 2019-05-02
 */
@Table(name = "yde_question", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "ID", isPK = true, isInsert = false, isUpdate = false),
        @Column(name = "question", attrName = "question", label = "题目", isQuery = false),
        @Column(name = "evaluation_id", attrName = "evaluationId", label = "测评ID"),
        @Column(name = "part_id", attrName = "partId", label = "所属部分", isQuery = false),
        @Column(name = "no", attrName = "no", label = "题号"),
        @Column(name = "option_type", attrName = "optionType", label = "选项类型", isQuery = false),
        @Column(name = "answer", attrName = "answer", label = "标准答案", isQuery = false),
        @Column(name = "points", attrName = "points", label = "标准答案分值", isQuery = false),
        @Column(includeEntity = DataEntity.class)
}, joinTable = {
        @JoinTable(type = JoinTable.Type.LEFT_JOIN, entity = YdePart.class, attrName = "part", alias = "p",
                on = "a.part_id = p.id and p.status = '0'",
                columns = {
                @Column(includeEntity = YdePart.class)
        })
}, orderBy = "p.section ASC, a.no ASC"
)
@ApiModel(description = "题目设置")
@Data
@JsonIgnoreProperties(value = {"status", "createBy", "createDate", "updateBy", "updateDate", "remarks", "isNewRecord"})
public class YdeQuestion extends DataEntity<YdeQuestion> {
    public static final Integer OPTION_TYPE_SELECTION = Integer.valueOf(1);
    public static final Integer OPTION_TYPE_FILLING = Integer.valueOf(2);
    public static final Integer OPTION_TYPE_YESNO = Integer.valueOf(3);
    public static final Integer OPTION_TYPE_ALLOCATION = Integer.valueOf(4);

    private static final long serialVersionUID = 1L;

    @Length(max = 10000, message = "题目 长度不能超过10000 个字符")
    @ApiModelProperty(value = "题目", position = 1, example = "哪种情况最与你相符?")
    private String question;

    @ApiModelProperty(value = "测评ID", position = 2, example = "1")
    private Long evaluationId;

    @ApiModelProperty(value = "所属部分ID", position = 3, example = "1")
    private Long partId;

    @ApiModelProperty(value = "题号", position = 4, example = "1")
    private Long no;

    @ApiModelProperty(value = "选项类型，1: 选项, 2: 填写, 3: 是否, 4: 分配", position = 5, example = "2")
    private Integer optionType;

    @ApiModelProperty(value = "选项列表", position = 6)
    private List<YdeOption> options;

    @ApiModelProperty(hidden = true)
    private String answer;  //标准答案，填写类型时才有值

    @ApiModelProperty(hidden = true)
    private Integer points; //填写类型标准答案分值，选项时所有选项中的最大值,分配是各分数总和

    @ApiModelProperty(hidden = true)
    private Integer totalPoints;

    @ApiModelProperty(hidden = true)
    private String section;

    @JsonIgnore
    private YdePart part;

    public YdeQuestion() {
        this(null);
    }

    public YdeQuestion(String id) {
        super(id);
    }

    @ApiModelProperty(hidden = true)
    public String getOptionStr() {
        if (options != null && !options.isEmpty()) {
            return options.stream().map(o ->
                    o.getOptionName() != null ? (o.getOptionName() + "." + Jsoup.parse(StringUtils.defaultString(o.getContent())).text()) : "")
                    .map(t-> "<p>" + t + "</p>").collect(Collectors.joining());
        }
        return "";
    }

    @ApiModelProperty(hidden = true)
    public Integer getPointA() {
        if (options != null && !options.isEmpty()) {
            Optional<YdeOption> optional = options.stream().filter(o ->
                    StringUtils.inString(o.getOptionName(), "A", "是")).findFirst();
            return optional.isPresent() ? optional.get().getPoints() : null;
        }
        return null;
    }

    @ApiModelProperty(hidden = true)
    public Integer getPointB() {
        if (options != null && !options.isEmpty()) {
            Optional<YdeOption> optional = options.stream().filter(o ->
                    StringUtils.inString(o.getOptionName(), "B", "否")).findFirst();
            return optional.isPresent() ? optional.get().getPoints() : null;
        }
        return null;
    }

    @ApiModelProperty(hidden = true)
    public Integer getPointC() {
        if (options != null && !options.isEmpty()) {
            Optional<YdeOption> optional = options.stream().filter(o ->
                    "C".equals(o.getOptionName())).findFirst();
            return optional.isPresent() ? optional.get().getPoints() : null;
        }
        return null;
    }

    @ApiModelProperty(hidden = true)
    public Integer getPointD() {
        if (options != null && !options.isEmpty()) {
            Optional<YdeOption> optional = options.stream().filter(o ->
                    "D".equals(o.getOptionName())).findFirst();
            return optional.isPresent() ? optional.get().getPoints() : null;
        }
        return null;
    }

    @ApiModelProperty(hidden = true)
    public Integer getPointE() {
        if (options != null && !options.isEmpty()) {
            Optional<YdeOption> optional = options.stream().filter(o ->
                    "E".equals(o.getOptionName())).findFirst();
            return optional.isPresent() ? optional.get().getPoints() : null;
        }
        return null;
    }

    @ApiModelProperty(hidden = true)
    public Integer getPointF() {
        if (options != null && !options.isEmpty()) {
            Optional<YdeOption> optional = options.stream().filter(o ->
                    "F".equals(o.getOptionName())).findFirst();
            return optional.isPresent() ? optional.get().getPoints() : null;
        }
        return null;
    }

    /**
     * 根据答案获取得分
     *
     * @param answer
     * @return
     */
    public Integer getPoints(String answer) {
        if (OPTION_TYPE_FILLING == optionType) {
            return this.answer.equals(answer.trim()) ? points : 0;
        } else {
            if (ListUtils.isNotEmpty(options)) {
                Optional<YdeOption> optional = options.stream().filter(o -> o.getOptionName().equals(answer)).findFirst();
                return optional.isPresent() ? optional.get().getPoints() : 0;
            }
        }

        return 0;
    }
}