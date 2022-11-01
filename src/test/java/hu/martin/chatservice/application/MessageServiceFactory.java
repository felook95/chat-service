package hu.martin.chatservice.application;

import hu.martin.chatservice.application.port.InMemoryMessageRepository;
import hu.martin.chatservice.application.port.MessageRepository;

public class MessageServiceFactory {

  public static MessageService withDefaults() {
    MessageRepository messageRepository = new InMemoryMessageRepository();
    return new DefaultMessageService(messageRepository);
  }
}
