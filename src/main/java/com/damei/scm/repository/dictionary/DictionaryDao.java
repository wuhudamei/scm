package com.damei.scm.repository.dictionary;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.dictionary.Dictionary;

import java.util.List;

@MyBatisRepository
public interface DictionaryDao extends CrudDao<Dictionary> {

    List<Dictionary> findByParentId(Long parentId);


    List<Dictionary> findByParentValue(String dicValue);



    void deleteByParentId(Long parentId);

}