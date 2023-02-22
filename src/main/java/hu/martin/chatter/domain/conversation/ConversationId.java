package hu.martin.chatter.domain.conversation;

import java.math.BigInteger;

public record ConversationId(BigInteger id) {

    public static ConversationId of(BigInteger id) {
        return new ConversationId(id);
    }
}
