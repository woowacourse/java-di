package lifecycle;

import jakarta.annotation.PostConstruct;

/**
 * `@PostConstruct`는 표준 자바에서 제공하는 어노테이션입니다.
 * 메서드에 `@PostConstruct`을 붙이면 컨테이너에서 객체 생성 후 호출됩니다.
 * 스프링이 아닌 다른 컨테이너에서도 사용할 수 있습니다.
 */
public class PostConstructSampleObject {

  private String message;

  @PostConstruct
  public void init() {
    this.message = "PostConstructSampleObject.init() method called";
  }

  public String getMessage() {
    return message;
  }
}
