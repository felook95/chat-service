package hu.martin.chatter.adapter.out.r2dbc;

import static org.assertj.core.api.Assertions.assertThat;

import hu.martin.chatter.domain.CreatedDateTime;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageContent;
import hu.martin.chatter.domain.MessageFactory;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.MessageStatus;
import hu.martin.chatter.domain.ParticipantId;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class MessageDBOMappingTest {

  @Test
  void domainToDBOIsMappedCorrectly() {
    MessageId messageId = MessageId.of(1L);
    ParticipantId sender = ParticipantId.of(1L);
    MessageContent messageContent = MessageContent.of("Test message");
    CreatedDateTime createdDateTime = CreatedDateTime.of(LocalDateTime.now().plusNanos(12345L));
    Message message = new Message(sender, messageContent, createdDateTime);
    message.setId(messageId);
    message.changeStatusFlagTo(MessageStatus.DELETED);

    MessageDBO messageDBO = MessageDBO.from(message);

    assertThat(messageDBO.getId()).isEqualTo(messageId.id());
    assertMappedCorrectly(messageDBO, message);
  }

  @Test
  void domainToDBOIsMappedCorrectlyWithNullId() {
    Message message = MessageFactory.defaultWIthIdOf(null);
    message.changeStatusFlagTo(MessageStatus.DELETED);

    MessageDBO messageDBO = MessageDBO.from(message);

    assertThat(messageDBO.getId()).isNull();
    assertMappedCorrectly(messageDBO, message);
  }

  @Test
  void DBOToDomainIsMappedCorrectly() {
    MessageDBO messageDBO = MessageDBO.from(
        MessageFactory.defaultsWithStatusFlag(MessageStatus.DELETED));

    Message message = messageDBO.asMessage();

    assertThat(message.id()).isEqualTo(MessageId.of(messageDBO.getId()));
    assertMappedCorrectly(messageDBO, message);
  }

  @Test
  void DBOTODomainIsMappedCorrectlyWithNullId() {
    MessageDBO messageDBO = MessageDBO.from(
        MessageFactory.defaultWIthIdAndStatusFlag(null, MessageStatus.DELETED));

    Message message = messageDBO.asMessage();

    assertThat(message.id()).isNull();
    assertMappedCorrectly(messageDBO, message);
  }

  private static void assertMappedCorrectly(MessageDBO messageDBO, Message message) {
    assertThat(message.sender()).isEqualTo(ParticipantId.of(messageDBO.getSenderId()));
    assertThat(message.content()).isEqualTo(MessageContent.of(messageDBO.getContent()));
    assertThat(message.statusFlag()).isEqualTo(MessageStatus.valueOf(messageDBO.getStatusFlag()));
    assertThat(message.createdDateTime()).isEqualTo(
        CreatedDateTime.of(messageDBO.getCreatedAt()));
  }
}