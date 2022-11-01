package hu.martin.chatter.adapter.in.web.conversation;

import hu.martin.chatter.application.ConversationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ConversationRouterTest {

  @Autowired
  private WebTestClient client;

  @Autowired
  ConversationService conversationService;

  @Test
  void startConversationReturnsConversationDTO() {
    client.post().uri("/conversation").exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.participantIds").isEmpty()
        .jsonPath("$.messageIds").isEmpty();
  }

  @Test
  void findConversationByIdReturnsFoundDTO() {
    Long conversationId = conversationService.startConversation().getId().id();
    client.get().uri("/conversation/" + conversationId).exchange()
        .expectStatus().isOk()
        .expectBody()
        .json("""
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
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.participantIds[0]").isEqualTo(123);
  }

//  @Test
//  void sendingMessageToConversationAddsMessageToConversation() {
//
//  }
//
//  @Test
//  void leavingConversationResultsInAConversationWithoutTheParticipant() {
//
//  }

}