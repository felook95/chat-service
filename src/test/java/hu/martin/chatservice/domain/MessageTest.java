package hu.martin.chatservice.domain;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unitTest")
public class MessageTest {

    @Test
    void messageStoresContent() {
        MessageContent messageContent = new MessageContent("Test message");
        Message message = new Message(messageContent);

        assertThat(message.content()).isEqualTo(messageContent);
    }

    @Test
    void editMessageContent() {
        Message message = new Message(MessageContent.of("Original content"));
        MessageContent modifiedContent = new MessageContent("Modified content");

        message.changeContentTo(modifiedContent);

        assertThat(message.content()).isEqualTo(modifiedContent);
    }

    @Test
    void deleteMessageSetsStatusFlagToDeleted() {
        Message message = new Message(MessageContent.of(""));

        message.deleted();

        assertThat(message.statusFlag()).isEqualTo(MessageStatus.DELETED);
    }
}
