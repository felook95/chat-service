package hu.martin.chatter.adapter.in.web.conversation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.domain.Message;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

@Tag("unitTest")
class DefaultConversationHandlerTest {

  @Test
  void sendingMessageToConversationReturnsMessageDTO() {
    MessageDTO newMessageDTO = new MessageDTO(2L, 3L, "Test message", ZonedDateTime.now());
    Message message = newMessageDTO.asMessage();
    ConversationService conversationService = mock(ConversationService.class);
    when(conversationService.receiveAndSendMessageTo(any(), any())).thenReturn(message);
    ConversationHandler conversationHandler = new DefaultConversationHandler(conversationService);

    Mono<MessageDTO> messageDTOMono = conversationHandler.messageSent(1L, newMessageDTO);

    MessageDTO messageDTO = messageDTOMono.block();
    assertThat(messageDTO.id()).isNotNull();
    assertThat(messageDTO.senderId()).isEqualTo(3L);
    assertThat(messageDTO.content()).isEqualTo("Test message");
    assertThat(messageDTO.createdDateTime()).isNotNull();
  }



}