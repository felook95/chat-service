package hu.martin.chatter;

import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.application.DefaultConversationService;
import hu.martin.chatter.application.port.ConversationRepository;
import hu.martin.chatter.application.port.InMemoryConversationRepository;
import hu.martin.chatter.application.port.InMemoryMessageRepository;
import hu.martin.chatter.application.port.MessageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatterConfiguration {

  @Bean
  ConversationService conversationService() {
    ConversationRepository conversationRepository = new InMemoryConversationRepository();
    MessageRepository messageRepository = new InMemoryMessageRepository();
    return new DefaultConversationService(conversationRepository, messageRepository);
  }

}
