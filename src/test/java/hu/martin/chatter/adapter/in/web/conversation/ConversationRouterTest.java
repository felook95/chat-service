package hu.martin.chatter.adapter.in.web.conversation;

import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.domain.conversation.ConversationFactory;
import hu.martin.chatter.domain.message.MessageFactory;
import hu.martin.chatter.domain.conversation.Conversation;
import hu.martin.chatter.domain.conversation.ConversationId;
import hu.martin.chatter.domain.message.CreatedDateTime;
import hu.martin.chatter.domain.message.Message;
import hu.martin.chatter.domain.participant.ParticipantId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ConversationRouterTest {

    @MockBean
    private ConversationService conversationService;

    @Autowired
    private WebTestClient client;

    @Test
    void startConversationReturnsConversationDTO() {
        when(conversationService.startConversation()).thenReturn(
                Mono.just(ConversationFactory.withDefaults()));

        client.post().uri("/conversation").exchange().expectStatus().isOk().expectBody()
                .jsonPath("$.id").isNotEmpty().jsonPath("$.participantIds").isEmpty()
                .jsonPath("$.messageIds").isEmpty();
    }

    @Test
    void findConversationByIdReturnsFoundDTO() {
        Conversation conversation = ConversationFactory.withDefaults();
        BigInteger conversationId = conversation.getId().id();
        when(conversationService.findConversationById(any())).thenReturn(Mono.just(conversation));

        client.get().uri("/conversation/" + conversationId).exchange().expectStatus().isOk()
                .expectBody().json("""
                        {
                           "id":%s,
                           "participantIds":[],
                           "messageIds":[]
                        }
                        """.formatted(conversationId));
    }

    @Test
    void joinConversationReturnsDTOWithTheJoinedParticipants() {
        ConversationId conversationId = ConversationId.of(BigInteger.valueOf(1L));
        ParticipantId participantId = ParticipantId.of(BigInteger.valueOf(123L));
        when(conversationService.joinParticipantTo(conversationId, participantId)).thenReturn(
                Mono.just(ConversationFactory.withParticipants(participantId)));

        client.post()
                .uri("/conversation/" + conversationId.id() + "/participants/" + participantId.id())
                .exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.participantIds[0]")
                .isEqualTo(participantId.id());
    }

    @Test
    void sendingMessageToConversationReturnsMessageDTO() {
        Message message = MessageFactory.defaultWIthCreatedDateTimeOf(
                CreatedDateTime.of(LocalDateTime.parse("2022-11-03T18:27:40.005661434")));
        when(conversationService.receiveAndSendMessageTo(any(), any())).thenReturn(Mono.just(message));
        MessageDTO messageDTO = MessageDTO.from(message);

        client.post().uri("/conversation/1/messages")
                .body(Mono.just(messageDTO), MessageDTO.class).exchange().expectBody().json("""
                        {
                           "id":1,
                           "senderId":1,
                           "content":"",
                           "createdDateTime":"2022-11-03T18:27:40.005661434"
                        }
                        """);
    }

    @Test
    void storedMessagesCanBeRetrievedFromConversation() {
        CreatedDateTime createdDateTime = CreatedDateTime.of(
                LocalDateTime.parse("2022-11-03T18:38:20.005661434"));
        Message message = MessageFactory.defaultWIthCreatedDateTimeOf(createdDateTime);
        when(conversationService.messagesFrom(any())).thenReturn(Flux.just(message));

        client.get().uri("/conversation/1/messages").exchange().expectBody()
                .json("""
                        [{"senderId":1,"content":"","createdDateTime":"2022-11-03T18:38:20.005661434"}]
                        """);
    }

    @Test
    void verifyCallToRemoveFromConversationIsCalledOnParticipantDelete() {
        when(conversationService.removeFromConversation(any(), any())).thenReturn(Mono.empty());

        client.delete()
                .uri("/conversation/1/participants/1")
                .exchange().expectStatus().isOk();

        verify(conversationService).removeFromConversation(any(), any());
    }

}