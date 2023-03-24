package hu.martin.chatter;

import hu.martin.chatter.adapter.out.inmemory.InMemoryMessageRepository;
import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.application.DefaultConversationService;
import hu.martin.chatter.application.port.ConversationRepository;
import hu.martin.chatter.application.port.MessageRepository;
import hu.martin.chatter.application.time.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatterConfiguration {

    @Bean
    public ConversationService conversationService(ConversationRepository conversationRepository,
                                                   MessageRepository messageRepository, TimeProvider timeProvider) {
        return new DefaultConversationService(conversationRepository, messageRepository, timeProvider);
    }

    @Bean
    public MessageRepository messageRepository() {
        return new InMemoryMessageRepository();
    }

}
