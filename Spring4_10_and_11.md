# Spring4

## Spring集成持久化机制的数据访问（基于关系型数据库）

#### 了解Spring的数据访问

* 数据访问对象（DAO|Repository）：将数据访问的功能放到一个或多个专注于此任务的组件。良好的DAO|Repository以接口形式暴露。

  [![无标题.png](https://i.loli.net/2018/04/22/5adc646c7de8b.png)](https://i.loli.net/2018/04/22/5adc646c7de8b.png)

* 数据访问异常体系 :Sprin提供平台无关的持久化异常，既比JDBC的异常体系丰富，又没有绑定特定的持久化方案（Hibernate异常体系）。

* 数据访问模板化：数据访问过程中可分成固定以及可变部分，分别划分为两个不同的类：模板以及回调。模板负责处理管理资源，事务控制以及异常处理。回调负责查询语句，参数绑定以及整理结果集。
[![Spring数据访问模板化.png](https://i.loli.net/2018/04/24/5adef627125a8.png)](https://i.loli.net/2018/04/24/5adef627125a8.png)
  * Spirng提供的常用数据访问模板，每个模板对应每种持久化机制。
    * jdbc.core.JdbcTemplate：JDBC连接。
    * jdbc.core.namedparam.NamedParameterJdbcTemplate：支持命名参数的JDBC连接。
    * orm.hibernate3.HibernateTemplate：hibernate3.x以上的session。
    * orm.jpa.JpaTemplate：Java持久化API的实体管理器。

* 配置数据源：
  * 使用JNDI配置数据源：适用WEB应用生产环境
    * JNDI：JavaNaming and Directory Interface，即Java命名与目录接口，为Java应用程序提供命名服务（如DNS映射服务）以及目录服务（如互联网上寻找离散资源服务）。
    * 配置：
    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <datasources>
        <local-tx-datasource>
            <jndi-name>MySqldruidDataSource</jndi-name>
            <connection-url>jdbc:mysql://localhost:3306/lw</connection-url>
            <driver-class>com.mysql.jdbc.Driver</driver-class>
            <user-name>root</user-name>
            <password>rootpassword</password>
            <exception-sorter-class-name>
                org.jboss.resource.adapter.jdbc.vendor.MySQLExceptionSorter
            </exception-sorter-class-name>
            <metadata>
                <type-mapping>mySQL</type-mapping>
            </metadata>
        </local-tx-datasource>
    </datasources>
    ```
    ```
    <!-- Spring应用上下文 -->
    <!-- XML Config -->
    <jee:jndi-lookup id="dataSource" jndi-name="/jdbc/MySqldruidDataSource" resource-ref="true" />
    <!-- Java Config -->
    @Bean
    public JndiObjectFactoryBean dataSource(){
      JndiObjectFactoryBean jndiObjectFB = new JndiObjectFactoryBean();
      jndiObjectFB.setJndiName("/jdbc/MySqldruidDataSource");
      jndiObjectFB.setResource(true);
      jndiObjectFB.setProxyInterface(javax.sql.DataSource.class);
    }
    ```
  * 使用连接池配置数据源：适用WEB应用生产环境
    * 主流连接池：[Druid](https://github.com/alibaba/druid)
    * 配置：[DruidDataSource](tool.oschina.net/uploads/apidocs/druid0.26/com/alibaba/druid/pool/DruidDataSource.html)
    ```
    <!-- Spring应用上下文 -->
    <!-- XML Config -->
    <bean id = "dataSource" class = "com.alibaba.druid.pool.DruidDataSource" destroy-method = "close">    
        <!-- 数据库基本信息配置 -->  
        <property name = "url" value = "${url}" />    
        <property name = "username" value = "${username}" />    
        <property name = "password" value = "${password}" />    
        <property name = "driverClassName" value = "${driverClassName}" />    
        <property name = "filters" value = "${filters}" />    
        <!-- 最大并发连接数 -->  
        <property name = "maxActive" value = "${maxActive}" />  
        <!-- 初始化连接数量 -->  
        <property name = "initialSize" value = "${initialSize}" />  
        <!-- 配置获取连接等待超时的时间 -->  
        <property name = "maxWait" value = "${maxWait}" />  
        <!-- 最小空闲连接数 -->  
        <property name = "minIdle" value = "${minIdle}" />    
        <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->  
        <property name = "timeBetweenEvictionRunsMillis" value ="${timeBetweenEvictionRunsMillis}" />  
        <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->  
        <property name = "minEvictableIdleTimeMillis" value ="${minEvictableIdleTimeMillis}" />    
        <property name = "validationQuery" value = "${validationQuery}" />    
        <property name = "testWhileIdle" value = "${testWhileIdle}" />    
        <property name = "testOnBorrow" value = "${testOnBorrow}" />    
        <property name = "testOnReturn" value = "${testOnReturn}" />    
        <property name = "maxOpenPreparedruidDataSourcetatements" value ="${maxOpenPreparedruidDataSourcetatements}" />  
        <!-- 打开 removeAbandoned 功能 -->  
        <property name = "removeAbandoned" value = "${removeAbandoned}" />  
        <!-- 1800 秒，也就是 30 分钟 -->  
        <property name = "removeAbandonedTimeout" value ="${removeAbandonedTimeout}" />  
        <!-- 关闭 abanded 连接时输出错误日志 -->     
        <property name = "logAbandoned" value = "${logAbandoned}" />  
    </ bean >
    <!-- Java Config -->
    InputStream in = RootConfig.class.getClassLoader().getResourceAsStream("database.properties");
    Properties properties = new Properties();
    properties.load(in);
    @Bean
    public DruidDataSource dataSource(){
      DruidDataSource druidDataSource = new DruidDataSource ();
      druidDataSource.setDriverClassName(properties.getProperty("jdbc.driver"));
      druidDataSource.setUsername(properties.getProperty("jdbc.user"));
      druidDataSource.setPassword(properties.getProperty("jdbc.password"));
      druidDataSource.setUrl(properties.getProperty("jdbc.url"));
      druidDataSource.setInitialSize(Integer.parseInt(properties.getProperty("jdbc.initialSize")));
      druidDataSource.setMaxActive(Integer.parseInt(properties.getProperty("jdbc.maxActive")));
      druidDataSource.setMinIdle(Integer.parseInt(properties.getProperty("jdbc.minIdle")));
      druidDataSource.setMaxWait(Integer.parseInt(properties.getProperty("jdbc.maxWait")));
      druidDataSource.setFilters("stat,wall,log4j");
      druidDataSource.setPoolPreparedruidDataSourcetatements(true);
      druidDataSource.setMaxPoolPreparedruidDataSourcetatementPerConnectionSize(20);
    }
    ```
  * 使用JDBC驱动配置数据源：适用WEB应用开发环境
    * JDBC驱动：
      * DriverManagerDataSource：在每个连接请求时都会返回一个新建的连接。
      * SimpleDriverDataSource：工作方式与DriverManagerDataSource相似。但其直接JDBC驱动来解决特点环境下的类加载问题。
      * SingleConnectionDataSource:在每个连接请求时都会返回同一个连接。
    * 配置  
    ```
    <!-- Spring应用上下文 -->
    <!-- XML Config -->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource"
          p:driverClassName="org.h2.Driver"
          p:url="jdbc:h2:tcp://localhost/~/spitter"
          p:username="garden"
          p:password="garden" />
    <!-- Java Config -->
    @Bean
    public DataSource dataSource(){
      DriverManagerDataSource ds = new DriverManagerDataSource();
      ds.setDriverClassName("org.h2.Driver");
      ds.setUrl("jdbc:h2:tcp://localhost/~/spitter");
      ds.setUsername("garden");
      ds.setPassword("garden");
    }
    ```
  * 使用嵌入式配置数据源：适用WEB应用测试环境。
    * 嵌入式：嵌入外部sql文件作为数据源。
    * 配置
    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	     xmlns:c="http://www.springframework.org/schema/c"
    	     xmlns:jdbc="http://www.springframework.org/schema/jdbc"
    	     xsi:schemaLocation="http://www.springframework.org/schema/jdbc     http://www.springframework.org/schema/jdbc/spring-jdbc-3.1.xsd
    		   http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
           <jdbc:embedded-database id="dataSource" type="H2">
               <jdbc:script location="classpath:spittr/db/jdbc/schema.sql" />
               <jdbc:script location="classpath:spittr/db/jdbc/test-data.sql" />
           </jdbc:embedded-database>
    </beans>
    ```
  * [使用Profile选择数据源](https://github.com/Garden12138/Spring4/blob/master/Spring4_3.md)

#### Spring集成JDBC的数据访问
* JDBC建立于SQL基础之上，能够更好地对数据访问的性能进行调优。使用JDBC包括使用原生JDBC和模板JDBC，原生JDBC职责不专一，除了进行数据访问还需负责管理事务与资源，处理异常等任务，故不建议使用。
* 使用模板JDBC：Spring的JDBC框架承担了资源管理和异常的工作，我们只需关心从数据库读写数据的代码。
  * JdbcTemplate：支持简单的JDBC数据库访问功能以及基于索引参数的查询。
  * NamedParameterJdbcTemplate：支持简单的JDBC数据库访问功能以及基于参数命名方式的查询。
* Demo：[JdbcTemplate API](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html)
```
//Spring应用上下文
@Bean
public JdbcTemplate jdbcTemplate(DataSource dataSource){
  return new JdbcTemplate(dataSource);
}
```
```
//Repository|dao层
@Repository
public class JdbcRepositoryImpl implements JdbcRepository{
  @Autowired
  private JdbcOperations jdbcOperations;    //JdbcOperations实现JdbcTemplate所有操作。
  ...  
}
```
#### Spring集成ORM的数据访问
