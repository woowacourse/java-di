package extensions;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class CustomAnnotationBeanPostProcessor implements BeanPostProcessor {

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    for (Field field : bean.getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(CustomAnnotation.class)) {
        System.out.printf("CustomAnnotation found on property %s of bean %s%n", field.getName(), beanName);
      }
    }
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }
}
