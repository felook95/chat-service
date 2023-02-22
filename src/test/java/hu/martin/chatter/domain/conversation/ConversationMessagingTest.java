package hu.martin.chatter.domain.conversation;

import hu.martin.chatter.domain.conversation.Conversation;
import hu.martin.chatter.domain.message.MessageId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unitTest")
class ConversationMessagingTest {

    @Test
    void conversationStartsWithoutMessages() {
        Conversation conversation = new Conversation();

        assertThat(conversation.messages()).isEmpty();
    }

    @Test
    void messageStoredInConversation() {
        Conversation conversation = new Conversation();
        MessageId messageId = MessageId.of(BigInteger.valueOf(1L));

        conversation.messageSent(messageId);

        assertThat(conversation.messages()).containsOnly(messageId);
    }

    @Test
    void nullMessageThrowsException() {
        Conversation conversation = new Conversation();

        assertThatThrownBy(() -> conversation.messageSent(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
