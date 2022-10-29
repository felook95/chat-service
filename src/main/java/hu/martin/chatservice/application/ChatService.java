package hu.martin.chatservice.application;

import hu.martin.chatservice.domain.*;

import java.util.Collection;
import java.util.Set;

public interface ChatService {
    Conversation startConversation();

    Conversation findConversationById(ConversationId id);

    Message findMessageById(MessageId id);

    void joinParticipantTo(ConversationId conversationId, ParticipantId participantId);

    void sendMessageTo(MessageId messageId, ConversationId conversationId);

    Message receiveMessage(MessageContent messageContent, CreatedDateTime createdDateTime);

    void deleteMessage(MessageId messageId);

    Set<Message> messagesFrom(ConversationId conversationId);

    Collection<Message> messagesByChronologicalOrderFrom(ConversationId conversationId);
}
