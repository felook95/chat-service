package hu.martin.chatservice.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Conversation {

    private final Set<ParticipantId> participants = new HashSet<>();
    private final Set<Message> messages = new HashSet<>();

    public void joinedBy(ParticipantId participantId) {
        assertNotNull(participantId);
        participants.add(participantId);
    }

    private void assertNotNull(ParticipantId participantId) {
        if (participantId == null) {
            throw new IllegalArgumentException("participantId must not be null!");
        }
    }

    public boolean hasParticipant(ParticipantId participantId) {
        return participants.contains(participantId);
    }

    public Set<ParticipantId> participants() {
        return Collections.unmodifiableSet(participants);
    }

    public void leftBy(ParticipantId participantId) {
        participants.remove(participantId);
    }

    public boolean isJoined(ParticipantId participantId) {
        return participants.contains(participantId);
    }

    public Set<Message> messages() {
        return messages;
    }

    public void messageSent(Message message) {
        messages.add(message);
    }

    public void deleteMessage(Message message) {
        messages.remove(message);
    }
}
