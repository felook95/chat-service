package hu.martin.chatter.application;

import hu.martin.chatter.adapter.out.inmemory.InMemoryConversationRepository;
import hu.martin.chatter.adapter.out.inmemory.InMemoryMessageRepository;
import hu.martin.chatter.application.port.ConversationRepository;
import hu.martin.chatter.application.port.MessageRepository;
import hu.martin.chatter.application.time.TimeProvider;
import hu.martin.chatter.application.time.UTCTimeProvider;

public class ConversationServiceFactory {

    public static ConversationService withDefaults() {
        MessageRepository messageRepository = new InMemoryMessageRepository();
        return withMessageRepository(messageRepository);
    }

    public static ConversationService withMessageRepository(MessageRepository messageRepository) {
        ConversationRepository conversationRepository = new InMemoryConversationRepository();
        UTCTimeProvider utcTimeProvider = new UTCTimeProvider();
        return new DefaultConversationService(conversationRepository, messageRepository, utcTimeProvider);
    }

    public static ConversationService with(ConversationRepository conversationRepository,
                                           MessageRepository messageRepository) {
        UTCTimeProvider utcTimeProvider = new UTCTimeProvider();
        return new DefaultConversationService(conversationRepository, messageRepository, utcTimeProvider);
    }

    public static ConversationService withConversationRepository(
            ConversationRepository conversationRepository) {
        MessageRepository messageRepository = new InMemoryMessageRepository();
        UTCTimeProvider utcTimeProvider = new UTCTimeProvider();
        return new DefaultConversationService(conversationRepository, messageRepository, utcTimeProvider);
    }

    public static ConversationService withTimeProvider(TimeProvider timeProvider) {
        MessageRepository messageRepository = new InMemoryMessageRepository();
        ConversationRepository conversationRepository = new InMemoryConversationRepository();
        return new DefaultConversationService(conversationRepository,messageRepository,timeProvider);
    }
}
