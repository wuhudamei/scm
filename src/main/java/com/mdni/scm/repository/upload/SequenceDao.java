package com.mdni.scm.repository.upload;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.service.upload.SequenceService.SequenceTable;

/**
 * @author zhangmin
 */
@MyBatisRepository
public interface SequenceDao {

	/**
	 * 返回 sequence的当前值
	 */
	Integer getCurVal(SequenceTable seqTab);

	/**
	 * 增加sequence值
	 */
	void next(SequenceTable seqTab);

	void insert(String seqTab);
}
