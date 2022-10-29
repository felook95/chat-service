package hu.martin.chatservice.domain;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unitTest")
public class MessageTest {

    @Test
    void newMessageHasDefaultStatusOfNormal() {
        MessageContent messageContent = MessageContent.of("");
        CreatedDateTime createdDateTime = CreatedDateTime.of(ZonedDateTime.now());

        Message message = new Message(messageContent, createdDateTime);

        assertThat(message.statusFlag()).isEqualTo(MessageStatus.CREATED);
    }

    @Test
    void changeStatusFlagToChangesTheStatusFlag() {
        Message message = new Message(MessageContent.of(""), CreatedDateTime.of(ZonedDateTime.now()));

        message.changeStatusFlagTo(MessageStatus.DELETED);

        assertThat(message.statusFlag()).isEqualTo(MessageStatus.DELETED);
    }

    @Test
    void messageStoresContent() {
        MessageContent messageContent = new MessageContent("Test message");
        Message message = new Message(messageContent, CreatedDateTime.of(ZonedDateTime.now()));

        assertThat(message.content()).isEqualTo(messageContent);
    }

    @Test
    void editMessageContent() {
        Message message = new Message(MessageContent.of("Original content"), CreatedDateTime.of(ZonedDateTime.now()));
        MessageContent modifiedContent = new MessageContent("Modified content");

        message.changeContentTo(modifiedContent);

        assertThat(message.content()).isEqualTo(modifiedContent);
    }

    @Test
    void deleteMessageSetsStatusFlagToDeleted() {
        Message message = new Message(MessageContent.of(""), CreatedDateTime.of(ZonedDateTime.now()));

        message.deleted();

        assertThat(message.statusFlag()).isEqualTo(MessageStatus.DELETED);
    }
}
