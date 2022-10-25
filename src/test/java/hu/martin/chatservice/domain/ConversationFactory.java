package hu.martin.chatservice.domain;

import java.util.Arrays;

public class ConversationFactory {
    public static Conversation withParticipants(ParticipantId... participantIds) {
        Conversation conversation = new Conversation();
        Arrays.stream(participantIds).forEach(conversation::joinedBy);
        return conversation;
    }
}
