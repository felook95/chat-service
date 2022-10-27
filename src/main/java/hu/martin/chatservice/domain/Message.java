package hu.martin.chatservice.domain;

public class Message {

    private MessageId id;

    private MessageContent content;
    private MessageStatus statusFlag;

    public Message(MessageContent content) {
        changeContentTo(content);
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
}
