package com.haimacy.jpautil.support.expression;

import javax.persistence.criteria.*;

/**
 * @ClassName SimpleExpression
 * @Description 条件表达式
 * @Author KinBill Dang
 * @Date 2020/8/25 09:49
 * @Version 1.0
 **/
public class SimpleExpression extends SimpleJoinExpression implements Criterion {
    /**
     * 属性名
     */
    private String fieldName;
    /**
     * 对应值
     */
    private Object value;
    /**
     * 计算符
     */
    private Operator operator;

    public SimpleExpression(String fieldName, Object value, Operator operator) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        Path expression;
        try {
            expression = joinCondition(root, fieldName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        switch (operator) {
            case EQ:
                return builder.equal(expression, value);
            case NE:
                return builder.notEqual(expression, value);
            case LIKE:
                return builder.like((Expression<String>) expression, value.toString());
            case LT:
                return builder.lessThan(expression, (Comparable) value);
            case GT:
                return builder.greaterThan(expression, (Comparable) value);
            case LTE:
                return builder.lessThanOrEqualTo(expression, (Comparable) value);
            case GTE:
                return builder.greaterThanOrEqualTo(expression, (Comparable) value);
            case IS_MEMBER:
                return builder.isMember(value, expression);
            case IS_NOT_MEMBER:
                return builder.isNotMember(value, expression);
            case IN:
                return builder.in(expression).value(value);
            default:
                return null;
        }
    }

}