package hu.martin.chatservice.application;

import hu.martin.chatservice.domain.*;

public interface ChatService {
    Conversation createConversation();

    Conversation findConversationById(ConversationId id);

    Message findMessageById(MessageId id);

    void joinParticipantTo(ConversationId conversationId, ParticipantId participantId);

    void sendMessageTo(MessageId messageId, ConversationId conversationId);

    void deleteMessageFrom(MessageId messageId, ConversationId conversationId);

    Message createMessageWith(MessageContent messageContent);
}
