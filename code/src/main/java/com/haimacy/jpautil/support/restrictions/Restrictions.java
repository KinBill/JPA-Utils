package com.haimacy.jpautil.support.restrictions;

import com.haimacy.jpautil.support.expression.*;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName Restrictions
 * @Description 添加查询字段或者条件表达式
 * @Author KinBill Dang
 * @Date 2020/8/25 09:50
 * @Version 1.0
 **/
public class Restrictions {

    /**
     * 添加一个查询字段
     */
    public static SimpleSelector pick(String fieldName) {
        return new SimpleSelector(fieldName, ISelector.Operator.PICK);
    }

    /**
     * 添加多个查询字段
     */
    public static List<SimpleSelector> pickSome(String... fieldName) {
        List<SimpleSelector> simpleSelectors = new ArrayList<>(fieldName.length);
        for (String field : fieldName) {
            simpleSelectors.add(pick(field));
        }
        return simpleSelectors;
    }

    /**
     * 添加一个最大值查询字段
     */
    public static SimpleSelector max(String fieldName, String alias) {
        return new SimpleSelector(fieldName, ISelector.Operator.MAX, alias);
    }

    /**
     * 添加一个最小值查询字段
     */
    public static SimpleSelector min(String fieldName, String alias) {
        return new SimpleSelector(fieldName, ISelector.Operator.MIN, alias);
    }

    /**
     * 添加一个求和查询字段
     */
    public static SimpleSelector sum(String fieldName, String alias) {
        return new SimpleSelector(fieldName, ISelector.Operator.SUM, alias);
    }

    /**
     * 添加一个计算总条数查询字段
     */
    public static SimpleSelector count(String fieldName, String alias) {
        return new SimpleSelector(fieldName, ISelector.Operator.COUNT, alias);
    }

    /**
     * 添加一个计算总条数查询字段去重
     */
//    public static SimpleSelector countDistinct(String fieldName,String alias) {
//        return new SimpleSelector(fieldName, ISelector.Operator.COUNT_DISTINCT, alias);
//    }

    /**
     * 添加 order by 语句
     */
    public static GroupByExpression groupBy(String fieldName) {
        return new GroupByExpression(fieldName);
    }

    /**
     * 添加 group by 语句
     */
    public static SimpleOrder orderByAsc(String fieldName) {
        return new SimpleOrder(fieldName, IOrder.Operator.ASC);
    }

    /**
     * 添加 group by 语句
     */
    public static SimpleOrder orderByDesc(String fieldName) {
        return new SimpleOrder(fieldName, IOrder.Operator.DESC);
    }

    /**
     * 根据传入的条件动态添加条件
     */
    public static SimpleExpression condition(String fieldName, Object value, Criterion.Operator operator) {
        return new SimpleExpression(fieldName, value, operator);
    }

    /**
     * 等于
     */
    public static SimpleExpression eq(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && value == null) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Criterion.Operator.EQ);
    }

    /**
     * 集合包含某个元素
     */
    public static SimpleExpression hasMember(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && value == null) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Criterion.Operator.IS_MEMBER);
    }

    /**
     * 不等于
     */
    public static SimpleExpression ne(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && value == null) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Criterion.Operator.NE);
    }

    /**
     * 模糊匹配
     */
    public static SimpleExpression like(String fieldName, String value, boolean ignoreNull) {
        boolean valueNull = value == null || StringUtils.isEmpty(value);
        if (ignoreNull && valueNull) {
            return null;
        }
        return new SimpleExpression(fieldName, "%" + value + "%", Criterion.Operator.LIKE);
    }

    /**
     * 模糊匹配,不带百分号
     */
    public static SimpleExpression likeNoWith(String fieldName, String value, boolean ignoreNull) {
        boolean valueNull = value == null || StringUtils.isEmpty(value);
        if (ignoreNull && valueNull) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Criterion.Operator.LIKE);
    }

    /**
     * 大于
     */
    public static SimpleExpression gt(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && value == null) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Criterion.Operator.GT);
    }

    /**
     * 小于
     */
    public static SimpleExpression lt(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && value == null) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Criterion.Operator.LT);
    }

    /**
     * 小于等于
     */
    public static SimpleExpression lte(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && value == null) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Criterion.Operator.LTE);
    }

    /**
     * 大于等于
     */
    public static SimpleExpression gte(String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && value == null) {
            return null;
        }
        return new SimpleExpression(fieldName, value, Criterion.Operator.GTE);
    }

    /**
     * 并且
     */
    public static LogicalExpression and(Criterion... criterions) {
        return new LogicalExpression(criterions, Criterion.Operator.AND);
    }

    /**
     * 或者
     */
    public static LogicalExpression or(Criterion... criterions) {
        return new LogicalExpression(criterions, Criterion.Operator.OR);
    }

//    /**
//     * 包含于
//     */
//    @SuppressWarnings("rawtypes")
//    public static LogicalExpression in(String fieldName, Collection value, boolean ignoreNull) {
//        if (ignoreNull && (value == null || value.isEmpty())) {
//            return null;
//        }
//        SimpleExpression[] ses = new SimpleExpression[value.size()];
//        int i = 0;
//        for (Object obj : value) {
//            ses[i] = new SimpleExpression(fieldName, obj, Criterion.Operator.EQ);
//            i++;
//        }
//        return new LogicalExpression(ses, Criterion.Operator.OR);
//    }

    public static SimpleExpression in(String fieldName, Collection value) {
        return new SimpleExpression(fieldName, value, Criterion.Operator.IN);
    }

    /**
     * 集合包含某几个元素，譬如可以查询User类中Set<String> set包含"ABC","bcd"的User集合，
     * 或者查询User中Set<Address>的Address的name为"北京"的所有User集合
     * 集合可以为基本类型或者JavaBean，可以是one to many或者是@ElementCollection
     * @param fieldName
     * 列名
     * @param value
     * 集合
     * @return
     * expresssion
     */
//    public static LogicalExpression hasMembers(String fieldName, Object... value) {
//        SimpleExpression[] ses = new SimpleExpression[value.length];
//        int i = 0;
//        //集合中对象是基本类型，如Set<Long>，List<String>
//        Criterion.Operator operator = Criterion.Operator.IS_MEMBER;
//        //集合中对象是JavaBean
//        if (fieldName.contains(".")) {
//            operator = Criterion.Operator.EQ;
//        }
//        for (Object obj : value) {
//            ses[i] = new SimpleExpression(fieldName, obj, operator);
//            i++;
//        }
//        return new LogicalExpression(ses, Criterion.Operator.OR);
//    }
}