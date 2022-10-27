package hu.martin.chatservice.domain;

public class Message {

    private MessageContent content;

    public Message(MessageContent content) {
        this.content = content;
    }

    public void changeContentTo(MessageContent content) {
        this.content = content;
    }

    public MessageContent content() {
        return content;
    }
}
