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
1.配置mapper接口文件扫描路径。
```java
@SpringBootApplication
@MapperScan(basePackages = "com.example.mybatis.demo.mybatis.mapper")
```
在启动类中添加@MapperScan注解，若不配置@MapperScan注解，则必须在每个Mapper接口类中添加@Mapper注解。

2.application.yml中配置mybatis相关内容
```xml
mybatis:
    mapper-locations: classpath:mapper/*.xml
    type-aliases-package: com.example.mybatis.demo.mybatis.entity
```
* `mapper-locations` 指定Mapper接口绑定的mapper.xml所在路径。
若不配置该路径，会报`org.apache.ibatis.binding.BindingException: Invalid bound statement (not found)`错误。

* `type-aliases-package` 指定entity在哪个包中，避免同名class时找不到对应的bean。

3.数据源配置
```xml
spring:
    datasource:
        driverClassName: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/MybatisDemo?useUnicode=true&characterEncoding=utf-8
        username: root
        password:
```

4.运算符替换
避免运算符号如`<`、`>`与xml文件中的格式符混淆引起xml文件解析错误，用如下符号替换。

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
这里使用logback对日志进行输出。

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

1.不生成Example查询相关的内容，在`<table>`标签中添加以下内容。

```xml
<table tableName="%"
   enableCountByExample="false" enableUpdateByExample="false"
   enableDeleteByExample="false" enableSelectByExample="false"
   selectByExampleQueryId="false">
</table>
```

2.每张表只生成一个实体类，避免Blob类型的字段单独生成一个实体类。
```xml
<context id="DB2Tables" targetRuntime="MyBatis3" defaultModelType="flat"></context>
```
`<context>`标签中指定`defaultModelType`属性为`flat`，默认值为`conditional`。指定`flat`后每张表就只会生成一个实体类。