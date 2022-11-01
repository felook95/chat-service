package hu.martin.chatter.application;

import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.ParticipantId;
import java.util.Collection;
import java.util.Set;

public interface ConversationService {

  Conversation startConversation();

  Conversation findConversationById(ConversationId id);

  Message findMessageById(MessageId id);

  Conversation joinParticipantTo(ConversationId conversationId, ParticipantId participantId);

  Message receiveMessage(Message message);

  void sendMessageTo(MessageId messageId, ConversationId conversationId);

  Message receiveAndSendMessageTo(ConversationId conversationId, Message message);

  void deleteMessage(MessageId messageId);

  Set<Message> messagesFrom(ConversationId conversationId);

  Collection<Message> messagesByChronologicalOrderFrom(ConversationId conversationId);

  void removeFromConversation(ConversationId conversationId, ParticipantId participantId);
}
