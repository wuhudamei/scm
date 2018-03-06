package cn.damei.scm.service.dictionary;

import cn.damei.scm.entity.dictionary.Dictionary;
import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.common.utils.WebUtils;
import cn.damei.scm.repository.dictionary.DictionaryDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DictionaryService extends CrudService<DictionaryDao, Dictionary> {

   public List<Dictionary> findByParentId(Long parentId){
       return  entityDao.findByParentId(parentId);
   }

    @Transactional(rollbackFor = Exception.class)
    public void insert(Dictionary entity) {
        if (entity == null)
            return;
        entityDao.insert(entity);
        List<Dictionary> list = entity.getList();
        batch(list,entity.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Dictionary entity) {
        if (entity == null)
            return;
        entityDao.update(entity);
        entityDao.deleteByParentId(entity.getId());
        batch(entity.getList(),entity.getId());
    }

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

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        if (id == null || id < 1)
            return;
        entityDao.deleteByParentId(id);
        entityDao.deleteById(id);
    }

    public List<Dictionary> findByParentValue(String dicValue){
        return  entityDao.findByParentValue(dicValue);
    }
}
