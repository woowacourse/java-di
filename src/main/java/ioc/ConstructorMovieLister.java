package ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConstructorMovieLister {

  // ConstructorMovieLister는 MovieFinder에 대한 의존성을 가진다.
  private final MovieFinder movieFinder;

  @Autowired
  // 생성자를 사용하여 스프링 컨테이너가 MovieFinder를 주입한다.
  // 인자가 하나인 생성자는 @Autowired 어노테이션을 생략할 수 있다.
  public ConstructorMovieLister(MovieFinder movieFinder) {
    this.movieFinder = movieFinder;
  }

  public MovieFinder getMovieFinder() {
    return movieFinder;
  }
}
