package lifecycle;

import jakarta.annotation.PreDestroy;

/**
 * `@PreDestroy`는 표준 자바에서 제공하는 어노테이션입니다.
 * 메서드에 `@PreDestroy`을 붙이면 컨테이너가 종료될 때 호출됩니다.
 * 스프링이 아닌 다른 컨테이너에서도 사용할 수 있습니다.
 */
public class PreDestroySampleObject {

  private String message;

  @PreDestroy
  public void close() {
    this.message = "PostConstructSampleObject.close() method called";
  }

  public String getMessage() {
    return message;
  }
}
