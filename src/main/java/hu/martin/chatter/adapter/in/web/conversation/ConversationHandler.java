package hu.martin.chatter.adapter.in.web.conversation;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ConversationHandler {

  Mono<ServerResponse> startConversation(ServerRequest serverRequest);

  Mono<ServerResponse> joinToConversation(ServerRequest serverRequest);

  Mono<ServerResponse> removeFromConversation(ServerRequest serverRequest);

  Mono<ServerResponse> messageSent(ServerRequest serverRequest);

  Mono<ServerResponse> findConversationById(ServerRequest serverRequest);

  Mono<ServerResponse> messagesFromConversation(ServerRequest serverRequest);
}
