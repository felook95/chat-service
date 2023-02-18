package hu.martin.chatter.adapter.out.cassandra;

import hu.martin.chatter.domain.CreatedDateTime;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageContent;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.MessageStatus;
import hu.martin.chatter.domain.ParticipantId;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("message")
public class MessageDBO {

  @Id
  @Column("id")
  Long id;

  @Column("sender_id")
  Long senderId;

  @Column("content")
  String content;

  @Column("created_at")
  LocalDateTime createdAt;

  @Column("status_flag")
  String statusFlag;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSenderId() {
    return senderId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getStatusFlag() {
    return statusFlag;
  }

  public void setStatusFlag(String statusFlag) {
    this.statusFlag = statusFlag;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public static MessageDBO from(Message message) {
    MessageDBO messageDBO = new MessageDBO();
    messageDBO.setId(message.id() == null ? null : message.id().id());
    messageDBO.setSenderId(message.sender().id());
    messageDBO.setContent(message.content().content());
    messageDBO.setCreatedAt(message.createdDateTime().createdDateTime());
    messageDBO.setStatusFlag(message.statusFlag().name());
    return messageDBO;
  }

  public Message asMessage() {
    Message message = new Message(
        ParticipantId.of(senderId),
        MessageContent.of(content),
        CreatedDateTime.of(createdAt)
    );
    message.changeStatusFlagTo(MessageStatus.valueOf(statusFlag));
    if (id != null) {
      message.setId(MessageId.of(id));
    }

    return message;
  }
}
