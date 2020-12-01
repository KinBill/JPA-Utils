package com.haimacy.jpautil;


import com.haimacy.jpautil.exception.SimpleException;
import com.haimacy.jpautil.support.expression.IGroupBy;
import com.haimacy.jpautil.support.expression.IOrder;
import com.haimacy.jpautil.support.expression.ISelector;
import com.haimacy.jpautil.support.expression.SimpleSelector;
import com.haimacy.jpautil.support.restrictions.Restrictions;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CriteriaPlus
 * @Description TODO
 * @Author KinBill Dang
 * @Date 2020/8/25 09:48
 * @Version 1.0
 **/
@SuppressWarnings("ALL")
public class CriteriaPlus<T, B> extends Criteria<T> implements CriteriaQueryBuilder<T, B> {
    private Class<T> fromClass;
    private Class<B> parseClass;
    private EntityManager em;

    public CriteriaPlus(Class<T> fromClass, Class<B> parseClass, EntityManager em) {
        this.fromClass = fromClass;
        this.parseClass = parseClass;
        this.em = em;
    }

    private List<ISelector> selectionList = new ArrayList<>();
    private List<IGroupBy> expressionList = new ArrayList<>();
    private List<IOrder> orderList = new ArrayList<>();

    @Override
    public List<Selection<?>> buildSelections(CriteriaBuilder builder, Root<T> root) {
        List<Selection<?>> selections = new ArrayList<>();
        for (ISelector iSelector : selectionList) {
            selections.add(iSelector.getSelection(root, builder));
        }
        return selections;
    }

    @Override
    public List<Expression<?>> buildGroupBy(Root<T> root) {
        List<Expression<?>> expressions = new ArrayList<>();
        for (IGroupBy expression : expressionList) {
            expressions.add(expression.getGroupBy(root));
        }
        return expressions;
    }

    @Override
    public List<Order> buildOrderBy(CriteriaBuilder builder, Root<T> root) {
        List<Order> orders = new ArrayList<>();
        for (IOrder iOrder : orderList) {
            boolean flag = true;
            for (ISelector iSelector : selectionList) {
                if (StringUtils.equals(iOrder.getAlisa(), iSelector.getAlis())) {
                    orders.add(iOrder.getOrder(builder, (Expression) iSelector.getSelection(root, builder)));
                    flag = false;
                    break;
                }
            }
            if (flag) {
                orders.add(iOrder.getOrder(root, builder));
            }
        }
        return orders;

    }

    public B findOneResult() throws SimpleException {
        List<B> listResult = findListResult();
        if (listResult.size() == 0) {
            return null;
        } else if (listResult.size() > 1) {
            throw new SimpleException("The number of returned entries is greater than 1 ");
        }
        return listResult.get(0);
    }

    public List<Tuple> findTupleResult() {
        return (List<Tuple>) findResult(fromClass, null, Pageable.unpaged());
    }

    public List<B> findListResult() {
        return (List<B>) findResult(fromClass, parseClass, Pageable.unpaged());
    }

    public Page<B> findPageResult(Pageable pageable) {
        return (Page<B>) findResult(fromClass, parseClass, pageable);
    }

    @Override
    public Object findResult(Class<T> fromClass, Class<B> parseClass, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaQuery<?> criteriaQuery;

        if (parseClass != null) {
            criteriaQuery = criteriaBuilder.createQuery(parseClass);
        } else {
            criteriaQuery = criteriaBuilder.createTupleQuery();
        }

        Root<T> root = criteriaQuery.from(fromClass);

        //构建select 语句
        if (!selectionList.isEmpty()) {
            criteriaQuery.multiselect(buildSelections(criteriaBuilder, root));
        }

        //构建groupBy
        if (!expressionList.isEmpty()) {
            criteriaQuery.groupBy(buildGroupBy(root));
        }
        //构建orderBy
        if (!orderList.isEmpty()) {
            criteriaQuery.orderBy(buildOrderBy(criteriaBuilder, root));
        }

        //构建where语句
        criteriaQuery.where(toPredicate(root, criteriaQuery, criteriaBuilder));
        TypedQuery<?> query = this.em.createQuery(criteriaQuery);

        //加入分页
        if (pageable.isPaged()) {
            boolean groupBy = false;
            CriteriaQuery<?> criteriaQueryCount;

            if (!expressionList.isEmpty()) {
                criteriaQueryCount = criteriaBuilder.createTupleQuery();
                groupBy = true;
            } else {
                criteriaQueryCount = criteriaBuilder.createQuery(Long.class);
            }

            Root<T> r = criteriaQueryCount.from(fromClass);

            //构建select 语句
            if (groupBy && !selectionList.isEmpty()) {
                SimpleSelector count = Restrictions.count("id", "id");
                criteriaQueryCount.multiselect(count.getSelection(r, criteriaBuilder));
                criteriaQueryCount.multiselect(buildSelections(criteriaBuilder, r));
            } else {
                criteriaQueryCount.multiselect(criteriaBuilder.count(r));
            }

            //构建groupBy
            if (groupBy) {
                criteriaQueryCount.groupBy(buildGroupBy(r));
            }

            //构建where语句
            criteriaQueryCount.where(toPredicate(r, criteriaQueryCount, criteriaBuilder));

            //构建count 语句
            TypedQuery<?> queryCount = this.em.createQuery(criteriaQueryCount);
            //加入分页条件
            query.setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize());

            boolean finalGroupBy = groupBy;
            return PageableExecutionUtils.getPage(query.getResultList(), pageable, () -> {
                return finalGroupBy ? queryCount.getResultList().size() : (Long) queryCount.getSingleResult();
            });
        }
        return query.getResultList();
    }

//    private static long executeCountTupleQuery(TypedQuery<?> query) {
//        Assert.notNull(query, "TypedQuery must not be null!");
//        List<Tuple> totalsTuple = (List<Tuple>) query.getResultList();
//        return totalsTuple.size();
//        List<Long> totals = totalsTuple.stream().map(e -> Long.valueOf((String) e.get(0))).collect(Collectors.toList());
//        return count(totals);
//    }

//    private static long count(List<Long> totals) {
//        long total = 0L;
//        Long element;
//
//        for (Iterator var4 = totals.iterator(); var4.hasNext(); total += element == null ? 0L : element) {
//            element = (Long) var4.next();
//        }
//        return total;
//    }

//    private static long executeCountQuery(TypedQuery<?> query) {
//        Assert.notNull(query, "TypedQuery must not be null!");
//        List<Long> totals = (List<Long>) query.getResultList();
//        return totals.size();
//        return count(totals);
//    }

    /**
     * 增加简单条件表达式
     */
    public void add(ISelector iSelector) {
        if (iSelector != null) {
            selectionList.add(iSelector);
        }
    }

    /**
     * 增加尾部语句
     */
    public void add(IGroupBy iExpression) {
        if (iExpression != null) {
            expressionList.add(iExpression);
        }
    }

    /**
     * 增加尾部语句
     */
    public void add(IOrder iOrder) {
        if (iOrder != null) {
            orderList.add(iOrder);
        }
    }

    public <R extends ISelector> void addAll(List<R> selectors) {
        selectionList.addAll(selectors);
    }
}
