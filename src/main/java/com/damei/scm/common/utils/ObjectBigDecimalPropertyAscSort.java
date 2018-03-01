package com.damei.scm.common.utils;

import org.apache.commons.beanutils.BeanUtilsBean;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Created by weiys on 16/6/21.
 */
public class ObjectBigDecimalPropertyAscSort implements Comparator {
    //比较的字段
    private String comparatorFieldName;

    public ObjectBigDecimalPropertyAscSort() {
    }

    public ObjectBigDecimalPropertyAscSort(String comparatorFieldName) {
        this.comparatorFieldName = comparatorFieldName;
    }

    @Override
    public int compare(Object o1, Object o2) {
        try {
            Object propValue1 = BeanUtilsBean.getInstance().getPropertyUtils().getProperty(o1, comparatorFieldName);
            Object propValue2 = BeanUtilsBean.getInstance().getPropertyUtils().getProperty(o2, comparatorFieldName);
            if (((BigDecimal) propValue1).doubleValue() > ((BigDecimal) propValue2).doubleValue()) {
                return 1;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
