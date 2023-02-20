package hu.martin.chatter.domain;

import java.math.BigInteger;

public record MessageId(BigInteger id) {

    public static MessageId of(BigInteger id) {
        return new MessageId(id);
    }
}
