package com.damei.scm.common.utils;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Created by weiys on 16/5/19.
 * BigDecimal列表排序
 */
public class BigDecimalListAscSort implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        if (((BigDecimal) o1).doubleValue() > ((BigDecimal) o2).doubleValue()) {
            return 1;
        } else {
            return -1;
        }
    }
}