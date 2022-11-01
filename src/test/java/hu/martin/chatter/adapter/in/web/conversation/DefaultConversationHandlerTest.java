package hu.martin.chatter.adapter.in.web.conversation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.ParticipantId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

@Tag("unitTest")
class DefaultConversationHandlerTest {

  @Test
  void startConversationReturnsConversationDTO() {
    ConversationDTO expectedDTO = conversationDTOAllInitialized();
    ConversationService conversationService = mock(ConversationService.class);
    when(conversationService.startConversation()).thenReturn(expectedDTO.asConversation());
    ConversationHandler conversationHandler = new DefaultConversationHandler(conversationService);

    ConversationDTO actualDTO = conversationHandler.startConversation().block();

    assertThat(expectedDTO).isEqualTo(actualDTO);
  }

  private static ConversationDTO conversationDTOAllInitialized() {
    Conversation conversation = new Conversation();
    conversation.setId(ConversationId.of(1_1L));
    conversation.joinedBy(ParticipantId.of(2_1L));
    conversation.messageSent(MessageId.of(3_1L));
    return ConversationDTO.from(conversation);
  }

  @Test
  void findConversationByIdReturnsFoundDTO() {
    ConversationService conversationService = mock(ConversationService.class);
    ConversationDTO conversationDTOToReturn = conversationDTOAllInitialized();
    Conversation conversationToReturn = conversationDTOToReturn.asConversation();
    when(conversationService.findConversationById(ConversationId.of(1L))).thenReturn(
        conversationToReturn);
    ConversationHandler conversationHandler = new DefaultConversationHandler(conversationService);

    ConversationDTO conversationDTO = conversationHandler.findConversationById(1L).block();

    assertThat(conversationDTO).isEqualTo(conversationDTOToReturn);
  }

  @Test
  void joinConversationReturnsADTOWithTheJoinedParticipants() {
    ConversationId conversationId = ConversationId.of(1_1L);
    ParticipantId participantId = ParticipantId.of(2_1L);
    ConversationService conversationService = mock(ConversationService.class);
    Conversation conversation = new Conversation();
    conversation.setId(conversationId);
    conversation.joinedBy(participantId);
    when(conversationService.findConversationById(conversationId)).thenReturn(conversation);
    ConversationHandler conversationHandler = new DefaultConversationHandler(conversationService);

    Mono<ConversationDTO> conversationMono = conversationHandler.joinToConversation(
        conversationId.id(), participantId.id());

    ConversationDTO conversationDTO = conversationMono.block();
    assertThat(conversationDTO.id()).isEqualTo(conversationId.id());
    assertThat(conversationDTO.participantIds()).containsOnly(participantId.id());
  }

  @Test
  void sendingMessageToConversationReturnsMessageDTO() {
    MessageDTO newMessageDTO = new MessageDTO(2L, 3L, "Test message", ZonedDateTime.now());
    Message message = newMessageDTO.asMessage();
    ConversationService conversationService = mock(ConversationService.class);
    when(conversationService.receiveMessage(any())).thenReturn(message);
    ConversationHandler conversationHandler = new DefaultConversationHandler(conversationService);

    Mono<MessageDTO> messageDTOMono = conversationHandler.messageSent(1L, newMessageDTO);

    MessageDTO messageDTO = messageDTOMono.block();
    assertThat(messageDTO.id()).isNotNull();
    assertThat(messageDTO.senderId()).isEqualTo(3L);
    assertThat(messageDTO.content()).isEqualTo("Test message");
    assertThat(messageDTO.createdDateTime()).isNotNull();
  }

  @Test
  void sendingMessageToConversationAddsMessageToConversation() {
    ConversationService conversationService = new StubConversationServiceForSendingMessage(
        ConversationId.of(1L));
    ConversationHandler conversationHandler = new DefaultConversationHandler(conversationService);
    MessageDTO newMessageDTO = new MessageDTO(null, 3L, "Test message", ZonedDateTime.now());

    MessageDTO messageDTO = conversationHandler.messageSent(1L, newMessageDTO).block();

    ConversationDTO conversationDTO = conversationHandler.findConversationById(1L).block();
    assertThat(conversationDTO.messageIds()).containsOnly(messageDTO.id());
  }

  @Test
  void leavingConversationResultsInAConversationWithoutTheParticipant() {
    ConversationService conversationService = new StubConversationServiceForLeaving(
        ParticipantId.of(1L));
    ConversationHandler conversationHandler = new DefaultConversationHandler(conversationService);

    conversationHandler.leaveConversation(1L, 1L);

    ConversationDTO conversationDTO = conversationHandler.findConversationById(1L).block();
    assertThat(conversationDTO.participantIds()).doesNotContain(1L).isEmpty();
  }

}