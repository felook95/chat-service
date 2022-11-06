package hu.martin.chatter.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
    ParticipantId participantId = ParticipantId.of(1L);
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

}
