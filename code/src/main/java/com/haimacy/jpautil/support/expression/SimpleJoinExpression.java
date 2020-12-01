package com.haimacy.jpautil.support.expression;

import com.haimacy.jpautil.Criteria;
import com.haimacy.jpautil.exception.SimpleException;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName SimpleJoinExpression
 * @Description 添加join条件
 * @Author KinBill Dang | gdin0036
 * @Date 2020/8/25 16:22
 * @Version 1.0
 **/
class SimpleJoinExpression {

    Path joinCondition(Root<?> root, String fieldName) throws SimpleException {
        Path expression;
        Map<String, Join> joinMap = Criteria.getJoinMap();

        //此处是表关联数据，注意仅限一层关联，如user.address，
        //查询user的address集合中，address的name为某个值

        if (fieldName.contains(".")) {
            String[] names = StringUtils.split(fieldName, ".");
            if (names == null || names.length !=2){
                throw new SimpleException("传入的表达式错误["+fieldName+"]");
            }
            //获取该属性的类型，Set？List？Map？
            expression = root.get(names[0]);
            Class clazz = expression.getJavaType();

            if (clazz.equals(Set.class)) {
                SetJoin setJoin;
                if (joinMap.containsKey(names[0])) {
                    setJoin = (SetJoin) joinMap.get(names[0]);
                } else {
                    setJoin = root.joinSet(names[0]);
                    joinMap.put(names[0], setJoin);
                }
                expression = setJoin.get(names[1]);
            } else if (clazz.equals(List.class)) {
                ListJoin listJoin;
                if (joinMap.containsKey(names[0])) {
                    listJoin = (ListJoin) joinMap.get(names[0]);
                } else {
                    listJoin = root.joinList(names[0]);
                    joinMap.put(names[0], listJoin);
                }
                expression = listJoin.get(names[1]);
            } else if (clazz.equals(Map.class)) {
                MapJoin mapJoin;
                if (joinMap.containsKey(names[0])) {
                    mapJoin = (MapJoin) joinMap.get(names[0]);
                } else {
                    mapJoin = root.joinMap(names[0]);
                    joinMap.put(names[0], mapJoin);
                }
                expression = mapJoin.get(names[1]);
            } else {
                //是many to one时
                expression = expression.get(names[1]);
            }

        } else {
            //单表查询
            expression = root.get(fieldName);
        }
        return expression;
    }
}
