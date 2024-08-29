package ioc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"ioc"})
public class AppConfig {

  @Bean
  public SampleObject sampleObject() {
    return new SampleObject();
  }

  @Bean(name = {"firstName", "secondName"})
  public SampleObject sampleAlias() {
    return new SampleObject();
  }

  @Bean
  public SampleFactoryObject sampleFactoryObject() {
    return SampleFactoryObject.create();
  }
}
