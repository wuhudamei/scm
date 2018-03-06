package cn.damei.scm.repository.dataArrangement;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.dataArrangement.MaterialBaseMain;
import cn.damei.scm.entity.eum.dataArrangement.MetarialTypeEnum;
import cn.damei.scm.common.MyBatisRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface MaterialBaseMainDao extends CrudDao<MaterialBaseMain> {

    List<MaterialBaseMain> findByContractIdAndMetarialType(@Param("contractId") Long contractId , @Param("metarialType") MetarialTypeEnum metarialType);


    List<Map<String,Object>> findForTotal(@Param("contractId") Long contractId , @Param("metarialType") MetarialTypeEnum metarialType);

    void delete(Map<String,Object> paramMap);
}