package com.mdni.scm.repository.dataArrangement;

import com.mdni.scm.entity.dataArrangement.MaterialStandardConfig;
import java.util.List;

public interface MaterialStandardConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MaterialStandardConfig record);

    MaterialStandardConfig selectByPrimaryKey(Integer id);

    List<MaterialStandardConfig> selectAll();

    int updateByPrimaryKey(MaterialStandardConfig record);
}