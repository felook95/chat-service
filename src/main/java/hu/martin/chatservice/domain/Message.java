package hu.martin.chatservice.domain;

public class Message {

    private MessageId id;

    private MessageContent content;
    private MessageStatus statusFlag;

    private final CreatedDateTime createdDateTime;

    public Message(MessageContent content, CreatedDateTime createdDateTime) {
        changeContentTo(content);
        changeStatusFlagTo(MessageStatus.CREATED);
        this.createdDateTime = createdDateTime;
    }

    public void changeContentTo(MessageContent content) {
        this.content = content;
    }

    public MessageContent content() {
        return content;
    }

    public void deleted() {
        statusFlag = MessageStatus.DELETED;
    }

    public MessageStatus statusFlag() {
        return statusFlag;
    }

    public MessageId id() {
        return id;
    }

    public void setId(MessageId id) {
        this.id = id;
    }

    public CreatedDateTime createdDateTime() {
        return createdDateTime;
    }

    public void changeStatusFlagTo(MessageStatus newStatusFlag) {
        this.statusFlag = newStatusFlag;
    }
}
