package hu.martin.chatservice.application;

import hu.martin.chatservice.application.port.ConversationRepository;
import hu.martin.chatservice.application.port.InMemoryConversationRepository;
import hu.martin.chatservice.application.port.InMemoryMessageRepository;
import hu.martin.chatservice.application.port.MessageRepository;

public class ChatServiceFactory {

    public static ChatService withDefaults() {
        ConversationRepository conversationRepository = new InMemoryConversationRepository();
        MessageRepository messageRepository = new InMemoryMessageRepository();
        return new DefaultChatService(conversationRepository, messageRepository);
    }
}
