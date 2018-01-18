package com.mdni.scm.service.dictionary;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.dictionary.Dictionary;
import com.mdni.scm.repository.dictionary.DictionaryDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @describe 字典 Service
 * @author Ryze
 * @date 2017-8-11 11:22:00
 *
 */
@Service
public class DictionaryService extends CrudService<DictionaryDao, Dictionary> {
    /**
     *  根据 字典 父id 获取 子节点 列表
     * @param parentId
     * @return
     */
   public List<Dictionary> findByParentId(Long parentId){
       return  entityDao.findByParentId(parentId);
   }

    /**
     * 增加
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(Dictionary entity) {
        if (entity == null)
            return;
        entityDao.insert(entity);
        List<Dictionary> list = entity.getList();
        batch(list,entity.getId());
    }

    /**
     * 修改
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(Dictionary entity) {
        if (entity == null)
            return;
        entityDao.update(entity);
        entityDao.deleteByParentId(entity.getId());
        batch(entity.getList(),entity.getId());
    }

    /**
     * 批量添加
     * @param list
     * @param id
     */
    private void batch(List<Dictionary> list,Long id){
        if(list!=null &&  list.size()>0){
            for (Dictionary dictionary :list) {
                dictionary.setParentId(id);
                dictionary.setCreateAccount(WebUtils.getLoggedUser().getLoginName());
                dictionary.setCreateTime(new Date());
                entityDao.insert(dictionary);
            }
        }
    }

    /**
     * 删除  父节点 子节点也删除
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        if (id == null || id < 1)
            return;
        entityDao.deleteByParentId(id);
        entityDao.deleteById(id);
    }

    /**
     *  根据 字典 父类值 获取 子节点 列表 下拉框
     * @param dicValue
     * @return
     */
    public List<Dictionary> findByParentValue(String dicValue){
        return  entityDao.findByParentValue(dicValue);
    }
}
