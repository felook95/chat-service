package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.domain.message.MessageId;

import java.math.BigInteger;

public class SentMessage {

    BigInteger id;

    public SentMessage() {
    }

    public SentMessage(BigInteger id) {
        this.id = id;
    }

    static SentMessage fromMessageId(MessageId messageId) {
        return new SentMessage(messageId.id());
    }

    MessageId asMessageId() {
        return MessageId.of(id);
    }
}
