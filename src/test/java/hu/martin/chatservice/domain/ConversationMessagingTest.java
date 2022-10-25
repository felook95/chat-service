package hu.martin.chatservice.domain;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unitTest")
public class ConversationMessagingTest {

    @Test
    void conversationStartsWithoutMessages() {
        Conversation conversation = new Conversation();

        assertThat(conversation.messages()).isEmpty();
    }

    @Test
    void messageStoredInConversation() {
        ParticipantId senderParticipantId = ParticipantId.of(1L);
        Conversation conversation = new Conversation();
        Message message = new Message(senderParticipantId, "Test message");

        conversation.messageSent(message);

        assertThat(conversation.messages()).contains(message);
    }

    @Test
    void messageDeletedFromConversation() {
        Conversation conversation = new Conversation();
        ParticipantId senderParticipantId = ParticipantId.of(1L);
        Message message = new Message(senderParticipantId, "Test message");
        conversation.messageSent(message);

        conversation.deleteMessage(message);

        assertThat(conversation.messages()).doesNotContain(message);
    }
}
