package lifecycle;

import org.springframework.beans.factory.InitializingBean;

/**
 * InitializingBean 인터페이스는 코드를 스프링에 불필요하게 결합하므로 사용하지 않는 것이 좋습니다.
 * import를 보면 스프링에 결합되어 있음을 알 수 있습니다.
 * 스프링에 결합되어 있어서 다른 컨테이너나 프레임워크로 전환하기 어려워집니다.
 * import org.springframework.beans.factory.InitializingBean;
 */
public class InitializingSampleObject implements InitializingBean {

  private String message;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.message = "InitializingSampleObject.afterPropertiesSet() method called";
  }

  public String getMessage() {
    return message;
  }
}
