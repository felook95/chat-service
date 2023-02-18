package hu.martin.chatter.adapter.out.cassandra;

import hu.martin.chatter.domain.MessageId;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@Table("sent_message")
@UserDefinedType("sent_message_type")
public class SentMessage {

  @PrimaryKey
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
