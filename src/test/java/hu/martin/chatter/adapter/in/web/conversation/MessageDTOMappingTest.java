package hu.martin.chatter.adapter.in.web.conversation;

import static org.assertj.core.api.Assertions.assertThat;

import hu.martin.chatter.domain.CreatedDateTime;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageContent;
import hu.martin.chatter.domain.MessageFactory;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.ParticipantId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class MessageDTOMappingTest {

  @Test
  void domainToDTOIsMappedCorrectly() {
    MessageId messageId = MessageId.of(1L);
    ParticipantId sender = ParticipantId.of(1L);
    MessageContent messageContent = MessageContent.of("Test message");
    CreatedDateTime createdDateTime = CreatedDateTime.of(ZonedDateTime.now().plusNanos(12345L));
    Message message = new Message(sender, messageContent, createdDateTime);
    message.setId(messageId);

    MessageDTO messageDTO = MessageDTO.from(message);

    assertThat(messageDTO.id()).isEqualTo(messageId.id());
    assertThat(messageDTO.senderId()).isEqualTo(sender.id());
    assertThat(messageDTO.content()).isEqualTo(messageContent.content());
    assertThat(messageDTO.createdDateTime()).isEqualTo(createdDateTime.createdDateTime());
  }

  @Test
  void domainToDTOIsMappedCorrectlyWithNullId() {
    Message message = MessageFactory.defaultWIthIdOf(null);

    MessageDTO messageDTO = MessageDTO.from(message);

    assertThat(messageDTO.id()).isNull();
    assertThat(messageDTO.senderId()).isEqualTo(message.sender().id());
    assertThat(messageDTO.content()).isEqualTo(message.content().content());
    assertThat(messageDTO.createdDateTime()).isEqualTo(message.createdDateTime().createdDateTime());
  }

  @Test
  void DTOToDomainIsMappedCorrectly() {
    MessageDTO messageDTO = MessageDTO.from(MessageFactory.withDefaults());

    Message message = messageDTO.asMessage();

    assertThat(message.id()).isEqualTo(MessageId.of(messageDTO.id()));
    assertMappedCorrectly(messageDTO, message);
  }

  @Test
  void DTOTODomainIsMappedCorrectlyWithNullId() {
    MessageDTO messageDTO = MessageDTO.from(MessageFactory.defaultWIthIdOf(null));

    Message message = messageDTO.asMessage();

    assertThat(message.id()).isNull();
    assertMappedCorrectly(messageDTO, message);
  }

  private static void assertMappedCorrectly(MessageDTO messageDTO, Message message) {
    assertThat(message.sender()).isEqualTo(ParticipantId.of(messageDTO.senderId()));
    assertThat(message.content()).isEqualTo(MessageContent.of(messageDTO.content()));
    assertThat(message.createdDateTime()).isEqualTo(
        CreatedDateTime.of(messageDTO.createdDateTime()));
  }

}
