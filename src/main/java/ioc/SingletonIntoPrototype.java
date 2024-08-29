package ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("singletonIntoPrototype")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SingletonIntoPrototype {

  private final SampleObject singletonBean;

  @Autowired
  public SingletonIntoPrototype(@Qualifier("singletonBean") SampleObject singletonBean) {
    this.singletonBean = singletonBean;
  }

  public SampleObject getSingletonBean() {
    return singletonBean;
  }
}
