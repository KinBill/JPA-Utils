package com.haimacy.jpautil.support.expression;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

/**
 * @ClassName IExpression
 * @Description TODO
 * @Author KinBill Dang
 * @Date 2020/8/20 10:35 上午
 * @Version 1.0
 **/
public interface IOrder {

    enum Operator {
        ASC, DESC
    }

    String getAlisa();

    Order getOrder(Root<?> root, CriteriaBuilder builder);

    Order getOrder(CriteriaBuilder builder, Expression expression);

}
