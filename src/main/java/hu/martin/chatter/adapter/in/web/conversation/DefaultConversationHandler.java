package hu.martin.chatter.adapter.in.web.conversation;

import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.ParticipantId;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class DefaultConversationHandler implements ConversationHandler {

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
    Long conversationId = Long.valueOf(serverRequest.pathVariable("conversationId"));
    Long participantId = Long.valueOf(serverRequest.pathVariable("participantId"));
    ConversationId domainConversationId = ConversationId.of(conversationId);
    ParticipantId domainParticipantId = ParticipantId.of(participantId);
    Conversation conversation = conversationService.joinParticipantTo(domainConversationId,
        domainParticipantId);
    Mono<ConversationDTO> just = Mono.just(ConversationDTO.from(conversation));
    return ServerResponse.ok().body(just, ConversationDTO.class);
  }

  @Override
  public void removeFromConversation(Long conversationId, Long participantId) {
    conversationService.removeFromConversation(ConversationId.of(conversationId),
        ParticipantId.of(participantId));
  }

  @Override
  public Mono<ServerResponse> findConversationById(ServerRequest serverRequest) {
    Long conversationId = Long.valueOf(serverRequest.pathVariable("id"));
    Conversation conversation = conversationService.findConversationById(
        ConversationId.of(conversationId));
    return ServerResponse.ok()
        .body(Mono.just(ConversationDTO.from(conversation)), ConversationDTO.class);
  }

  @Override
  public Mono<MessageDTO> messageSent(Long conversationId, MessageDTO messageDTO) {
    Message receivedMessage = conversationService.receiveAndSendMessageTo(
        ConversationId.of(conversationId),
        messageDTO.asMessage());
    return Mono.just(MessageDTO.from(receivedMessage));
  }
}
