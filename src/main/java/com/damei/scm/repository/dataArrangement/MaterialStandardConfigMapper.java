package com.damei.scm.repository.dataArrangement;

import com.damei.scm.entity.dataArrangement.MaterialStandardConfig;
import java.util.List;

public interface MaterialStandardConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MaterialStandardConfig record);

    MaterialStandardConfig selectByPrimaryKey(Integer id);

    List<MaterialStandardConfig> selectAll();

    int updateByPrimaryKey(MaterialStandardConfig record);
}