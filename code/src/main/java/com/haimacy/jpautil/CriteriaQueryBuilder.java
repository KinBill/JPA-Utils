package com.haimacy.jpautil;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * 适用于做sum、avg、count等运算时使用，并且查询条件不固定，需要动态生成predicate</p>
 * 如select sum(a), count(b), count distinct(c) from table where a = ? & b = ?
 *
 * @author KinBill Dang
 */
public interface CriteriaQueryBuilder<T, B> extends Specification<T> {

    /**
     * 功能描述: 构建 select 字段
     *
     * @param builder builder
     * @param root    root
     * @return java.util.List<javax.persistence.criteria.Selection < ?>>
     * @Author KinBill Dang | gdin0036
     * @Date 2020/8/27
     **/
    List<Selection<?>> buildSelections(CriteriaBuilder builder, Root<T> root);

    /**
     * 功能描述: 构建 groupBy 条件
     *
     * @param root 动态查询构造器
     * @return java.util.List<javax.persistence.criteria.Expression < ?>>
     * @Author KinBill Dang | gdin0036
     * @Date 2020/8/27
     **/
    List<Expression<?>> buildGroupBy(Root<T> root);

    /**
     * 功能描述: 构建 groupBy 条件
     *
     * @param root 动态查询构造器
     * @return java.util.List<javax.persistence.criteria.Expression < ?>>
     * @Author KinBill Dang | gdin0036
     * @Date 2020/8/27
     **/
    List<Order> buildOrderBy(CriteriaBuilder builder, Root<T> root);

    /**
     * 功能描述: 获取查询结果
     *
     * @param entityManager 动态查询构造器
     * @param fromClass     当前查询 class
     * @param parseClass    需要返回的 class
     * @param pageable      分页条件
     * @return java.lang.Object
     * @Author KinBill Dang | gdin0036
     * @Date 2020/8/27
     **/
    Object findResult(Class<T> fromClass, Class<B> parseClass, Pageable pageable);
}
