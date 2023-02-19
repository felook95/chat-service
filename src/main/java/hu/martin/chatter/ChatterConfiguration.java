package hu.martin.chatter;

import hu.martin.chatter.adapter.out.inmemory.InMemoryMessageRepository;
import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.application.DefaultConversationService;
import hu.martin.chatter.application.port.ConversationRepository;
import hu.martin.chatter.application.port.MessageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatterConfiguration {

    @Bean
    public ConversationService conversationService(ConversationRepository conversationRepository,
                                                   MessageRepository messageRepository) {
        return new DefaultConversationService(conversationRepository, messageRepository);
    }

    @Bean
    public MessageRepository messageRepository() {
        return new InMemoryMessageRepository();
    }

}
