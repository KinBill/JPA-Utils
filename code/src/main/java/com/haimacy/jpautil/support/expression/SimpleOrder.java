package com.haimacy.jpautil.support.expression;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

/**
 * @ClassName SimpleExpression
 * @Description 条件表达式
 * @Author KinBill Dang
 * @Date 2020/8/25 09:49
 * @Version 1.0
 **/
public class SimpleOrder implements IOrder {
    /**
     * 属性名
     */
    private String fieldName;
    /**
     * 计算符
     */
    private Operator operator;

    public SimpleOrder(String fieldName, Operator operator) {
        this.fieldName = fieldName;
        this.operator = operator;
    }


    @Override
    public String getAlisa() {
        return fieldName;
    }

    @Override
    public Order getOrder(Root<?> root, CriteriaBuilder builder) {

        switch (operator) {
            case ASC:
                return builder.asc(root.get(fieldName));
            case DESC:
                return builder.desc(root.get(fieldName));
            default:
                return null;
        }
    }

    @Override
    public Order getOrder(CriteriaBuilder builder, Expression expression) {

        switch (operator) {
            case ASC:
                return builder.asc(expression);
            case DESC:
                return builder.desc(expression);
            default:
                return null;
        }
    }
}