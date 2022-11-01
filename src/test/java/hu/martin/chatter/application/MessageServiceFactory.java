package hu.martin.chatter.application;

import hu.martin.chatter.application.port.InMemoryMessageRepository;
import hu.martin.chatter.application.port.MessageRepository;

public class MessageServiceFactory {

  public static MessageService withDefaults() {
    MessageRepository messageRepository = new InMemoryMessageRepository();
    return new DefaultMessageService(messageRepository);
  }
}
