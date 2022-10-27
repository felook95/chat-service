package hu.martin.chatservice.domain;

public record MessageId(Long id) {
    public static MessageId of(Long id) {
        return new MessageId(id);
    }
}
