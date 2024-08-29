package ioc;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@Configuration
@Import({ PrototypeIntoSingleton.class, SingletonIntoPrototype.class })
public class BeanScopesConfig {

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) // @Bean 뿐만 아니라 @Component와 함께 사용 가능
  public SampleObject singletonBean() {
    return new SampleObject();
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public SampleObject prototypeBean() {
    return new SampleObject();
  }
}
