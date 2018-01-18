package com.mdni.scm.repository.reviewSizeResult;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.reviewSizeResult.ReviewSizeResult;

import java.util.List;

/**
 * <dl>
 * <dd>ReviewSizeResultDao: 复尺结果Dao</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/8/1</dd>
 * <dd>@author：Chaos</dd>
 * </dl>
 */
@MyBatisRepository
public interface ReviewSizeResultDao extends CrudDao<ReviewSizeResult> {
    List<ReviewSizeResult> getReviewSizeResultById(Long id);
}
