/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.yida.modules.yde.dao.YdeOptionDao;
import com.yida.modules.yde.entity.YdeOption;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 选项Service
 *
 * @author Kevin
 * @version 2019-05-02
 */
@Service
@Transactional(readOnly = true)
public class YdeOptionService extends CrudService<YdeOptionDao, YdeOption> {

    /**
     * 获取单条数据
     *
     * @param ydeOption
     * @return
     */
    @Override
    public YdeOption get(YdeOption ydeOption) {
        return super.get(ydeOption);
    }

    /**
     * 查询分页数据
     *
     * @param ydeOption 查询条件
     * @return
     */
    @Override
    public Page<YdeOption> findPage(YdeOption ydeOption) {
        return super.findPage(ydeOption);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param ydeOption
     */
    @Override
    @Transactional(readOnly = false)
    public void save(YdeOption ydeOption) {
        super.save(ydeOption);
    }

    /**
     * 更新状态
     *
     * @param ydeOption
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(YdeOption ydeOption) {
        super.updateStatus(ydeOption);
    }

    /**
     * 删除数据
     *
     * @param ydeOption
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(YdeOption ydeOption) {
        super.delete(ydeOption);
    }

    @Transactional
    public void insertBatch(List<YdeOption> optionsToSave) {
        dao.insertBatch(optionsToSave);
    }

    @Transactional
    public void deleteBatch(List<YdeOption> optionsToDelete) {
        dao.phyDeleteByIds(new YdeOption(), optionsToDelete.stream()
                .map(YdeOption::getId).collect(Collectors.toList()));
    }
}