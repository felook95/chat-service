package hu.martin.chatter.domain;

public record MessageContent(String content) {

    public static MessageContent of(String content) {
        return new MessageContent(content);
    }
}
