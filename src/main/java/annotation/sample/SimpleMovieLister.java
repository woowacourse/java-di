package annotation.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleMovieLister {

  private NoImplements noImplements;

  @Autowired(required = false)
  public void setNoImplements(NoImplements noImplements) {
    this.noImplements = noImplements;
  }

  public NoImplements getNoImplements() {
    return noImplements;
  }
}
