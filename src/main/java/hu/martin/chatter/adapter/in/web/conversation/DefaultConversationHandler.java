package hu.martin.chatter.adapter.in.web.conversation;

import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.application.MessageService;
import hu.martin.chatter.application.paging.PageProperties;
import hu.martin.chatter.domain.conversation.ConversationId;
import hu.martin.chatter.domain.participant.ParticipantId;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.Optional;

@Component
public class DefaultConversationHandler implements ConversationHandler {

    public static final String CONVERSATION_ID_PARAM_NAME = "conversationId";
    public static final String PARTICIPANT_ID_PARAM_NAME = "participantId";
    private final ConversationService conversationService;
    private final MessageService messageService;

    public DefaultConversationHandler(ConversationService conversationService, MessageService messageService) {
        this.conversationService = conversationService;
        this.messageService = messageService;
    }

    @Override
    public Mono<ServerResponse> startConversation(ServerRequest serverRequest) {
        return conversationService.startConversation().map(ConversationDTO::from).flatMap(
                conversationDTO -> ServerResponse.ok()
                        .body(Mono.just(conversationDTO), ConversationDTO.class));
    }

    @Override
    public Mono<ServerResponse> joinToConversation(ServerRequest serverRequest) {
        BigInteger conversationId = new BigInteger(serverRequest.pathVariable(CONVERSATION_ID_PARAM_NAME));
        BigInteger participantId = new BigInteger(serverRequest.pathVariable(PARTICIPANT_ID_PARAM_NAME));
        ConversationId domainConversationId = ConversationId.of(conversationId);
        ParticipantId domainParticipantId = ParticipantId.of(participantId);
        return conversationService.joinParticipantTo(domainConversationId, domainParticipantId)
                .map(ConversationDTO::from).flatMap(conversationDTO -> ServerResponse.ok()
                        .body(Mono.just(conversationDTO), ConversationDTO.class));
    }

    @Override
    public Mono<ServerResponse> removeFromConversation(ServerRequest serverRequest) {
        BigInteger conversationId = new BigInteger(serverRequest.pathVariable(CONVERSATION_ID_PARAM_NAME));
        BigInteger participantId = new BigInteger(serverRequest.pathVariable(PARTICIPANT_ID_PARAM_NAME));
        return conversationService.removeFromConversation(ConversationId.of(conversationId),
                ParticipantId.of(participantId)).then(ServerResponse.ok().build());
    }

    @Override
    public Mono<ServerResponse> findConversationById(ServerRequest serverRequest) {
        BigInteger conversationId = new BigInteger(serverRequest.pathVariable(CONVERSATION_ID_PARAM_NAME));
        return conversationService.findConversationById(ConversationId.of(conversationId))
                .map(ConversationDTO::from).flatMap(conversationDTO -> ServerResponse.ok()
                        .body(Mono.just(conversationDTO), ConversationDTO.class));
    }

    @Override
    public Mono<ServerResponse> messagesFromConversationPaged(ServerRequest serverRequest) {
        BigInteger conversationId = new BigInteger(serverRequest.pathVariable(CONVERSATION_ID_PARAM_NAME));
        PageProperties pageProperties = getPageProperties(serverRequest);
        return conversationService.messageIdsFrom(ConversationId.of(conversationId)).collectList().flatMap(messageIds ->
                        messageService.findAllByIdOrderedByCreatedDateTime(messageIds, pageProperties).map(MessageDTO::from).collectList())
                .flatMap(messageDTOs -> ServerResponse.ok().body(Mono.just(messageDTOs), new ParameterizedTypeReference<>() {
                }));
    }

    private static PageProperties getPageProperties(ServerRequest serverRequest) {
        Integer pageIndex = getValidatedQueryParamAsInteger(serverRequest, "pageIndex");
        Integer pageSize = getValidatedQueryParamAsInteger(serverRequest, "pageSize");
        return new PageProperties(pageIndex, pageSize);
    }

    private static Integer getValidatedQueryParamAsInteger(ServerRequest serverRequest, String paramName) {
        Optional<Integer> pageIndexOptional = serverRequest.queryParam(paramName).map(Integer::valueOf);
        return pageIndexOptional.orElseThrow();
    }

    @Override
    public Mono<ServerResponse> messageSent(ServerRequest serverRequest) {
        BigInteger conversationId = new BigInteger(serverRequest.pathVariable(CONVERSATION_ID_PARAM_NAME));
        return serverRequest.bodyToMono(MessageDTO.class).map(MessageDTO::asMessage)
                .flatMap(message -> conversationService.receiveAndSendMessageTo(
                        ConversationId.of(conversationId), message))
                .map(MessageDTO::from)
                .flatMap(messageDTO -> ServerResponse.ok()
                        .body(Mono.just(messageDTO), MessageDTO.class));
    }
}
