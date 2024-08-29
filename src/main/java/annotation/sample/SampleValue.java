package annotation.sample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SampleValue {

  private final String value;
  private final String defaultValue;

  public SampleValue(@Value("${value.name}") final String value,
                     @Value("${value.default:기본값}") final String defaultValue) {
    this.value = value;
    this.defaultValue = defaultValue;
  }

  public String getValue() {
    return value;
  }

  public String getDefaultValue() {
    return defaultValue;
  }
}
