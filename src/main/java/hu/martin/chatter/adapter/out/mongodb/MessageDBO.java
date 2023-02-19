package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.domain.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Document(collection = "messages")
public class MessageDBO {

    @Id
    BigInteger id;

    Sender senderId;

    String content;

    LocalDateTime createdAt;

    String statusFlag;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Sender getSenderId() {
        return senderId;
    }

    public void setSenderId(Sender senderId) {
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
        messageDBO.setSenderId(Sender.fromParticipantId(message.sender()));
        messageDBO.setContent(message.content().content());
        messageDBO.setCreatedAt(message.createdDateTime().createdDateTime());
        messageDBO.setStatusFlag(message.statusFlag().name());
        return messageDBO;
    }

    public Message asMessage() {
        Message message = new Message(
                senderId.asParticipantId(),
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
