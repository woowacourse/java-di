package ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetterMovieLister {

  // SetterMovieLister는 MovieFinder에 대한 의존성을 가진다.
  // 생성자 주입 방식처럼 final을 쓸 수 없다.
  private MovieFinder movieFinder;

  @Autowired
  // setter 메서드를 사용하여 스프링 컨테이너가 MovieFinder를 주입한다.
  public void setMovieFinder(MovieFinder movieFinder) {
    this.movieFinder = movieFinder;
  }

  public MovieFinder getMovieFinder() {
    return movieFinder;
  }
}
