package annotation.sample;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.HashMap;
import java.util.Map;

// 스프링이 아닌 자바 표준의 @Inject와 @Named를 사용해봅시다.
@Named
public class MovieRecommender {

  private final CustomerPreferenceDao customerPreferenceDao;
  private Map<String, MovieCatalog> movieCatalogs = new HashMap<>();

  // @Autowired 대신 @Inject를 사용했습니다.
  // 스프링 4.3부터는 생성자가 하나라면 @Autowired를 생략해도 자동 주입이 가능합니다.
  // @Inject를 주석 처리하고 테스트를 실행하여 정상 동작하는지 확인해보세요.
  @Inject
  public MovieRecommender(final CustomerPreferenceDao customerPreferenceDao) {
    this.customerPreferenceDao = customerPreferenceDao;
  }

  public CustomerPreferenceDao getCustomerPreferenceDao() {
    return customerPreferenceDao;
  }

  // setter도 @Autowired 또는 @Inject를 사용할 수 있습니다.
  // 컬렉션 타입도 자동 주입이 가능합니다.
  @Inject
  public void setMovieCatalogs(final Map<String, MovieCatalog> movieCatalogs) {
    this.movieCatalogs = movieCatalogs;
  }

  public Map<String, MovieCatalog> getMovieCatalogs() {
    return movieCatalogs;
  }
}
