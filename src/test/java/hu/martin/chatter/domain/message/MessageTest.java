package hu.martin.chatter.domain.message;

import hu.martin.chatter.domain.participant.ParticipantId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unitTest")
class MessageTest {

    @Test
    void newMessageHasDefaultStatusOfNormal() {
        Message message = MessageFactory.withDefaults();

        assertThat(message.statusFlag()).isEqualTo(MessageStatus.CREATED);
    }

    @Test
    void changeStatusFlagToChangesTheStatusFlag() {
        Message message = MessageFactory.withDefaults();

        message.changeStatusFlagTo(MessageStatus.DELETED);

        assertThat(message.statusFlag()).isEqualTo(MessageStatus.DELETED);
    }

    @Test
    void messageStoresContent() {
        ParticipantId participantId = ParticipantId.of(BigInteger.valueOf(1L));
        MessageContent messageContent = new MessageContent("Test message");
        Message message = new Message(participantId, messageContent,
                CreatedDateTime.of(LocalDateTime.now()));

        assertThat(message.content()).isEqualTo(messageContent);
    }

    @Test
    void editMessageContent() {
        Message message = MessageFactory.withDefaults();
        MessageContent newContent = MessageContent.of("Modified content");

        message.changeContentTo(newContent);

        assertThat(message.content()).isEqualTo(newContent);
    }

    @Test
    void editingMessageContentChangesMessageStatusToEdited() {
        Message message = MessageFactory.withDefaults();

        message.changeContentTo(MessageContent.of("Modified content"));

        assertThat(message.statusFlag()).isEqualTo(MessageStatus.EDITED);
    }

    @Test
    void editingADeletedMessageThrowsException() {
        Message message = MessageFactory.withDefaults();
        message.delete();

        MessageContent modifiedContent = MessageContent.of("Modified content");
        assertThatThrownBy(() -> message.changeContentTo(modifiedContent)).isInstanceOf(
                IllegalStateException.class);
    }


    @Test
    void deleteMessageSetsStatusFlagToDeleted() {
        Message message = MessageFactory.withDefaults();

        message.delete();

        assertThat(message.statusFlag()).isEqualTo(MessageStatus.DELETED);
    }

    @Test
    void changeCreatedDateTimeToSetsTheCorrectDateTime() {
        Message message = MessageFactory.defaultWIthCreatedDateTimeOf(null);

        LocalDateTime newCreatedDateTime = LocalDateTime.of(2023, 3, 23, 23, 30);
        message.changeCreatedDateTimeTo(CreatedDateTime.of(newCreatedDateTime));

        assertThat(message.createdDateTime().createdDateTime()).isEqualTo(newCreatedDateTime);
    }

}
