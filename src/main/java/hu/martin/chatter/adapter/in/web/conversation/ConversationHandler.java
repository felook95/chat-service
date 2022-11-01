package hu.martin.chatter.adapter.in.web.conversation;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ConversationHandler {

  Mono<ServerResponse> startConversation(ServerRequest serverRequest);

  Mono<ServerResponse> joinToConversation(ServerRequest serverRequest);

  void removeFromConversation(Long conversationId, Long participantId);

  Mono<MessageDTO> messageSent(Long conversationId, MessageDTO messageDTO);

  Mono<ServerResponse> findConversationById(ServerRequest serverRequest);
}
