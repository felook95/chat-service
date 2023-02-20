package hu.martin.chatter.domain;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class MessageFactory {

    public static Message withDefaults() {
        ParticipantId participantId = ParticipantId.of(BigInteger.valueOf(1L));
        return defaultWithSender(participantId);
    }

    public static Message defaultWIthIdOf(MessageId messageId) {
        Message message = withDefaults();
        message.setId(messageId);
        return message;
    }

    public static Message defaultWithSender(ParticipantId participantId) {
        MessageContent messageContent = MessageContent.of("");
        CreatedDateTime createdDateTime = CreatedDateTime.of(LocalDateTime.now());
        Message message = new Message(participantId, messageContent, createdDateTime);
        message.setId(MessageId.of(BigInteger.valueOf(1L)));
        return message;
    }

    public static Message defaultWIthCreatedDateTimeOf(CreatedDateTime createdDateTime) {
        ParticipantId participantId = ParticipantId.of(BigInteger.valueOf(1L));
        MessageContent messageContent = MessageContent.of("");
        Message message = new Message(participantId, messageContent, createdDateTime);
        message.setId(MessageId.of(BigInteger.valueOf(1L)));
        return message;
    }

    public static Message defaultsWithStatusFlag(MessageStatus newStatusFlag) {
        Message message = withDefaults();
        message.changeStatusFlagTo(newStatusFlag);
        return message;
    }

    public static Message defaultWIthIdAndStatusFlag(MessageId messageId,
                                                     MessageStatus messageStatus) {
        Message message = defaultWIthIdOf(messageId);
        message.changeStatusFlagTo(messageStatus);
        return message;
    }
}
