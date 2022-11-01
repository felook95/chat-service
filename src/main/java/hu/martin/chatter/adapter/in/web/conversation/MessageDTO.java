package hu.martin.chatter.adapter.in.web.conversation;

import hu.martin.chatter.domain.CreatedDateTime;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageContent;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.ParticipantId;
import java.time.ZonedDateTime;

public record MessageDTO(Long id, Long senderId, String content, ZonedDateTime createdDateTime) {

  public static MessageDTO from(Message message) {
    return new MessageDTO(
        message.id() == null ? null : message.id().id(),
        message.sender().id(),
        message.content().content(),
        message.createdDateTime().createdDateTime());
  }

  public Message asMessage() {
    Message message = new Message(
        ParticipantId.of(senderId),
        MessageContent.of(content),
        CreatedDateTime.of(createdDateTime)
    );
    if (id != null) {
      message.setId(MessageId.of(id));
    }
    return message;
  }
}
