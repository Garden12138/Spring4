# Spring4

## 1. 初识Spring

#### 1.1. 简化Java开发

* 最小侵入性编程和基于POJO的轻量级
  * 侵入式：类不需要强制实现框架接口和继承框架父类
  * POJO：Plain Old Java Object，简单老式Java对象，即简单普通Java类。
  *
```
 public class HelloWorldBean {
    public String sayHello(){
     return "Hello World";
   }
 }
 ```
* 通过依赖注入和面向接口编程实现松耦合
  * 依赖注入（DI）：使依赖关系中的依赖者只关心本身业务，不生产管理被依赖者，从而松散耦合。
  * 面向接口编程：定义与实现分离的一种设计模式。
  * 依赖注入的简单使用Demo
 ```
普通依赖关系
public class ConcreteService implements AbstractService{
  public ConcreteDao concreteDao;
  public ConcreteService(){
    this.ConcreteDao = new ConcreteDao();
  }
  public void business(){
    ConcreteDao.business();
  }
}
 ```
 ```
面向接口依赖关系
public class ConcreteService implements AbstractService{
  public AbstractDao abstractDao;
  public ConcreteService(AbstractDao abstractDao){
    this.abstractDao = abstractDao;
  }
  public void business(){
    abstractDao.business();
  }
}
 ```
 ```
 XML配置注入
 <bean id = "ConcreteServiceBean" class = "com.web.spring4.service.ConcreteService">
     <constructor-arg ref = "ConcreteDaoBean" />
 </bean>
 <bean id = "ConcreteDaoBean" class = "com.web.spring4.dao.ConcreteDaoBean">
 </bean>
 ```
 ```
 Java类配置注入
 @Configuration
 public class AbstractServiceConfig{
   @Bean
   public AbstractService AbstractService(){
     return new ConcreteService(AbstractDao());
   }
   @Bean
   public AbstractDao AbstractDao(){
     return new ConcreteDao();
   }
 }
 ```
 ```
 依赖注入使用
 public class DIMain{
   public static void main(String[] agrs) throws Exception{
     //加载上下文
     ClassPathXmlApplicationContext contetx = new ClassPathXmlApplicationContext("XML配置文件路径");
     /**
         //Java类配置注入方式加载上下文
         AnnotationConfigApplicationContext contetx = new AnnotationConfigApplicationContext("Java配置类路径");
     **/
     AbstractService AbstractService = contetx.getBean(AbstractService.class);
     AbstractService.business();
     contetx.close();
   }
 }
 ```
* 基于切面和惯例进行声明式编程
  * 面向切面编程：将遍布应用各处的功能分离出来形成可重用的组件，实现系统关注点分离的一项技术。即系统组件只关心本身业务逻辑，其余非本职责（事务管理，日志，安全服务）通过横切关注点融入该组件中。
  * 简单的面向切面编程DEMO
 ```
 //事务类
 public class Affair{
   public void begin(){
     System.out.println("affair begin");
   }
   public void close(){
     System.out.println("affair close");
   }
 }
 ```
 ```
 //业务类
 public class Business{
   public void action(){
     System.out.println("action...");
   }
 }
 ```
 ```
 //声明配置
 <aop:config>
     <aop:aspect ref = "Affair">    /**定义切面**/
         <aop:pointcut id = "Act" expression = "execution(* *.action(..))" />   /**定义切点**/
         <aop:before pointcut-ref="Act" method="begin"/>     /**前置声明**/
         <aop:after pointcut-ref="Act" method="close"/>      /**后置声明**/
     </aop:aspect>
 </aop:config>
 ```

* 通过切面和模板减少样板式代码
  * 模板:通用代码
  * 样板式代码:需重复使用代码

#### 1.2. 框架核心-容器

#### 1.3. Spring生态系统

#### 1.4. Spring4新功能
