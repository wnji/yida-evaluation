/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.dao.YdeTypeGroupAnalysisDao;
import com.yida.modules.yde.entity.YdeTypeGroupAnalysis;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分批统计评分规则Service
 *
 * @author Kevin
 * @version 2019-05-01
 */
@Service
@Transactional(readOnly = true)
public class YdeTypeGroupAnalysisService extends CrudService<YdeTypeGroupAnalysisDao, YdeTypeGroupAnalysis> {

    /**
     * 获取单条数据
     *
     * @param ydeTypeGroupAnalysis
     * @return
     */
    @Override
    public YdeTypeGroupAnalysis get(YdeTypeGroupAnalysis ydeTypeGroupAnalysis) {
        return super.get(ydeTypeGroupAnalysis);
    }

    /**
     * 查询分页数据
     *
     * @param ydeTypeGroupAnalysis 查询条件
     * @return
     */
    @Override
    public Page<YdeTypeGroupAnalysis> findPage(YdeTypeGroupAnalysis ydeTypeGroupAnalysis) {
        return super.findPage(ydeTypeGroupAnalysis);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param ydeTypeGroupAnalysis
     */
    @Override
    @Transactional(readOnly = false)
    public void save(YdeTypeGroupAnalysis ydeTypeGroupAnalysis) {
        super.save(ydeTypeGroupAnalysis);
    }

    /**
     * 更新状态
     *
     * @param ydeTypeGroupAnalysis
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(YdeTypeGroupAnalysis ydeTypeGroupAnalysis) {
        super.updateStatus(ydeTypeGroupAnalysis);
    }

    /**
     * 删除数据
     *
     * @param ydeTypeGroupAnalysis
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(YdeTypeGroupAnalysis ydeTypeGroupAnalysis) {
        super.delete(ydeTypeGroupAnalysis);
    }

}