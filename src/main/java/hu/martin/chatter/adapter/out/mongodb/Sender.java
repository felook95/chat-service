package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.domain.ParticipantId;

public class Sender {

    Long id;

    public Sender() {
    }

    public Sender(Long id) {
        this.id = id;
    }

    static Sender fromParticipantId(ParticipantId participantId) {
        return new Sender(participantId.id());
    }

    ParticipantId asParticipantId() {
        return ParticipantId.of(id);
    }
}
