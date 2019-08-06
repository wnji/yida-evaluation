/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 展示模式Entity
 *
 * @author Kevin
 * @version 2019-05-02
 */
@Table(name = "yde_layout", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "ID", isPK = true, isInsert = false, isUpdate = false),
        @Column(name = "layout_type", attrName = "layoutType", label = "版式类型", comment = "版式类型(4:四板块, 5:五板块, 6:六板块, 7:七板块, 8:八板块)"),
        @Column(name = "module_ids", attrName = "moduleIds", label = "模块ID列表,按顺序设置模块ID,逗号分隔"),
        @Column(includeEntity = DataEntity.class),
}, orderBy = "a.update_date DESC"
)
@ApiModel(description = "展示模式")
@Data
@JsonIgnoreProperties(value = {"status", "createBy", "createDate", "updateBy", "updateDate", "remarks", "isNewRecord"})
public class YdeLayout extends DataEntity<YdeLayout> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "版式类型 不能为空")
    @ApiModelProperty(value = "版式类型(4:四板块, 5:五板块, 6:六板块, 7:七板块, 8:八板块)", position = 1, example = "1")
    private Long layoutType;

    @NotBlank(message = "模块ID列表,按顺序设置模块ID,逗号分隔 不能为空")
    @Length(min = 0, max = 128, message = "模块ID列表,按顺序设置模块ID,逗号分隔 长度不能超过128 个字符")
    @ApiModelProperty(value = "模块ID列表,按顺序设置模块ID,逗号分隔", position = 2, example = "1,2,3")
    private String moduleIds;

    @ApiModelProperty(value = "相应的模块列表", position = 3)
    private List<YdeModule> modules;

    public YdeLayout() {
        this(null);
    }

    public YdeLayout(String id) {
        super(id);
    }

}