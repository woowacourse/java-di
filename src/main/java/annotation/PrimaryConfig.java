package annotation;

import annotation.sample.MovieCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PrimaryConfig {

  private MovieCatalog movieCatalog;

  @Bean
  @Primary
  public MovieCatalog firstMovieCatalog() {
    return new MovieCatalog();
  }

  @Bean
  public MovieCatalog secondMovieCatalog() {
    return new MovieCatalog();
  }

  @Autowired
  public void setMovieCatalog(final MovieCatalog movieCatalog) {
    this.movieCatalog = movieCatalog;
  }

  public MovieCatalog getMovieCatalog() {
    return movieCatalog;
  }
}
