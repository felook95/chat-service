package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.ParticipantId;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ConversationDBOMappingTest {

  @Test
  void DBOToDomainIsMappedCorrectly() {
    ConversationDBO conversationDBO = new ConversationDBO();
    conversationDBO.setId(999L);
    conversationDBO.setParticipantIds(Set.of(
        new JoinedParticipant(1L),
        new JoinedParticipant(2L)
    ));
    conversationDBO.setMessageIds(Set.of(
        new SentMessage(1L),
        new SentMessage(2L),
        new SentMessage(3L)
    ));

    Conversation conversation = conversationDBO.asConversation();

    assertThat(conversation.getId().id()).isEqualTo(999L);
    assertThat(conversation.participants()).extracting(ParticipantId::id).containsOnly(1L, 2L);
    assertThat(conversation.messages()).extracting(MessageId::id).containsOnly(1L, 2L, 3L);
  }

  @Test
  void domainToDTOIsMappedCorrectly() {
    Conversation conversationToTransform = new Conversation();
    conversationToTransform.setId(ConversationId.of(1L));
    conversationToTransform.joinedBy(ParticipantId.of(2L));
    conversationToTransform.messageSent(MessageId.of(3L));
    conversationToTransform.messageSent(MessageId.of(4L));

    ConversationDBO conversationDBO = ConversationDBO.from(conversationToTransform);

    assertThat(conversationDBO.getId()).isEqualTo(1L);
    assertThat(conversationDBO.getParticipantIds())
        .extracting(JoinedParticipant::asParticipantId)
        .extracting(ParticipantId::id)
        .containsOnly(2L);
    assertThat(conversationDBO.getMessageIds())
        .extracting(SentMessage::asMessageId)
        .extracting(MessageId::id).containsOnly(3L, 4L);
  }

  @Test
  void domainToDTOIsMappedCorrectlyWithNullId() {
    Conversation conversationToTransform = new Conversation();
    conversationToTransform.setId(null);
    conversationToTransform.joinedBy(ParticipantId.of(2L));
    conversationToTransform.messageSent(MessageId.of(3L));
    conversationToTransform.messageSent(MessageId.of(4L));

    ConversationDBO conversationDBO = ConversationDBO.from(conversationToTransform);

    assertThat(conversationDBO.getId()).isNull();
    assertThat(conversationDBO.getParticipantIds())
        .extracting(JoinedParticipant::asParticipantId)
        .extracting(ParticipantId::id).containsOnly(2L);
    assertThat(conversationDBO.getMessageIds())
        .extracting(SentMessage::asMessageId)
        .extracting(MessageId::id).containsOnly(3L, 4L);
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