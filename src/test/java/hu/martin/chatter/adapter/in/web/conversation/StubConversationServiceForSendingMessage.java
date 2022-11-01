package hu.martin.chatter.adapter.in.web.conversation;

import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.ParticipantId;
import java.util.Collection;
import java.util.Set;

public class StubConversationServiceForSendingMessage implements ConversationService {

  private final Conversation conversation;

  public StubConversationServiceForSendingMessage(ConversationId initialConversationsId) {
    conversation = new Conversation();
    conversation.setId(initialConversationsId);
  }

  @Override
  public Conversation startConversation() {
    return null;
  }

  @Override
  public Conversation findConversationById(ConversationId id) {
    return conversation;
  }

  @Override
  public Message findMessageById(MessageId id) {
    return null;
  }

  @Override
  public void joinParticipantTo(ConversationId conversationId, ParticipantId participantId) {

  }

  @Override
  public Message receiveMessage(Message message) {
    message.setId(MessageId.of(1L));
    return message;
  }

  @Override
  public void sendMessageTo(MessageId messageId, ConversationId conversationId) {
    conversation.messageSent(messageId);
  }

  @Override
  public void deleteMessage(MessageId messageId) {

  }

  @Override
  public Set<Message> messagesFrom(ConversationId conversationId) {
    return null;
  }

  @Override
  public Collection<Message> messagesByChronologicalOrderFrom(ConversationId conversationId) {
    return null;
  }

  @Override
  public void removeFromConversation(ConversationId conversationId, ParticipantId participantId) {

  }
}
