package hu.martin.chatservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
public class ConversationMessagingTest {

  @Test
  void conversationStartsWithoutMessages() {
    Conversation conversation = new Conversation();

    assertThat(conversation.messages()).isEmpty();
  }

  @Test
  void messageStoredInConversation() {
    Conversation conversation = new Conversation();
    MessageId messageId = MessageId.of(1L);

    conversation.messageSent(messageId);

    assertThat(conversation.messages()).contains(messageId);
  }

  @Test
  void nullMessageThrowsException() {
    Conversation conversation = new Conversation();

    assertThatThrownBy(() -> conversation.messageSent(null))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
