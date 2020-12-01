package com.haimacy.jpautil.demo;



public class Example {

//    @Resource
//    EntityManager em;

//    @Resource
//    VehicleInfoRepository repository;

    public void test() {
        //创建构建动态语句工厂 泛型第一个是接收的类型，第二个是返回的类型
//        CriteriaPlus<VehicleInfo, Temp> criteria = new CriteriaPlus<>();
        //添加查询字段 可以查子表字段
//        criteria.addAll(Restrictions.pickSome("id","driverInfoList.mobile","driverInfoList.idCard"));

        //添加条件 可以添加子表字段条件
//        criteria.add(Restrictions.eq("driverInfoList.idCard", "xxx1", true));
//        criteria.add(Restrictions.eq("driverInfoList.username", "xxx1", true));

        //可以添加聚合函数
//            criteria.add(Restrictions.sum("id", "id"));
//            String[] name = {"张三", "王五"};
//            criteria.add(Restrictions.in("name", Arrays.asList(name)));

        //添加group by 字段
//            criteria.add(Restrictions.groupBy("mobile"));

        //可以使用普通的findAll方法
//        List<VehicleInfo> tuples = repository.findAll(criteria);

        //可以添加自定义转换的类型 与泛型相同
//        List<Temp> temp = criteria.findResult(em, VehicleInfo.class, Temp.class);

        //可以直接使用List Tuple类作为返回，详情自己查找Tuple用法
//        List<Tuple> tuples = criteria.findResult(em, VehicleInfo.class);
//        for (Tuple tuple : tuples) {
//            Object id = tuple.get("id");
//        }
        //如果对象是有关联关系的，因为懒加载机制不能直接输出
//        System.out.println(tuples);X
    }
}