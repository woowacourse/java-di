package extensions;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("extensions")
public class BeanPostProcessorConfig {

  @CustomAnnotation
  private String customAnnotationField;
}
