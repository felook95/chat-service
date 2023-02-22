package hu.martin.chatter.domain.conversation;

import hu.martin.chatter.domain.conversation.Conversation;
import hu.martin.chatter.domain.conversation.ConversationId;
import hu.martin.chatter.domain.participant.ParticipantId;

import java.math.BigInteger;
import java.util.Arrays;

public class ConversationFactory {

    public static Conversation withParticipants(ParticipantId... participantIds) {
        Conversation conversation = new Conversation();
        Arrays.stream(participantIds).forEach(conversation::joinedBy);
        return conversation;
    }

    public static Conversation withDefaults() {
        Conversation conversation = new Conversation();
        conversation.setId(ConversationId.of(BigInteger.valueOf(1L)));
        return conversation;
    }
}
