package com.haimacy.jpautil.utils;

import com.haimacy.jpautil.Criteria;
import com.haimacy.jpautil.CriteriaPlus;
import com.haimacy.jpautil.annotation.FieldName;
import com.haimacy.jpautil.support.expression.Criterion;
import com.haimacy.jpautil.support.expression.SimpleSelector;
import com.haimacy.jpautil.support.restrictions.Restrictions;
import lombok.extern.slf4j.Slf4j;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JpaParamsUtils
 * @Description Jpa参数工具类
 * @Author KinBill Dang
 * @Date 2020/8/25 17:31
 * @Version 1.0
 **/
@Slf4j
public class JpaParamsUtils {

    /**
     * 功能描述: 添加一个类作为返回字段
     *
     * @return
     * @Author KinBill Dang
     * @Date 2020/11/9
     **/
    public static <T, B> void addSelect(
            CriteriaPlus<T, B> criteria,
            Class selectClass) {
        Field[] fs = selectClass.getDeclaredFields();
        //设置所有字段都能通过反射获取，包括私有的
        Field.setAccessible(fs, true);
        List<SimpleSelector> simpleSelectors = new ArrayList<>(fs.length);
        for (Field f : fs) {
            //判断是否含有FieldName注解，并把对应注解的value()映射成字段名称
            if (f.isAnnotationPresent(FieldName.class)) {
                String child = f.getAnnotation(FieldName.class).value();
                simpleSelectors.add(Restrictions.pick(child));
            } else {
                simpleSelectors.add(Restrictions.pick(f.getName()));
            }
        }
        criteria.addAll(simpleSelectors);
    }

    /**
     * 功能描述: 添加一个map作为参数
     *
     * @return
     * @Author KinBill Dang
     * @Date 2020/11/9
     **/
    public static <T> void addParams(
            Criteria<T> criteria,
            Map<String, Object> paramEquals) {

        if (paramEquals != null && paramEquals.size() > 0) {
            for (Map.Entry<String, Object> entry : paramEquals.entrySet()) {
                criteria.add(Restrictions.eq(entry.getKey(), entry.getValue().toString(), true));
            }

        }
    }

    /**
     * 功能描述: 添加一个对象作为参数
     *
     * @return
     * @Author KinBill Dang
     * @Date 2020/11/9
     **/
    public static <T> void addParams(
            Criteria<T> criteria,
            Object obj) {

        if (obj == null) {
            return;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String name = property.getName();
                // 过滤class属性
                if (!"class".equals(name)) {
                    Field field = obj.getClass().getDeclaredField(name);
                    //判断是否含有FieldName注解，并把对应注解的value()映射成字段名称，key映射成逻辑符号，eq，like
                    if (field.isAnnotationPresent(FieldName.class)) {
                        FieldName annotation = field.getAnnotation(FieldName.class);
                        name = annotation.value().isEmpty() ? field.getName() : annotation.value();
                        Criterion.Operator key = annotation.key();
                        // 得到property对应的getter方法并调用获取对应的值
                        Method getter = property.getReadMethod();
                        Object value = getter.invoke(obj);
                        if (value != null) {
                            if (value instanceof Collection<?>
                                    && key == Criterion.Operator.IN
                                    && ((Collection<?>) value).size() > 0) {
                                criteria.add(Restrictions.condition(name, value, key));
                            } else if (!value.toString().isEmpty()) {
                                criteria.add(Restrictions.condition(name, value.toString(), key));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
