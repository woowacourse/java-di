package annotation;

import annotation.sample.SampleValue;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackageClasses = SampleValue.class)
@PropertySource("classpath:application.properties")
public class PropertyConfig {
}
