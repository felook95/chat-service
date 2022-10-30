package hu.martin.chatservice.adapter.in.web.conversation;

import hu.martin.chatservice.application.ConversationService;
import hu.martin.chatservice.domain.Conversation;
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
}
