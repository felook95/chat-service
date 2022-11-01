package hu.martin.chatter.adapter.in.web.conversation;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ConversationRouter {

  @Bean
  RouterFunction<ServerResponse> conversationRoute(ConversationHandler conversationHandler) {
    RouterFunction<ServerResponse> conversationRoute = route()
        .GET("/{id}", conversationHandler::findConversationById)
        .POST("/{conversationId}/participants/{participantId}",
            conversationHandler::joinToConversation)
        .POST(conversationHandler::startConversation)
        .build();
    return nest(path("/conversation"), conversationRoute);
  }

}
