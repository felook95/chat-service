package hu.martin.chatservice.application;

import hu.martin.chatservice.domain.Conversation;
import hu.martin.chatservice.domain.ConversationId;
import hu.martin.chatservice.domain.Message;
import hu.martin.chatservice.domain.MessageId;
import hu.martin.chatservice.domain.ParticipantId;
import java.util.Collection;
import java.util.Set;

public interface ConversationService {

  Conversation startConversation();

  Conversation findConversationById(ConversationId id);

  Message findMessageById(MessageId id);

  void joinParticipantTo(ConversationId conversationId, ParticipantId participantId);

  Message receiveMessage(Message message);

  void sendMessageTo(MessageId messageId, ConversationId conversationId);

  void deleteMessage(MessageId messageId);

  Set<Message> messagesFrom(ConversationId conversationId);

  Collection<Message> messagesByChronologicalOrderFrom(ConversationId conversationId);

  void removeFromConversation(ConversationId conversationId, ParticipantId participantId);
}
