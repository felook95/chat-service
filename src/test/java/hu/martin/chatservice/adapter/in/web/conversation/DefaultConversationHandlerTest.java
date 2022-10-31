package hu.martin.chatservice.adapter.in.web.conversation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hu.martin.chatservice.application.ConversationService;
import hu.martin.chatservice.domain.Conversation;
import hu.martin.chatservice.domain.ConversationId;
import hu.martin.chatservice.domain.MessageId;
import hu.martin.chatservice.domain.ParticipantId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
}