package cn.damei.scm.common.utils;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Created by weiys on 16/5/5.
 * 比较对象的列 现在支持只BigDecimal类型字段比较
 * 按字段compFieldName的值升序排列,字段的值不能为空
 */
public final class EntityComparatorUtil implements Comparator {

    //要比较的字段名称
    private String compFieldName;

    public EntityComparatorUtil(String compFieldName) {
        this.compFieldName = compFieldName;
    }


    @Override
    public int compare(Object o1, Object o2) {
        Object object1 = ReflectionUtils.getFieldValue(o1, this.compFieldName);
        Object object2 = ReflectionUtils.getFieldValue(o2, this.compFieldName);
        double value1;
        double value2;
        if (object1 instanceof BigDecimal) {
            value1 = ((BigDecimal) object1).doubleValue();
            value2 = ((BigDecimal) object2).doubleValue();
            if (value1 > value2) {
                return 1;
            } else {
                return -1;
            }
        }
        return 0;
    }
}
