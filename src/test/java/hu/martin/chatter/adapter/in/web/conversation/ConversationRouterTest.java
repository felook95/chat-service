package hu.martin.chatter.adapter.in.web.conversation;

import static org.assertj.core.api.Assertions.assertThat;

import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.ParticipantId;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ConversationRouterTest {

  @Autowired
  private WebTestClient client;

  @Autowired
  ConversationService conversationService;

  @Test
  void startConversationReturnsConversationDTO() {
    client.post().uri("/conversation").exchange().expectStatus().isOk().expectBody()
        .jsonPath("$.id").isNotEmpty().jsonPath("$.participantIds").isEmpty()
        .jsonPath("$.messageIds").isEmpty();
  }

  @Test
  void findConversationByIdReturnsFoundDTO() {
    Long conversationId = conversationService.startConversation().getId().id();

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
    Long conversationId = conversationService.startConversation().getId().id();

    client.post().uri("/conversation/" + conversationId + "/participants/123").exchange()
        .expectStatus().isOk().expectBody().jsonPath("$.participantIds[0]").isEqualTo(123);
  }

  @Test
  void sendingMessageToConversationReturnsMessageDTO() {
    Conversation conversation = conversationService.startConversation();
    Long conversationId = conversation.getId().id();
    conversation.joinedBy(ParticipantId.of(1L));
    ZonedDateTime createdDateTime = ZonedDateTime.of(2022, 11, 3, 19, 27, 40, 5661434,
        ZoneId.of("Europe/Budapest"));
    MessageDTO messageDTO = new MessageDTO(null, 1L, "Test message", createdDateTime);

    client.post().uri("/conversation/" + conversationId + "/messages")
        .body(Mono.just(messageDTO), MessageDTO.class).exchange().expectBody().json("""
            {
               "id":1,
               "senderId":1,
               "content":"Test message",
               "createdDateTime":"2022-11-03T18:27:40.005661434Z"
            }
            """);
  }

  @Test
  void sendingMessageToConversationAddsMessageToConversation() {
    Conversation conversation = conversationService.startConversation();
    Long conversationId = conversation.getId().id();
    conversation.joinedBy(ParticipantId.of(1L));
    ZonedDateTime createdDateTime = ZonedDateTime.of(2022, 11, 3, 19, 38, 20, 5661434,
        ZoneId.of("Europe/Budapest"));
    MessageDTO messageDTO = new MessageDTO(null, 1L, "Test message2", createdDateTime);
    client.post().uri("/conversation/" + conversationId + "/messages")
        .body(Mono.just(messageDTO), MessageDTO.class).exchange();

    client.get().uri("/conversation/" + conversationId + "/messages").exchange()
        .expectBody()
        .json("""
            [{"senderId":1,"content":"Test message2","createdDateTime":"2022-11-03T18:38:20.005661434Z"}]
            """);
  }

  @Test
  void leavingConversationResultsInAConversationWithoutTheParticipant() {
    Conversation conversation = conversationService.startConversation();
    ParticipantId participantId = ParticipantId.of(1L);
    ConversationId conversationId = conversation.getId();
    conversationService.joinParticipantTo(conversationId, participantId);

    client.delete().uri("/conversation/" + conversationId.id() + "/participants/" + participantId.id())
        .exchange().expectStatus().isOk();

    Conversation conversationById = conversationService.findConversationById(conversationId);
    assertThat(conversationById.hasParticipant(participantId)).isFalse();
  }

}