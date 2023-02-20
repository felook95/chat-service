package hu.martin.chatter.domain;

import java.math.BigInteger;

public record ParticipantId(BigInteger id) {

    public static ParticipantId of(BigInteger id) {
        return new ParticipantId(id);
    }
}
