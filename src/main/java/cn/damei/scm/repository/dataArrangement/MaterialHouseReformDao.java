package cn.damei.scm.repository.dataArrangement;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.dataArrangement.MaterialHouseReform;
import cn.damei.scm.common.MyBatisRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface MaterialHouseReformDao extends CrudDao<MaterialHouseReform> {

    List<MaterialHouseReform>findByContractId(@Param("contractId") Long contractId );

    void delete(Map<String, Object> paramMap);
}