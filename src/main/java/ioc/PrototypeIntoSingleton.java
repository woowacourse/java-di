package ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("prototypeIntoSingleton")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PrototypeIntoSingleton {

  private final SampleObject prototypeBean;

  @Autowired
  public PrototypeIntoSingleton(@Qualifier("prototypeBean") SampleObject prototypeBean) {
    this.prototypeBean = prototypeBean;
  }

  public SampleObject getPrototypeBean() {
    return prototypeBean;
  }
}
