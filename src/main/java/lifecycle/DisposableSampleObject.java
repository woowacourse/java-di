package lifecycle;

import org.springframework.beans.factory.DisposableBean;

/**
 * InitializingBean 인터페이스와 마찬가지로 DisposableBean 인터페이스도 사용하지 않는 것이 좋습니다.
 */
public class DisposableSampleObject implements DisposableBean {

  private String message;

  @Override
  public void destroy() throws Exception {
    this.message = "DisposableSampleObject.destroy() method called";
  }

  public String getMessage() {
    return message;
  }
}
