package hu.martin.chatservice.domain;

public record ParticipantId(Long id) {
    public static ParticipantId of(Long id) {
        return new ParticipantId(id);
    }
}
