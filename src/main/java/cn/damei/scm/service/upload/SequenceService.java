package cn.damei.scm.service.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.damei.scm.repository.upload.SequenceDao;

@Service
public class SequenceService {

	@Autowired
	private SequenceDao sequenceDao;

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
