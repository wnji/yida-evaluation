/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.dao.YdeTypeRuleDao;
import com.yida.modules.yde.entity.YdeTypeRule;
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
public class YdeTypeRuleService extends CrudService<YdeTypeRuleDao, YdeTypeRule> {

    /**
     * 获取单条数据
     *
     * @param ydeTypeRule
     * @return
     */
    @Override
    public YdeTypeRule get(YdeTypeRule ydeTypeRule) {
        return super.get(ydeTypeRule);
    }

    /**
     * 查询分页数据
     *
     * @param ydeTypeRule 查询条件
     * @return
     */
    @Override
    public Page<YdeTypeRule> findPage(YdeTypeRule ydeTypeRule) {
        return super.findPage(ydeTypeRule);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param ydeTypeRule
     */
    @Override
    @Transactional(readOnly = false)
    public void save(YdeTypeRule ydeTypeRule) {
        super.save(ydeTypeRule);
    }

    /**
     * 更新状态
     *
     * @param ydeTypeRule
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(YdeTypeRule ydeTypeRule) {
        super.updateStatus(ydeTypeRule);
    }

    /**
     * 删除数据
     *
     * @param ydeTypeRule
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(YdeTypeRule ydeTypeRule) {
        super.delete(ydeTypeRule);
    }

}