#### Mybatis 参考文档

* **[Mybatis](http://www.mybatis.org/mybatis-3/)**
* **[MyBatis Generator](http://www.mybatis.org/generator/)**

#### Mybatis 相关依赖
```xml
<!--mybatis与spring boot集成-->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>
<!--mysql数据库驱动-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
<!--mybatis逆向工程-->
<dependency>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-core</artifactId>
    <version>1.3.5</version>
</dependency>
```

#### Mybatis 配置相关
##### 1.配置mapper接口文件扫描路径。
```java
@SpringBootApplication
@MapperScan(basePackages = "com.example.mybatis.demo.mybatis.mapper")
```
在启动类中添加@MapperScan注解，若不配置@MapperScan注解，则必须在每个Mapper接口类中添加@Mapper注解。

##### 2.application.yml中配置mybatis相关内容
```xml
mybatis:
    mapper-locations: classpath:mapper/*.xml
    type-aliases-package: com.example.mybatis.demo.mybatis.entity
```
* `mapper-locations` 指定Mapper接口绑定的mapper.xml所在路径。
若不配置该路径，会报`org.apache.ibatis.binding.BindingException: Invalid bound statement (not found)`错误。

* `type-aliases-package` 指定entity在哪个包中，避免同名class时找不到对应的bean。

##### 3.数据源配置
```xml
spring:
    datasource:
        driverClassName: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/MybatisDemo?useUnicode=true&characterEncoding=utf-8
        username: root
        password:
```

##### 4.运算符替换
避免特殊符号与xml文件中的格式符混淆引起xml文件解析错误，用如下符号替换。

其中`<`是会与xml中的标签符号产生歧义的，其他的暂未发现。

| 原始符号 | 替换符号 | 
| ------ | ------ | 
| < | `&lt;` |
| <= | `&lt;=` |
| > | `&gt;` |
| >= | `&gt;=` |
| & | `&amp;` |
| ' | `&apos;` |
| " | `&quot;` |


#### Mybatis 日志
这里使用logback对日志进行输出。logback详细使用参考 **[logback文档](https://logback.qos.ch/manual/index.html)**。

* 首先引入日志相关依赖
```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version>
</dependency>
```

* 配置logback.xml文件
```xml
<configuration scan="true" scanPeriod="60 seconds" debug="false">

    <!--定义彩色输出格式-->
    <property name="CONSOLE_LOG_PATTERN"
              value="%date{yyyy-MM-dd HH:mm:ss} | %highlight(%-5level) | %boldYellow(%thread) | %green(%logger) | %msg%n"/>

    <!--定义appender：输出到控制台-->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${CONSOLE_LOG_PATTERN}</pattern>
        </encoder>
    </appender>

    <!-- mybatis loggers 输出级别设为DEBUG -->
    <logger name="com.example.mybatis.demo.mybatis.mapper" level="DEBUG" additivity="false">
        <appender-ref ref="STDOUT"/>
    </logger>

    <root level="INFO">
        <appender-ref ref="STDOUT" />
    </root>

</configuration>
```

#### Mybatis 逆向工程配置

##### 1.不生成Example查询相关的内容，在`<table>`标签中添加以下内容。

```xml
<table tableName="%"
   enableCountByExample="false" enableUpdateByExample="false"
   enableDeleteByExample="false" enableSelectByExample="false"
   selectByExampleQueryId="false">
</table>
```

##### 2.每张表只生成一个实体类，避免Blob类型的字段单独生成一个实体类。
```xml
<context id="DB2Tables" targetRuntime="MyBatis3" defaultModelType="flat"></context>
```
`<context>`标签中指定`defaultModelType`属性为`flat`，默认值为`conditional`。指定`flat`后每张表就只会生成一个实体类。


#### Mybatis 操作

代码样例参考单元测试样例。

##### 1.selectKey标签设置

在INSERT语句中，可以通过设置`selectKey`标签获取插入记录的ID值。主键字段必须设为自增类型。

`<selectKey>`有个`order`属性
* 设为`before`，`<selectKey>`置于insert语句前面，通过`SELECT seq_user.nextval AS id` 获取序列（mysql中要创建序列）。
* 设为`after`，`<selectKey>`置于insert语句后面，通过`SELECT last_insert_id() AS id` 通过自增序列获取。

```xml
<insert id="insert" parameterType="com.example.mybatis.demo.mybatis.entity.User">
    insert into user (name, age, create_date)
    values (#{name,jdbcType=VARCHAR}, #{age,jdbcType=INTEGER}, #{createDate,jdbcType=TIMESTAMP})
    <selectKey keyProperty="id" order="AFTER" resultType="java.lang.Long">
        SELECT LAST_INSERT_ID() AS ID
    </selectKey>
</insert>
```

```java
int i = userMapper.insert(user);
log.info("Affect rows is： {}", i);
log.info("Insert Id is: {}", user.getId());
```
使用`selectKey`可以在调用插入接口后，通过`user.getId()`获取刚插入的主键。


##### 2.日期字段的作为查询条件。

数据库中字段`create_time`字段类型为`TIMESTAMP`，对应的Java实体类中的字段为 `private Date createDate;`。

mapper接口方法如下：
```java
public interface UserMapper {
    List<User> selectByDatetime(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
```
传入参数为`Date`类型。注意：多个参数传入需用@Param注解指名参数名称。

对应mapper.xml文件中的select语句写法如下：
```xml
<select id="selectByDatetime" parameterType="java.util.Date" resultMap="BaseResultMap">
    SELECT *
    FROM user
    WHERE
        create_date &gt; #{startDate, jdbcType=TIMESTAMP}
    AND create_date &lt; #{endDate, jdbcType=TIMESTAMP}
    ORDER BY create_date desc
</select>
```
`parameterType`指明传入参数类型为`java.util.Date`。Java中的`Date`类型可以与Mysql中的`TIMESTAMP`类型相对应。

注意：`Date()`函数获取的值必须经过格式化成`yyyy-MM-dd HH:mm:ss`， 与数据库存储的形式保持一致。


##### 3.批量插入操作。

通过`<foreach>`标签实现批量插入。

```xml
<insert id="batchInsert" parameterType="java.util.List" useGeneratedKeys="true" keyProperty="id">
    insert into user(name, age, create_date)
    values
    <foreach collection="list" item="item" index="index" separator=",">
        (
            #{item.name,jdbcType=VARCHAR},
            #{item.age,jdbcType=INTEGER},
            #{item.createDate,jdbcType=TIMESTAMP}
        )
    </foreach>
</insert>
```
`<foreach>`中的`collection`属性：
* List对象用 **`list`**
* 数组对象用 **`array`**
* Map对象用 **`map`**

通过设置`useGeneratedKeys="true" keyProperty="id"`，插入成功后list上的所有对象的ID属性都会设置插入时对应的ID值。


##### 4. 多参数查询

查询条件有多个参数的情况，可以使用Map集合保存参数。

```java
List<User> selectByMoreParams(Map map);
```

```xml
<select id="selectByMoreParams" parameterType="hashmap" resultMap="BaseResultMap">
    select
    <include refid="Base_Column_List" />
    from  user
    where age = #{age}
    and create_date &gt; #{startDate}
    and create_date &lt; #{endDate}
</select>
```
`#{}`里面的变量就是map集合对应的键值。单元测试如下：

```java
@Test
public void selectByMoreParams() {
    Map map = new HashMap<>();
    map.put("age", 18);
    map.put("startDate", "2018-09-15 22:34:02");
    map.put("endDate", "2018-09-16 22:34:02");
    List<User> result = userMapper.selectByMoreParams(map);
    log.info("user list size is : {}", result.size());
}
```
