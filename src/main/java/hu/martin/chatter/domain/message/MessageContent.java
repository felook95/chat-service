package hu.martin.chatter.domain.message;

public record MessageContent(String content) {

    public static MessageContent of(String content) {
        return new MessageContent(content);
    }
}
