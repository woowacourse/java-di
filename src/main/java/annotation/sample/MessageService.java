package annotation.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageService {

  private final Map<String, MessageSender> messageSenders;

  @Autowired
  public MessageService(final Map<String, MessageSender> messageSenders) {
    this.messageSenders = messageSenders;
  }

  public MessageSender getMessageSender(final String name) {
    return messageSenders.get(name);
  }
}
