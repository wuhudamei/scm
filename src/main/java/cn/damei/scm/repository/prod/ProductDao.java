package cn.damei.scm.repository.prod;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.eum.StatusEnum;
import cn.damei.scm.entity.prod.Product;

@MyBatisRepository
public interface ProductDao extends CrudDao<Product> {

	List<Product> adminSearch(Map<String, Object> params);

	Long adminSearchTotal(Map<String, Object> params);

	List<String> findAllCode();

	Product getByCode(Product product);

	List<Product> findByIdInList(@Param("ids") List<Long> productIdList, @Param("status") StatusEnum status);

	List<Product> findOrderedPositionList(@Param("categoryUrl") String categoryUrl);

	Product getByInfoById(Long id);
	
	Product getByMap(Map<String,String> map);
	
	List<Product> insertBatchReturnProperty(List<Product> list);
}