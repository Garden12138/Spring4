# Spring4

## 2.装配Bean

#### 装配Bean的方式

* 自动化装配Bean
  * 组件扫描：扫描组件，创建于应用上下文中的Bean。
  * 自动装配：Spring自动满足bean之间的依赖。
  * 适用范围：适用于具有强依赖关系的bean
* 基于Java配置装配Bean
  * 创建配置类：创建JavaConfig类。
  * 声明Bean：于配置类中声明bean。
  * 适用范围：适用于具有强依赖关系并且具有生成bean的细节的bean。
* 基于XML配置装配Bean
  * 适用范围：适用于引入第三方组件以及可选择性依赖（即可注入字面量）的bean

#### 自动化装配Bean

* 组件扫描
  * 创建组件（声明组件使用@Component或@Named,可添加value声明创建的bean ID，默认ID为第一个字母小写的类名）

  ```
  package com.web.spring4.bean;
  public interface CompactDisc {
    public void play();
  }
  ```

  ```
  package com.web.spring4.bean.impl;
  import org.springframework.stereotype.Component;
  import com.web.spring4.bean.CompactDisc;
  @Component("jca")
  public class JayChouAlbum implements CompactDisc{
    private String songs[] = {"夜曲"};
    private String artist = "周杰伦";
    @Override
    public void play() {
      // TODO Auto-generated method stub
      System.out.println("正在播放："+ songs[0] + "-" +artist);
    }
  }
  ```

  ```
  package com.web.spring4.bean;
  public interface MediaPlayer {
    public void play();
  }
  ```

  * 自动装配（组件装配使用@Resource或Autowired）

  ```
  package com.web.spring4.bean.impl;
  import javax.annotation.Resource;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Component;
  import com.web.spring4.bean.MediaPlayer;
  @Component
  public class CDPlayer implements MediaPlayer{
    //	@Resource
    private JayChouAlbum cd1;    /*参数装配*/
    //	@Autowired /*@Resource不可用于构造函数*/
    public CDPlayer(JayChouAlbum cd1) {    /*构造器装配**/
      super();
      this.cd1 = cd1;
    }
    public CDPlayer() {
      super();
    }
    public JayChouAlbum getCd1() {
      return cd1;
    }
    @Resource /*setter构造必需含有空构造函数*/
    public void setCd1(JayChouAlbum cd1) {  /*setter装配*/
      this.cd1 = cd1;
    }
    @Override
    public void play() {
      // TODO Auto-generated method stub
      cd1.play();
    }
  }

  ```

  * 创建扫描组件启动器（使用@Configuration和@ComponentScan）

  ```
  /*基于Java扫描组件启动器*/
  package com.web.spring4.config;
  import org.springframework.context.annotation.ComponentScan;
  import org.springframework.context.annotation.Configuration;
  import com.web.spring4.bean.impl.JayChouAlbum;
  import com.web.spring4.bean.impl.JayChouAlbum2;
  @Configuration
  /*扫描目标目录及其子目录包，默认扫描本类所在目录*/
  //@ComponentScan("com.web.spring4.bean")   
  //@ComponentScan("com.web.spring4.bean.impl")
  //@ComponentScan(basePackages="com.web.spring4.bean.impl")
  //@ComponentScan(basePackages={"com.web.spring4.bean.impl"})
  /*扫描目标目录类所在目录*/
  //@ComponentScan(basePackageClasses={JayChouAlbum.class,JayChouAlbum2.class})
  @ComponentScan(basePackageClasses={JayChouAlbum.class})
  public class CDPlayerConfig {
  }
  ```

  ```
  /*基于XML扫描组件启动器*/
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:context="http://www.springframework.org/schema/context" xmlns:p="http://www.springframework.org/schema/p"
    xmlns:mvc="http://www.springframework.org/schema/mvc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:tx="http://www.springframework.org/schema/tx" xmlns:task="http://www.springframework.org/schema/task"
    xsi:schemaLocation="http://www.springframework.org/schema/beans  
      http://www.springframework.org/schema/beans/spring-beans-3.0.xsd  
      http://www.springframework.org/schema/context  
      http://www.springframework.org/schema/context/spring-context.xsd" >

      <context:component-scan base-package="com.web.spring4.bean.impl" />
  </beans>
  ```

  * 测试器

  ```
  package com.web.spring4.test;
  import static org.junit.Assert.*;
  import javax.annotation.Resource;
  import org.junit.Test;
  import org.junit.runner.RunWith;
  import org.springframework.test.context.ContextConfiguration;
  import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
  import com.web.spring4.bean.CompactDisc;
  import com.web.spring4.bean.MediaPlayer;
  import com.web.spring4.bean.impl.JayChouAlbum;
  import com.web.spring4.bean.impl.JayChouAlbum2;
  import com.web.spring4.config.CDPlayerConfig;
  import com.web.spring4.bean.impl.*;
  @RunWith(SpringJUnit4ClassRunner.class)
  //@ContextConfiguration(classes=CDPlayerConfig.class)
  @ContextConfiguration(value = { "classpath:config.xml" })
  public class CDPlayerTest {
    //	@Resource
    //	private CompactDisc cd;
    @Resource
    private JayChouAlbum cd1;
    @Resource
    private JayChouAlbum2 cd2;
    @Resource
    private MediaPlayer CDPlayer;
    //	private CDPlayer CDPlayer;
    @Test
    public void cdShouldNotBeNull(){
      //assertNotNull(cd);
      assertNotNull(cd1);
      cd1.play();
      assertNotNull(cd2);
      cd2.play();
      System.out.println("---end--");
      CDPlayer.play();
    }
  }
  ```

  * PS:若装配多个实现的接口，会由于不确定具体的bean而装配失败

#### 基于Java配置装配Bean
* 创建配置类（该类应包含在Spring上下文中如何创建bean的细节，使用@Configuration表明该类是一个配置类）
* 声明Bean（于配置类中声明bean，@Bean告诉Spring这个方法将会返回一个对象并注册为应用上下文的bean，实质上拦截所有对该方法的调用，返回的是一个已创建的bean）

```
package com.web.spring4.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.web.spring4.bean.CompactDisc;
import com.web.spring4.bean.MediaPlayer;
import com.web.spring4.bean.impl.*;

@Configuration
public class CDPlayerConfig2 {

	/*声明简单的bean*/
	@Bean    /*可使用@Bean("")指定Bean ID，默认为其修饰的方法名*/
	public  CompactDisc jayChouAlbum(){
		return new JayChouAlbum();
	}

	@Bean
	public  CompactDisc jayChouAlbum2(){
		return new JayChouAlbum2();
	}

	/*声明依赖关系的bean*/
//	@Bean
//	public CDPlayer cDPlayer(){
//		return new CDPlayer(jayChouAlbum());
//	}

	@Bean
	public CDPlayer cDPlayer2(JayChouAlbum jayChouAlbum){
		return new CDPlayer(jayChouAlbum);
	}

}
```

#### 基于XML配置装配Bean
