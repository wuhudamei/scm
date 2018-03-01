package com.damei.scm.repository.upload;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.service.upload.SequenceService.SequenceTable;

@MyBatisRepository
public interface SequenceDao {

	Integer getCurVal(SequenceTable seqTab);

	void next(SequenceTable seqTab);

	void insert(String seqTab);
}
