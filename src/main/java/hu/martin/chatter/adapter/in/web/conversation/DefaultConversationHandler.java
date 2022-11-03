package hu.martin.chatter.adapter.in.web.conversation;

import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.ParticipantId;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class DefaultConversationHandler implements ConversationHandler {

  public static final String CONVERSATION_ID_PARAM_NAME = "conversationId";
  public static final String PARTICIPANT_ID_PARAM_NAME = "participantId";
  private final ConversationService conversationService;

  public DefaultConversationHandler(ConversationService conversationService) {
    this.conversationService = conversationService;
  }

  @Override
  public Mono<ServerResponse> startConversation(ServerRequest serverRequest) {
    Conversation conversation = conversationService.startConversation();
    Mono<ConversationDTO> conversationDTOMono = Mono.just(ConversationDTO.from(conversation));
    return ServerResponse.ok().body(conversationDTOMono, ConversationDTO.class);
  }

  @Override
  public Mono<ServerResponse> joinToConversation(ServerRequest serverRequest) {
    Long conversationId = Long.valueOf(serverRequest.pathVariable(CONVERSATION_ID_PARAM_NAME));
    Long participantId = Long.valueOf(serverRequest.pathVariable(PARTICIPANT_ID_PARAM_NAME));
    ConversationId domainConversationId = ConversationId.of(conversationId);
    ParticipantId domainParticipantId = ParticipantId.of(participantId);
    Conversation conversation = conversationService.joinParticipantTo(domainConversationId,
        domainParticipantId);
    Mono<ConversationDTO> just = Mono.just(ConversationDTO.from(conversation));
    return ServerResponse.ok().body(just, ConversationDTO.class);
  }

  @Override
  public Mono<ServerResponse> removeFromConversation(ServerRequest serverRequest) {
    Long conversationId = Long.valueOf(serverRequest.pathVariable(CONVERSATION_ID_PARAM_NAME));
    Long participantId = Long.valueOf(serverRequest.pathVariable(PARTICIPANT_ID_PARAM_NAME));
    conversationService.removeFromConversation(ConversationId.of(conversationId),
        ParticipantId.of(participantId));
    return ServerResponse.ok().build();
  }

  @Override
  public Mono<ServerResponse> findConversationById(ServerRequest serverRequest) {
    Long conversationId = Long.valueOf(serverRequest.pathVariable(CONVERSATION_ID_PARAM_NAME));
    Conversation conversation = conversationService.findConversationById(
        ConversationId.of(conversationId));
    return ServerResponse.ok()
        .body(Mono.just(ConversationDTO.from(conversation)), ConversationDTO.class);
  }

  @Override
  public Mono<ServerResponse> messagesFromConversation(ServerRequest serverRequest) {
    Long conversationId = Long.valueOf(serverRequest.pathVariable(CONVERSATION_ID_PARAM_NAME));
    Set<Message> messages = conversationService.messagesFrom(ConversationId.of(conversationId));
    Set<MessageDTO> messageDTOs = messages.stream().map(MessageDTO::from)
        .collect(Collectors.toSet());
    return ServerResponse.ok()
        .body(Mono.just(messageDTOs), new ParameterizedTypeReference<>() {
        });
  }

  @Override
  public Mono<ServerResponse> messageSent(ServerRequest serverRequest) {
    Long conversationId = Long.valueOf(serverRequest.pathVariable(CONVERSATION_ID_PARAM_NAME));
    return serverRequest.bodyToMono(MessageDTO.class).map(messageDTO -> {
      Message receivedMessage = conversationService.receiveAndSendMessageTo(
          ConversationId.of(conversationId), messageDTO.asMessage());
      return MessageDTO.from(receivedMessage);
    }).flatMap(messageDTO -> ServerResponse.ok().body(Mono.just(messageDTO), MessageDTO.class));
  }
}
