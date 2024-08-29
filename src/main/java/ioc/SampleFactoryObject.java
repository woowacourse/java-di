package ioc;

public class SampleFactoryObject {

  private SampleFactoryObject() {
  }

  public static SampleFactoryObject create() {
    return new SampleFactoryObject();
  }
}
