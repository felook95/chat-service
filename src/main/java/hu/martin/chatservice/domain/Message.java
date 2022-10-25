package hu.martin.chatservice.domain;

public record Message(ParticipantId senderId, String message) {
}
