package com.haimacy.jpautil.support.expression;


import com.haimacy.jpautil.exception.SimpleException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

/**
 * @ClassName SimpleSelector
 * @Description 查询字段的表达式
 * @Author KinBill Dang
 * @Date 2020/8/25 09:49
 * @Version 1.0
 **/
public class SimpleSelector extends SimpleJoinExpression implements ISelector {
    /**
     * 属性名
     */
    private String fieldName;
    /**
     * 计算符
     */
    private Operator operator;
    /**
     * 别名
     */
    private String alias;


    public SimpleSelector(String fieldName, Operator operator) {
        this.fieldName = fieldName;
        this.operator = operator;
    }

    public SimpleSelector(String fieldName, Operator operator, String alias) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.alias = alias;
    }

    @Override
    public Selection<?> getSelection(Root<?> root, CriteriaBuilder builder) {
        Path expression;
        try {
            expression = joinCondition(root, fieldName);
        } catch (SimpleException e) {
            e.printStackTrace();
            return null;
        }
        switch (operator) {
            case PICK:
                return expression;
            case SUM:
                return builder.sum(expression).alias(alias);
            case MAX:
                return builder.max(expression).alias(alias);
            case MIN:
                return builder.min(expression).alias(alias);
            case COUNT:
                return builder.count(expression).alias(alias);
            default:
                return null;
        }
    }

    @Override
    public String getAlis() {
        return alias;
    }
}