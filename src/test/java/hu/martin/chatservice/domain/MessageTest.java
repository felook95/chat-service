package hu.martin.chatservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.ZonedDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class MessageTest {

  @Test
  void newMessageHasDefaultStatusOfNormal() {
    Message message = MessageFactory.defaultWithContentOf("");

    assertThat(message.statusFlag()).isEqualTo(MessageStatus.CREATED);
  }

  @Test
  void changeStatusFlagToChangesTheStatusFlag() {
    Message message = MessageFactory.defaultWithContentOf("");

    message.changeStatusFlagTo(MessageStatus.DELETED);

    assertThat(message.statusFlag()).isEqualTo(MessageStatus.DELETED);
  }

  @Test
  void messageStoresContent() {
    ParticipantId participantId = ParticipantId.of(1L);
    MessageContent messageContent = new MessageContent("Test message");
    Message message = new Message(participantId, messageContent,
        CreatedDateTime.of(ZonedDateTime.now()));

    assertThat(message.content()).isEqualTo(messageContent);
  }

  @Test
  void editMessageContent() {
    Message message = MessageFactory.defaultWithContentOf("Original content");
    MessageContent modifiedContent = new MessageContent("Modified content");

    message.changeContentTo(modifiedContent);

    assertThat(message.content()).isEqualTo(modifiedContent);
  }

  @Test
  void editingMessageContentChangesMessageStatusToEdited() {
    Message message = MessageFactory.defaultWithContentOf("Original content");

    message.changeContentTo(MessageContent.of("Modified content"));

    assertThat(message.statusFlag()).isEqualTo(MessageStatus.EDITED);
  }

  @Test
  void editingADeletedMessageThrowsException() {
    Message message = MessageFactory.defaultWithContentOf("Original content");
    message.deleted();

    MessageContent modifiedContent = MessageContent.of("Modified content");
    assertThatThrownBy(() -> message.changeContentTo(modifiedContent)).isInstanceOf(
        IllegalStateException.class);
  }


  @Test
  void deleteMessageSetsStatusFlagToDeleted() {
    Message message = MessageFactory.defaultWithContentOf("");

    message.deleted();

    assertThat(message.statusFlag()).isEqualTo(MessageStatus.DELETED);
  }

}
