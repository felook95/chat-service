package hu.martin.chatservice.adapter.in.web.conversation;

import hu.martin.chatservice.application.ConversationService;
import hu.martin.chatservice.domain.Conversation;
import hu.martin.chatservice.domain.ConversationId;
import hu.martin.chatservice.domain.Message;
import hu.martin.chatservice.domain.ParticipantId;
import reactor.core.publisher.Mono;

public class DefaultConversationHandler implements ConversationHandler {

  private final ConversationService conversationService;

  public DefaultConversationHandler(ConversationService conversationService) {
    this.conversationService = conversationService;
  }

  @Override
  public Mono<ConversationDTO> startConversation() {
    Conversation conversation = conversationService.startConversation();
    return Mono.just(ConversationDTO.from(conversation));
  }

  @Override
  public Mono<ConversationDTO> joinToConversation(Long conversationId, Long participantId) {
    ConversationId domainConversationId = ConversationId.of(conversationId);
    ParticipantId domainParticipantId = ParticipantId.of(participantId);
    conversationService.joinParticipantTo(domainConversationId, domainParticipantId);
    Conversation conversation = conversationService.findConversationById(domainConversationId);
    return Mono.just(ConversationDTO.from(conversation));
  }

  @Override
  public void leaveConversation(Long conversationId, Long participantId) {
    throw new UnsupportedOperationException("Will be implemented later");
  }

  @Override
  public Mono<ConversationDTO> findConversationById(Long conversationId) {
    Conversation conversation = conversationService.findConversationById(
        ConversationId.of(conversationId));
    return Mono.just(ConversationDTO.from(conversation));
  }

  @Override
  public Mono<MessageDTO> messageSent(Long conversationId, MessageDTO messageDTO) {
    Message receivedMessage = receiveMessage(messageDTO);
    sendMessageTo(conversationId, receivedMessage);
    return Mono.just(MessageDTO.from(receivedMessage));
  }

  private Message receiveMessage(MessageDTO messageDTO) {
    return conversationService.receiveMessage(messageDTO.asMessage());
  }

  private void sendMessageTo(Long conversationId, Message receivedMessage) {
    conversationService.sendMessageTo(receivedMessage.id(), ConversationId.of(conversationId));
  }
}
