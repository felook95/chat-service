package hu.martin.chatter.adapter.in.web.conversation;

import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.ParticipantId;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
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
    conversationService.removeFromConversation(ConversationId.of(conversationId),
        ParticipantId.of(participantId));
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
