package com.mdni.scm.repository.dictionary;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.dictionary.Dictionary;

import java.util.List;

/**
 * @describe 字典dao
 * @author Ryze
 * @date 2017-8-11 11:12:32
 *
 */
@MyBatisRepository
public interface DictionaryDao extends CrudDao<Dictionary> {
    /**
     *  根据 字典 父id 获取 子节点 列表
     * @param parentId
     * @return
     */
    List<Dictionary> findByParentId(Long parentId);

    /**
     *  根据 字典 父类值 获取 子节点 列表 下拉框
     * @param dicValue
     * @return
     */
    List<Dictionary> findByParentValue(String dicValue);


    /**
     * 删除 子节点 通过父节点id
     * @param parentId
     */
    void deleteByParentId(Long parentId);

}