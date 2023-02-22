package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.domain.participant.ParticipantId;

import java.math.BigInteger;

public class Sender {

    BigInteger id;

    public Sender() {
    }

    public Sender(BigInteger id) {
        this.id = id;
    }

    static Sender fromParticipantId(ParticipantId participantId) {
        return new Sender(participantId.id());
    }

    ParticipantId asParticipantId() {
        return ParticipantId.of(id);
    }
}
