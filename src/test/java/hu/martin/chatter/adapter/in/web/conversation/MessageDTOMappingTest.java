package hu.martin.chatter.adapter.in.web.conversation;

import static org.assertj.core.api.Assertions.assertThat;

import hu.martin.chatter.domain.CreatedDateTime;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageContent;
import hu.martin.chatter.domain.MessageFactory;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.MessageStatus;
import hu.martin.chatter.domain.ParticipantId;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class MessageDTOMappingTest {

  @Test
  void domainToDTOIsMappedCorrectly() {
    MessageId messageId = MessageId.of(1L);
    ParticipantId sender = ParticipantId.of(1L);
    MessageContent messageContent = MessageContent.of("Test message");
    CreatedDateTime createdDateTime = CreatedDateTime.of(LocalDateTime.now().plusNanos(12345L));
    Message message = new Message(sender, messageContent, createdDateTime);
    message.setId(messageId);
    message.changeStatusFlagTo(MessageStatus.DELETED);

    MessageDTO messageDTO = MessageDTO.from(message);

    assertThat(messageDTO.id()).isEqualTo(messageId.id());
    assertMappedCorrectly(messageDTO, message);
  }

  @Test
  void domainToDTOIsMappedCorrectlyWithNullId() {
    Message message = MessageFactory.defaultWIthIdOf(null);
    message.changeStatusFlagTo(MessageStatus.DELETED);

    MessageDTO messageDTO = MessageDTO.from(message);

    assertThat(messageDTO.id()).isNull();
    assertMappedCorrectly(messageDTO, message);
  }

  @Test
  void DTOToDomainIsMappedCorrectly() {
    MessageDTO messageDTO = MessageDTO.from(
        MessageFactory.defaultsWithStatusFlag(MessageStatus.DELETED));

    Message message = messageDTO.asMessage();

    assertThat(message.id()).isEqualTo(MessageId.of(messageDTO.id()));
    assertMappedCorrectly(messageDTO, message);
  }

  @Test
  void DTOTODomainIsMappedCorrectlyWithNullId() {
    MessageDTO messageDTO = MessageDTO.from(
        MessageFactory.defaultWIthIdAndStatusFlag(null, MessageStatus.DELETED));

    Message message = messageDTO.asMessage();

    assertThat(message.id()).isNull();
    assertMappedCorrectly(messageDTO, message);
  }

  private static void assertMappedCorrectly(MessageDTO messageDTO, Message message) {
    assertThat(message.sender()).isEqualTo(ParticipantId.of(messageDTO.senderId()));
    assertThat(message.content()).isEqualTo(MessageContent.of(messageDTO.content()));
    assertThat(message.statusFlag()).isEqualTo(MessageStatus.valueOf(messageDTO.statusFlag()));
    assertThat(message.createdDateTime()).isEqualTo(
        CreatedDateTime.of(messageDTO.createdDateTime()));
  }

}
