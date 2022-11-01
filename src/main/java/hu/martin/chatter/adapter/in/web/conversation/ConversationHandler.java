package hu.martin.chatter.adapter.in.web.conversation;

import reactor.core.publisher.Mono;

public interface ConversationHandler {

  Mono<ConversationDTO> startConversation();

  Mono<ConversationDTO> joinToConversation(Long conversationId, Long participantId);

  void removeFromConversation(Long conversationId, Long participantId);

  Mono<MessageDTO> messageSent(Long conversationId, MessageDTO messageDTO);

  Mono<ConversationDTO> findConversationById(Long conversationId);
}
