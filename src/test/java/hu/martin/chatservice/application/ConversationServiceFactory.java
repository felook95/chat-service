package hu.martin.chatservice.application;

import hu.martin.chatservice.application.port.ConversationRepository;
import hu.martin.chatservice.application.port.InMemoryConversationRepository;
import hu.martin.chatservice.application.port.InMemoryMessageRepository;
import hu.martin.chatservice.application.port.MessageRepository;

public class ConversationServiceFactory {

  public static ConversationService withDefaults() {
    MessageRepository messageRepository = new InMemoryMessageRepository();
    return withMessageRepository(messageRepository);
  }

  public static ConversationService withMessageRepository(MessageRepository messageRepository) {
    ConversationRepository conversationRepository = new InMemoryConversationRepository();
    return new DefaultConversationService(conversationRepository, messageRepository);
  }

  public static ConversationService with(ConversationRepository conversationRepository,
      MessageRepository messageRepository) {
    return new DefaultConversationService(conversationRepository, messageRepository);
  }

  public static ConversationService withConversationRepository(
      ConversationRepository conversationRepository) {
    MessageRepository messageRepository = new InMemoryMessageRepository();
    return new DefaultConversationService(conversationRepository, messageRepository);
  }
}
