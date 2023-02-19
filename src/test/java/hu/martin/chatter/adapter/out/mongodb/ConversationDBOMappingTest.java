package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.ParticipantId;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ConversationDBOMappingTest {

  @Test
  void DBOToDomainIsMappedCorrectly() {
    ConversationDBO conversationDBO = new ConversationDBO();
    conversationDBO.setId(BigInteger.valueOf(999L));
    conversationDBO.setParticipantIds(Set.of(
        new JoinedParticipant(BigInteger.valueOf(1L)),
        new JoinedParticipant(BigInteger.valueOf(2L))
    ));
    conversationDBO.setMessageIds(Set.of(
        new SentMessage(BigInteger.valueOf(1L)),
        new SentMessage(BigInteger.valueOf(2L)),
        new SentMessage(BigInteger.valueOf(3L))
    ));

    Conversation conversation = conversationDBO.asConversation();

    assertThat(conversation.getId().id()).isEqualTo(999L);
    assertThat(conversation.participants()).extracting(ParticipantId::id).containsOnly(BigInteger.valueOf(1L), BigInteger.valueOf(2L));
    assertThat(conversation.messages()).extracting(MessageId::id).containsOnly(BigInteger.valueOf(1L), BigInteger.valueOf(2L), BigInteger.valueOf(3L));
  }

  @Test
  void domainToDTOIsMappedCorrectly() {
    Conversation conversationToTransform = new Conversation();
    conversationToTransform.setId(ConversationId.of(BigInteger.valueOf(1L)));
    conversationToTransform.joinedBy(ParticipantId.of(BigInteger.valueOf(2L)));
    conversationToTransform.messageSent(MessageId.of(BigInteger.valueOf(3L)));
    conversationToTransform.messageSent(MessageId.of(BigInteger.valueOf(4L)));

    ConversationDBO conversationDBO = ConversationDBO.from(conversationToTransform);

    assertThat(conversationDBO.getId()).isEqualTo(BigInteger.valueOf(1L));
    assertThat(conversationDBO.getParticipantIds())
        .extracting(JoinedParticipant::asParticipantId)
        .extracting(ParticipantId::id)
        .containsOnly(BigInteger.valueOf(2L));
    assertThat(conversationDBO.getMessageIds())
        .extracting(SentMessage::asMessageId)
        .extracting(MessageId::id).containsOnly(BigInteger.valueOf(3L), BigInteger.valueOf(4L));
  }

  @Test
  void domainToDTOIsMappedCorrectlyWithNullId() {
    Conversation conversationToTransform = new Conversation();
    conversationToTransform.setId(null);
    conversationToTransform.joinedBy(ParticipantId.of(BigInteger.valueOf(2L)));
    conversationToTransform.messageSent(MessageId.of(BigInteger.valueOf(3L)));
    conversationToTransform.messageSent(MessageId.of(BigInteger.valueOf(4L)));

    ConversationDBO conversationDBO = ConversationDBO.from(conversationToTransform);

    assertThat(conversationDBO.getId()).isNull();
    assertThat(conversationDBO.getParticipantIds())
        .extracting(JoinedParticipant::asParticipantId)
        .extracting(ParticipantId::id).containsOnly(BigInteger.valueOf(2L));
    assertThat(conversationDBO.getMessageIds())
        .extracting(SentMessage::asMessageId)
        .extracting(MessageId::id).containsOnly(BigInteger.valueOf(3L), BigInteger.valueOf(4L));
  }

  @Test
  void DTOTODomainMappingWithNullParticipantIdsMapsToEmptySet() {
    ConversationDBO conversationDBO = new ConversationDBO();
    conversationDBO.setParticipantIds(null);
    Conversation conversation = conversationDBO.asConversation();

    assertThat(conversation.participants()).isInstanceOf(Set.class);
    assertThat(conversation.participants()).isEmpty();
  }

  @Test
  void DTOTODomainMappingWithNullMessageIdsMapsToEmptySet() {
    ConversationDBO conversationDBO = new ConversationDBO();
    conversationDBO.setMessageIds(null);
    Conversation conversation = conversationDBO.asConversation();

    assertThat(conversation.messages()).isInstanceOf(Set.class);
    assertThat(conversation.messages()).isEmpty();
  }
}