
## 一日之计项目后端




关于controllerAdvice:

统一返回格式，自动拦截后端给前端的返回值。
https://juejin.cn/post/7023751970531835918

关于多项目打包，参照：

https://blog.csdn.net/qq_42875345/article/details/110790933

本项目github地址：

https://github.com/BriSky-2021/plantary_micorservice
dev分支

关于jenkins地址，访问

http://124.70.182.243:8080/


#### mongodb的entity映射：
类的嵌入情况时，比如User下面有SleepInfo，就创建两个类，一个类中放另外一个类的List

```java
@Data
@Document("User")
public class User {
    private String name;
    private Integer age;
    /**此处省略**/
    private List<UserExerciseInfo> exerciseInfoList;
}

@Data
public class UserExerciseInfo {
    private String date;
    private Integer total_time;
    private Integer steps;
    private String intensity;
}

```


#### mongodb的两种使用方法:

1.mongoTemplate
灵活

引用：
@Autowired
private MongoTemplate mongoTemplate;

插入：

直接构建User，然后直接插入
mongoTemplate.insert(user)

条件查询
```java
Query query = new Query(Criteria.where("name").is("wyt").and("age").is(21));
List<User> users = mongoTemplate.find(query, User.class);
```

修改：
```java

        Query query = new Query(Criteria.where("name").is("wyt"));
        Update update = new Update();
        update.set("height",user.getHeight());
        update.set("weight",user.getWeight());
        update.set("passwd","123456");
        //调用mongoTemplate的修改方法实现修改
        mongoTemplate.upsert(query, update, User.class);

```

删除：
```java
Query query = new Query(Criteria.where("_id").is("60b4b3ca861699233d33f3e2"));
        mongoTemplate.remove(query, User.class);
```

2.mongodb Repository

可以在repository内部写like:
```java
@Query(value = "{'phone':'?0','passwd':'?1'}")
Optional<User> validatePasswd(String phone, String passwd);
```

### Response说明
参考了https://juejin.cn/post/7023751970531835918

ControllerAdvice拦截器，配置在edu/tongji/plantary/user/response/BaseResponseBodyAdvice.java

可用于给后端给前端的返回值添加code和msg

添加新的controller类在@ControllerAdvice(basePackages = {"edu.tongji.plantary.user.controller","edu.tongji.plantary.schedule.controller"})