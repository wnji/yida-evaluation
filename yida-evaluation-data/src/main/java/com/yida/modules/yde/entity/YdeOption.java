/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 选项Entity
 *
 * @author Kevin
 * @version 2019-05-02
 */
@Table(name = "yde_option", alias = "a", columns = {
        @Column(name = "id", isPK = true, attrName = "id", label = "ID", isInsert = false, isUpdate = false),
        @Column(name = "question_id", attrName = "questionId", label = "题目ID"),
        @Column(name = "option_type", attrName = "optionType", label = "答案类型", comment = "答案类型(1: 选项，2:填写)"),
        @Column(name = "option_name", attrName = "optionName", label = "选项名称，例如A，B…", comment = "选项名称，例如A，B…(填写类型时为空)", queryType = QueryType.LIKE),
        @Column(name = "content", attrName = "content", label = "选项内容，或者标准答案"),
        @Column(name = "points", attrName = "optionPoints", label = "分值"),
        @Column(includeEntity = DataEntity.class),
}, orderBy = "a.option_name ASC"
)
@ApiModel(description = "选项")
@Data
@JsonIgnoreProperties(value = {"status", "createBy", "createDate", "updateBy", "updateDate", "remarks", "isNewRecord"})
public class YdeOption extends DataEntity<YdeOption> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long questionId;

    @ApiModelProperty(value = "答案类型(1: 选项，2:填写，3:是否)", position = 1, example = "1")
    private Integer optionType;

    @ApiModelProperty(value = "选项名称，例如A，B…(填写类型时为答案内容)", position = 2, example = "1")
    private String optionName;

    @Length(max = 10000, message = "选项内容，或者标准答案 长度不能超过255 个字符")
    @ApiModelProperty(value = "选项内容，或者标准答案", position = 3, example = "")
    private String content;

    private Integer points;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer optionPoints;

    public YdeOption() {
        this(null);
    }

    public YdeOption(String id) {
        super(id);
    }

    public void copyPoints(){
        this.points = optionPoints;
    }
}