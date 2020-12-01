package com.haimacy.jpautil.support.expression;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

/**
 * @ClassName LastExpression
 * @Description 添加结尾表达式
 * @Author KinBill Dang
 * @Date 2020/8/20 12:36 下午
 * @Version 1.0
 **/
public class GroupByExpression implements IGroupBy {
    /**
     * 属性名
     */
    private String fieldName;

    public GroupByExpression(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public Expression<?> getGroupBy(Root<?> root) {
        return root.get(fieldName);
    }

}
