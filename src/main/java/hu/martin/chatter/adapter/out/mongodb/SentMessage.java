package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.domain.MessageId;

public class SentMessage {

  Long id;

  public SentMessage() {
  }

  public SentMessage(Long id) {
    this.id = id;
  }

  static SentMessage fromMessageId(MessageId messageId) {
    return new SentMessage(messageId.id());
  }

  MessageId asMessageId() {
    return MessageId.of(id);
  }
}
