package cn.damei.scm.repository.dictionary;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.dictionary.Dictionary;
import cn.damei.scm.common.MyBatisRepository;

import java.util.List;

@MyBatisRepository
public interface DictionaryDao extends CrudDao<Dictionary> {

    List<Dictionary> findByParentId(Long parentId);


    List<Dictionary> findByParentValue(String dicValue);



    void deleteByParentId(Long parentId);

}