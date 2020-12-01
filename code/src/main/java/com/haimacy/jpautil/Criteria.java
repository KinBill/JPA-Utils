package com.haimacy.jpautil;

import javax.persistence.criteria.*;

import com.haimacy.jpautil.support.expression.Criterion;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

/**
 * @ClassName Criteria
 * @Description TODO
 * @Author KinBill Dang
 * @Date 2020/8/25 09:48
 * @Version 1.0
 **/
@SuppressWarnings("ALL")
public class Criteria<T> implements Specification<T> {

    private List<Criterion> criterionList = new ArrayList<>();

    /**
     * 创建线程独立的JOIN 变量，反之重复JOIN
     */
    private static ThreadLocal<Map<String, Join>> joinMap = ThreadLocal.withInitial(HashMap::new);

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        try {
            query.distinct(true);
            if (!criterionList.isEmpty()) {
                List<Predicate> predicates = new ArrayList<>();
                for (Criterion c : criterionList) {
                    predicates.add(c.toPredicate(root, query, builder));
                }
                // 将所有条件用 and 联合起来
                if (predicates.size() > 0) {
                    return builder.and(predicates.toArray(new Predicate[0]));
                }
            }
            return builder.conjunction();
        } finally {
            joinMap.remove();
        }
    }

    public static Map<String, Join> getJoinMap() {
        return joinMap.get();
    }

    /**
     * 增加简单条件表达式
     */
    public void add(Criterion criterion) {
        if (criterion != null) {
            criterionList.add(criterion);
        }
    }


}