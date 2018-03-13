  # Spring4

#### 自动化装配Bean的歧义性

* 场景：自动化装配Bean包括JavaConfig装配Bean时使用参数自动注入被依赖者时（实质上是自动扫描已创建该类型的Bean）的时候是使用接口注入，若该接口多个实现，则Spring无法得知需要注入的具体实现，故会抛出异常。
* 解决方式：
  * 使用首选的Bean，使用注解@Primary标识被依赖Bean。

  ```
  - 使用前例：
  - JayChouAlbum，JayChouAlbum2 实现 CompactDisc
  - CDPlayer 依赖 CompactDisc
  ```

  ```
  - 自动装配
  package com.web.spring4.bean.impl;
  import org.springframework.context.annotation.Primary;
  import org.springframework.stereotype.Component;
  import com.web.spring4.bean.CompactDisc;
  @Component
  @Primary
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
  - JavaConfig
  @Bean
  @Primary
  public CompactDisc JayChouAlbum{
    return new JayChouAlbum();
  }
  ```

  ```
  - XML
  <bean id = "jayChouAlbum" class = "com.web.spring4.bean.impl.JayChouAlbum" primary = "true"/>
  ```

  * 使用限定符的Bean，使用注解@Qualifier标识依赖者注入，参数为被依赖者Bean ID，由于对类名称或方法名的任意改动会导致限定符失效，即限定符也伴随着改动。故常使用自定义的限定符，即依赖者和被依赖者约定相同的限定符（此时的限定符用于描述bean的属性），由于常会出现相同属性的Bean，故常使用自定义@Qualifier注解多次描述Bean以至于区分开Bean。

  ```
  - @Qualifier标识依赖者注入处（属性，setter）
  ```

  ```
  package com.web.spring4.bean.impl;
  import javax.annotation.Resource;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.beans.factory.annotation.Qualifier;
  import org.springframework.stereotype.Component;
  import com.web.spring4.bean.CompactDisc;
  import com.web.spring4.bean.MediaPlayer;
  @Component
  public class CDPlayer implements MediaPlayer{
    @Resource
    @Qualifier("jayChouAlbum2")    /*限定符即字符串为Bean ID*/
    private CompactDisc cd1;
  //@Autowired /*@Resource不可用于构造函数*/
  //@Qualifier("jayChouAlbum2")  /*@Qualifier不能用于构造函数*/
    public CDPlayer(CompactDisc compactDisc) {
      super();
      this.cd1 = compactDisc;
    }
    public CDPlayer() {
      super();
    }
  //@Resource /*setter构造必需含有空构造函数*/
  //@Qualifier("jayChouAlbum2")
    public void setCd1(CompactDisc cd1) {
      this.cd1 = cd1;
    }
    @Override
    public void play() {
      // TODO Auto-generated method stub
      cd1.play();
    }
  }

  ```

  ```
  - 自定义限定符
  ```

  ```
  package com.web.spring4.bean.impl;
  import javax.annotation.Resource;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.beans.factory.annotation.Qualifier;
  import org.springframework.stereotype.Component;
  import com.web.spring4.bean.CompactDisc;
  import com.web.spring4.bean.MediaPlayer;
  @Component
  public class CDPlayer implements MediaPlayer{
    @Resource
  //@Qualifier("jayChouAlbum2")    /*限定符即字符串为Bean ID*/
  	@QualiZfier("JCA")
	  private CompactDisc cd1;
  //@Autowired /*@Resource不可用于构造函数*/
  //@Qualifier("jayChouAlbum2")  /*@Qualifier不能用于构造函数*/
    public CDPlayer(CompactDisc compactDisc) {
      super();
      this.cd1 = compactDisc;
    }
    public CDPlayer() {
      super();
    }
  //@Resource /*setter构造必需含有空构造函数*/
  //@Qualifier("jayChouAlbum2")
    public void setCd1(CompactDisc cd1) {
      this.cd1 = cd1;
    }
    @Override
    public void play() {
      // TODO Auto-generated method stub
      cd1.play();
    }
  }
  ```

  ```
  package com.web.spring4.bean.impl;
  import org.springframework.beans.factory.annotation.Qualifier;
  import org.springframework.stereotype.Component;
  import com.web.spring4.bean.CompactDisc;
  @Component
  @Qualifier("JCA")
  public class JayChouAlbum2 implements CompactDisc{
    private String songs[] = {"告白气球"};
    private String artist = "周杰伦";
    @Override
    public void play() {
      // TODO Auto-generated method stub
      System.out.println("正在播放："+ songs[0] + "-" +artist);
    }
  }
  ```

  ```
  - 自定义@Qualifier注解
  ```

  ```
  package com.web.spring4.annotation;
  import java.lang.annotation.ElementType;
  import java.lang.annotation.Retention;
  import java.lang.annotation.RetentionPolicy;
  import java.lang.annotation.Target;
  import org.springframework.beans.factory.annotation.Qualifier;
  @Target({ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.METHOD,ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @Qualifier
  public @interface JCA {

  }
  ```

  ```
  package com.web.spring4.annotation;
  import java.lang.annotation.ElementType;
  import java.lang.annotation.Retention;
  import java.lang.annotation.RetentionPolicy;
  import java.lang.annotation.Target;
  import org.springframework.beans.factory.annotation.Qualifier;
  @Target({ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.METHOD,ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @Qualifier
  public @interface _1 {

  }
  ```

  ```
  package com.web.spring4.bean.impl;
  import javax.annotation.Resource;
  import org.springframework.stereotype.Component;
  import com.web.spring4.annotation.JCA;
  import com.web.spring4.annotation._2;
  import com.web.spring4.bean.CompactDisc;
  import com.web.spring4.bean.MediaPlayer;
  @Component
  public class CDPlayer implements MediaPlayer{

	@Resource
	@JCA
	@_2
	private CompactDisc cd1;
	public CDPlayer(CompactDisc compactDisc) {
		super();
		this.cd1 = compactDisc;
	}
	public CDPlayer() {
		super();
	}
	public void setCd1(CompactDisc cd1) {
		this.cd1 = cd1;
	}
	@Override
	public void play() {
		// TODO Auto-generated method stub
		cd1.play();
  }
  }
  ```

  ```
  package com.web.spring4.bean.impl;
  import org.springframework.stereotype.Component;
  import com.web.spring4.annotation.JCA;
  import com.web.spring4.annotation._2;
  import com.web.spring4.bean.CompactDisc;
  @Component
  @JCA
  @_2
  public class JayChouAlbum2 implements CompactDisc{
    private String songs[] = {"告白气球"};
    private String artist = "周杰伦";
    @Override
    public void play() {
      // TODO Auto-generated method stub
      System.out.println("正在播放："+ songs[0] + "-" +artist);
    }
  }
  ```


#### 条件化的Bean装配

* 适用场景：
  * bean只有在应用的类路径下包含特定的库时才创建。
  * 只有当其他某个特定Bean声明后才被创建。
  * 只有在某个特定环境变量才被创建。
* 使用方法
  * 注解@Conditional与@Bean一起使用于创建Bean的方法之上（使用JavaConfig装配）
  * 创建条件类，实现Condition接口。
    * [了解Condition接口的match方法参数](https://github.com/Garden12138/Spring4/blob/master/spring-source-code.md)
* Demo

```
package com.web.spring4.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import com.web.spring4.bean.CompactDisc;
import com.web.spring4.bean.condition.ChooseJayChouAlbum;
import com.web.spring4.bean.impl.*;
/**
 * JavaConfig - 条件化装配Bean
 * @author Garden
 * 2018年3月13日
 */
@Configuration
public class CDPlayerConfig3 {
	@Bean
	@Conditional(ChooseJayChouAlbum.class)
	public  CompactDisc jayChouAlbum(){
		return new JayChouAlbum();
	}
	@Bean
	public  CompactDisc jayChouAlbum2(){
		return new JayChouAlbum2();
	}
}
```

```
package com.web.spring4.bean.condition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
public class ChooseJayChouAlbum implements Condition{
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		// TODO Auto-generated method stub
		Object ob = context.getBeanFactory().getBean("jayChouAlbum2");
		return null == ob ? false : true;
	}
}
```

#### 特殊条件化Bean装配-Profile Bean

#### Bean的作用域

#### Spring表达式(spEL)
