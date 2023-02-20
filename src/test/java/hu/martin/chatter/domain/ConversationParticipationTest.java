package hu.martin.chatter.domain;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unitTest")
class ConversationParticipationTest {

    @Test
    void newConversationHasZeroParticipants() {
        Conversation conversation = new Conversation();

        assertThat(conversation.participants()).isEmpty();
    }

    @Test
    void joinParticipant() {
        ParticipantId participantId = ParticipantId.of(BigInteger.valueOf(1L));

        Conversation conversation = new Conversation();
        conversation.joinedBy(participantId);

        assertThat(conversation.participants()).containsOnly(participantId);
        assertThat(conversation.isJoined(participantId)).isTrue();
    }

    @Test
    void missingParticipant() {
        Conversation conversation = new Conversation();
        ParticipantId participantId = ParticipantId.of(BigInteger.valueOf(1L));

        assertThat(conversation.isJoined(participantId)).isFalse();
    }

    @Test
    void alreadyJoinedParticipantNotJoinedAgain() {
        ParticipantId addedParticipantId = ParticipantId.of(BigInteger.valueOf(1L));
        Conversation conversation = ConversationFactory.withParticipants(addedParticipantId);

        conversation.joinedBy(addedParticipantId);

        assertThat(conversation.participants()).hasSize(1);
    }

    @Test
    void nullParticipantIdNotAllowedToJoin() {
        Conversation conversation = new Conversation();

        assertThatThrownBy(() -> conversation.joinedBy(null)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void leaveConversation() {
        ParticipantId participantId = ParticipantId.of(BigInteger.valueOf(1L));
        Conversation conversation = ConversationFactory.withParticipants(participantId);

        conversation.leftBy(participantId);

        assertThat(conversation.hasParticipant(participantId)).isFalse();
    }

    @Test
    void leavingOnlyRemovesTheLeftUser() {
        ParticipantId leavingParticipantId = ParticipantId.of(BigInteger.valueOf(1L));
        ParticipantId stayingParticipantId = ParticipantId.of(BigInteger.valueOf(2L));
        Conversation conversation = ConversationFactory.withParticipants(leavingParticipantId,
                stayingParticipantId);

        conversation.leftBy(leavingParticipantId);

        assertThat(conversation.participants()).containsOnly(stayingParticipantId);
    }
}
