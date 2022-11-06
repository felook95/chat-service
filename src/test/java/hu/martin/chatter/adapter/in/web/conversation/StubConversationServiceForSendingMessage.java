package hu.martin.chatter.adapter.in.web.conversation;

import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.ParticipantId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class StubConversationServiceForSendingMessage implements ConversationService {

  private final Conversation conversation;

  public StubConversationServiceForSendingMessage(ConversationId initialConversationsId) {
    conversation = new Conversation();
    conversation.setId(initialConversationsId);
  }

  @Override
  public Mono<Conversation> startConversation() {
    return null;
  }

  @Override
  public Mono<Conversation> findConversationById(ConversationId id) {
    return Mono.just(conversation);
  }

  @Override
  public Mono<Message> findMessageById(MessageId id) {
    return null;
  }

  @Override
  public Mono<Conversation> joinParticipantTo(ConversationId conversationId,
      ParticipantId participantId) {

    return null;
  }

  @Override
  public Mono<Message> receiveMessage(Message message) {
    message.setId(MessageId.of(1L));
    return Mono.just(message);
  }

  @Override
  public Mono<Void> sendMessageTo(MessageId messageId, ConversationId conversationId) {
    conversation.messageSent(messageId);
    return null;
  }

  @Override
  public Mono<Message> receiveAndSendMessageTo(ConversationId conversationId, Message message) {
    return receiveMessage(message).flatMap(
        receivedMessage -> sendMessageTo(receivedMessage.id(), conversationId).thenReturn(
            receivedMessage));
  }

  @Override
  public Mono<Void> deleteMessage(MessageId messageId) {
    return null;
  }

  @Override
  public Flux<Message> messagesFrom(ConversationId conversationId) {
    return null;
  }

  @Override
  public Flux<Message> messagesByChronologicalOrderFrom(ConversationId conversationId) {
    return null;
  }

  @Override
  public Mono<Void> removeFromConversation(ConversationId conversationId,
      ParticipantId participantId) {
    return null;
  }
}
