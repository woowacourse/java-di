package lifecycle;

/**
 * init() 메서드 호출을 통해 초기화합니다.
 * 컨테이너 구성에서 init() 메서드 호출을 명시하지 않으면 초기화를 누락할 수 있습니다.
 * LifecycleConfig 클래스에서 @Bean(initMethod = "init")으로 초기화 메서드를 명시합니다.
 */
public class PojoSampleObject {

  private String message;

  public void init() {
    this.message = "PojoSampleObject.init() method called";
  }

  public void close() {
    this.message = "PojoSampleObject.close() method called";
  }

  public String getMessage() {
    return message;
  }
}
