package hu.martin.chatter.adapter.in.web.conversation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationFactory;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.CreatedDateTime;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageFactory;
import hu.martin.chatter.domain.ParticipantId;
import java.time.ZonedDateTime;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ConversationRouterTest {

  @Autowired
  private WebTestClient client;

  @MockBean
  ConversationService conversationService;

  @Test
  void startConversationReturnsConversationDTO() {
    when(conversationService.startConversation()).thenReturn(ConversationFactory.withDefaults());

    client.post().uri("/conversation").exchange().expectStatus().isOk().expectBody()
        .jsonPath("$.id").isNotEmpty().jsonPath("$.participantIds").isEmpty()
        .jsonPath("$.messageIds").isEmpty();
  }

  @Test
  void findConversationByIdReturnsFoundDTO() {
    Conversation conversation = ConversationFactory.withDefaults();
    Long conversationId = conversation.getId().id();
    when(conversationService.findConversationById(any())).thenReturn(conversation);

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
    ConversationId conversationId = ConversationId.of(1L);
    ParticipantId participantId = ParticipantId.of(123L);
    when(conversationService.joinParticipantTo(conversationId, participantId)).thenReturn(
        ConversationFactory.withParticipants(participantId));

    client.post()
        .uri("/conversation/" + conversationId.id() + "/participants/" + participantId.id())
        .exchange()
        .expectStatus().isOk().expectBody().jsonPath("$.participantIds[0]")
        .isEqualTo(participantId.id());
  }

  @Test
  void sendingMessageToConversationReturnsMessageDTO() {
    Message message = MessageFactory.defaultWIthCreatedDateTimeOf(
        CreatedDateTime.of(ZonedDateTime.parse("2022-11-03T18:27:40.005661434Z")));
    when(conversationService.receiveAndSendMessageTo(any(), any())).thenReturn(message);
    MessageDTO messageDTO = MessageDTO.from(message);

    client.post().uri("/conversation/1/messages")
        .body(Mono.just(messageDTO), MessageDTO.class).exchange().expectBody().json("""
            {
               "id":1,
               "senderId":1,
               "content":"",
               "createdDateTime":"2022-11-03T18:27:40.005661434Z"
            }
            """);
  }

  @Test
  void storedMessagesCanBeRetrievedFromConversation() {
    CreatedDateTime createdDateTime = CreatedDateTime.of(
        ZonedDateTime.parse("2022-11-03T18:38:20.005661434Z"));
    Message message = MessageFactory.defaultWIthCreatedDateTimeOf(createdDateTime);
    when(conversationService.messagesFrom(any())).thenReturn(Set.of(message));

    client.get().uri("/conversation/1/messages").exchange().expectBody()
        .json("""
            [{"senderId":1,"content":"","createdDateTime":"2022-11-03T18:38:20.005661434Z"}]
            """);
  }

  @Test
  void verifyCallToRemoveFromConversationIsCalledOnParticipantDelete() {
    client.delete()
        .uri("/conversation/1/participants/1")
        .exchange().expectStatus().isOk();

    verify(conversationService).removeFromConversation(any(), any());
  }

}