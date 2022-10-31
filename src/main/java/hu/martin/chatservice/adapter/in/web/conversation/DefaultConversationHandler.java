package hu.martin.chatservice.adapter.in.web.conversation;

import hu.martin.chatservice.application.ConversationService;
import hu.martin.chatservice.domain.Conversation;
import hu.martin.chatservice.domain.ConversationId;
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
}
