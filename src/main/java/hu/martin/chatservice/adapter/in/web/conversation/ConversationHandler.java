package hu.martin.chatservice.adapter.in.web.conversation;

import reactor.core.publisher.Mono;

public interface ConversationHandler {

  Mono<ConversationDTO> startConversation();
}
