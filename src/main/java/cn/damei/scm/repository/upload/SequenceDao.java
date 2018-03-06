package cn.damei.scm.repository.upload;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.service.upload.SequenceService;

@MyBatisRepository
public interface SequenceDao {

	Integer getCurVal(SequenceService.SequenceTable seqTab);

	void next(SequenceService.SequenceTable seqTab);

	void insert(String seqTab);
}
