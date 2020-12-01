package com.haimacy.jpautil.support.expression;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName LogicalExpression
 * @Description 逻辑条件表达式 用于复杂条件时使用，如单属性多对应值的OR查询等
 * @Author KinBill Dang
 * @Date 2020/8/25 10:10
 * @Version 1.0
 **/
public class LogicalExpression implements Criterion {

    private Criterion[] criterion;

    private Operator operator;

    public LogicalExpression(Criterion[] criterions, Operator operator) {
        this.criterion = criterions;
        this.operator = operator;
    }

    @Override
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        for (Criterion value : this.criterion) {
            predicates.add(value.toPredicate(root, query, builder));
        }
        switch (operator) {
            case OR:
                return builder.or(predicates.toArray(new Predicate[0]));
            case AND:
                return builder.and(predicates.toArray(new Predicate[0]));
            default:
                return null;
        }
    }
}
