package hu.martin.chatter;

import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.application.DefaultConversationService;
import hu.martin.chatter.application.port.ConversationRepository;
import hu.martin.chatter.adapter.out.inmemory.InMemoryConversationRepository;
import hu.martin.chatter.adapter.out.inmemory.InMemoryMessageRepository;
import hu.martin.chatter.application.port.MessageRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ChatterTestConfiguration {

  @Bean
  ConversationService conversationService() {
    ConversationRepository conversationRepository = new InMemoryConversationRepository();
    MessageRepository messageRepository = new InMemoryMessageRepository();
    return new DefaultConversationService(conversationRepository, messageRepository);
  }

}
