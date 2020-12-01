package com.haimacy.jpautil.support.expression;


import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

/**
 * @ClassName IExpression
 * @Description TODO
 * @Author KinBill Dang
 * @Date 2020/8/20 10:35 上午
 * @Version 1.0
 **/
public interface IGroupBy {
    Expression<?> getGroupBy(Root<?> root);
}
