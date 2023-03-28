package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.domain.message.MessageFactory;
import hu.martin.chatter.domain.message.*;
import hu.martin.chatter.domain.participant.ParticipantId;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MessageDBOMappingTest {

    private static void assertMappedCorrectly(MessageDBO messageDBO, Message message) {
        assertThat(message.sender()).isEqualTo(messageDBO.getSenderId().asParticipantId());
        assertThat(message.content()).isEqualTo(MessageContent.of(messageDBO.getContent()));
        assertThat(message.statusFlag()).isEqualTo(MessageStatus.valueOf(messageDBO.getStatusFlag()));
        assertThat(message.createdDateTime()).isEqualTo(
                CreatedDateTime.of(messageDBO.getCreatedAt()));
    }

    @Test
    void domainToDBOIsMappedCorrectly() {
        MessageId messageId = MessageId.of(BigInteger.valueOf(1L));
        ParticipantId sender = ParticipantId.of(BigInteger.valueOf(1L));
        MessageContent messageContent = MessageContent.of("Test message");
        CreatedDateTime createdDateTime = CreatedDateTime.of(LocalDateTime.now().plusNanos(12345L));
        Message message = new Message(sender, messageContent, createdDateTime);
        message.changeIdTo(messageId);
        message.changeStatusFlagTo(MessageStatus.DELETED);

        MessageDBO messageDBO = MessageDBO.from(message);

        assertThat(messageDBO.getId()).isEqualTo(messageId.id());
        assertMappedCorrectly(messageDBO, message);
    }

    @Test
    void domainToDBOIsMappedCorrectlyWithNullId() {
        Message message = MessageFactory.defaultWIthIdOf(null);
        message.changeStatusFlagTo(MessageStatus.DELETED);

        MessageDBO messageDBO = MessageDBO.from(message);

        assertThat(messageDBO.getId()).isNull();
        assertMappedCorrectly(messageDBO, message);
    }

    @Test
    void DBOToDomainIsMappedCorrectly() {
        MessageDBO messageDBO = MessageDBO.from(
                MessageFactory.defaultsWithStatusFlag(MessageStatus.DELETED));

        Message message = messageDBO.asMessage();

        assertThat(message.id()).isEqualTo(MessageId.of(messageDBO.getId()));
        assertMappedCorrectly(messageDBO, message);
    }

    @Test
    void DBOTODomainIsMappedCorrectlyWithNullId() {
        MessageDBO messageDBO = MessageDBO.from(
                MessageFactory.defaultWithIdAndStatusFlag(null, MessageStatus.DELETED));

        Message message = messageDBO.asMessage();

        assertThat(message.id()).isNull();
        assertMappedCorrectly(messageDBO, message);
    }
}