package com.haimacy.jpautil.annotation;

import com.haimacy.jpautil.support.expression.Criterion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName FieldName
 * @Description 参数注解
 * @Author KinBill Dang
 * @Date 2020/8/25 16:06
 * @Version 1.0
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldName {

    String value() default "";

    Criterion.Operator key() default Criterion.Operator.EQ;

}
