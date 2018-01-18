package com.mdni.scm.service.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdni.scm.repository.upload.SequenceDao;

/**
 * @author zhangmin
 */
@Service
public class SequenceService {

	@Autowired
	private SequenceDao sequenceDao;

	/**
	 * 增加sequence的值,然后返回增加后sequence值
	 */
	public Integer getNextVal(SequenceTable seqTab) {
		sequenceDao.next(seqTab);
		Integer nextVal = sequenceDao.getCurVal(seqTab);
		if (nextVal == null) {
			sequenceDao.insert(seqTab.name());
			nextVal = 1;
		}
		return nextVal;
	}

	public static enum SequenceTable {
		UPLOAD, ADV_TYPE(4), INSTORE_CODE(6);

		//返回的code sequence,格式化成固定的宽度
		private int fixWidth;

		SequenceTable() {
		}

		SequenceTable(int width) {
			this.fixWidth = width;
		}

		public int getFixWidth() {
			return this.fixWidth;
		}
	}
}
