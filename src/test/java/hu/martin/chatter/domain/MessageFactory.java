package hu.martin.chatter.domain;

import java.time.ZonedDateTime;

public class MessageFactory {

  public static Message withDefaults() {
    ParticipantId participantId = ParticipantId.of(1L);
    return defaultWithSender(participantId);
  }

  public static Message defaultWIthIdOf(MessageId messageId) {
    Message message = withDefaults();
    message.setId(messageId);
    return message;
  }

  public static Message defaultWithSender(ParticipantId participantId) {
    MessageContent messageContent = MessageContent.of("");
    CreatedDateTime createdDateTime = CreatedDateTime.of(ZonedDateTime.now());
    Message message = new Message(participantId, messageContent, createdDateTime);
    message.setId(MessageId.of(1L));
    return message;
  }

  public static Message defaultWIthCreatedDateTimeOf(CreatedDateTime createdDateTime) {
    ParticipantId participantId = ParticipantId.of(1L);
    MessageContent messageContent = MessageContent.of("");
    Message message = new Message(participantId, messageContent, createdDateTime);
    message.setId(MessageId.of(1L));
    return message;
  }
}
