package lifecycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LifecycleConfig {

  @Bean
  public InitializingSampleObject initializingSampleObject() {
    return new InitializingSampleObject();
  }

  @Bean
  public PostConstructSampleObject postConstructSampleObject() {
    return new PostConstructSampleObject();
  }

  // Bean 대상 객체의 메서드명에 close 또는 shutdown가 있으면 소멸 메서드를 자동으로 유추합니다.
  // PojoSampleObject 클래스의 close() 메서드를 감지하여 destroyMethod를 자동으로 설정합니다.
  @Bean(initMethod = "init") //, destroyMethod = "close")
  public PojoSampleObject pojoSampleObject() {
    return new PojoSampleObject();
  }

  @Bean
  public DisposableSampleObject disposableSampleObject() {
    return new DisposableSampleObject();
  }

  @Bean
  public PreDestroySampleObject preDestroySampleObject() {
    return new PreDestroySampleObject();
  }
}
