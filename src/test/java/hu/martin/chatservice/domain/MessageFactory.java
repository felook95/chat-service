package hu.martin.chatservice.domain;

import java.time.ZonedDateTime;

public class MessageFactory {

  public static Message defaultWithContentOf(String content) {
    ParticipantId participantId = ParticipantId.of(1L);
    MessageContent messageContent = MessageContent.of(content);
    CreatedDateTime createdDateTime = CreatedDateTime.of(ZonedDateTime.now());
    return new Message(participantId, messageContent, createdDateTime);
  }

  public static Message defaultWIthIdOf(Long messageId) {
    ParticipantId participantId = ParticipantId.of(1L);
    MessageContent messageContent = MessageContent.of("");
    CreatedDateTime createdDateTime = CreatedDateTime.of(ZonedDateTime.now());
    Message message = new Message(participantId, messageContent, createdDateTime);
    message.setId(MessageId.of(messageId));
    return message;
  }
}