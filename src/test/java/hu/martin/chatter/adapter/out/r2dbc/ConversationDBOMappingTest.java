package hu.martin.chatter.adapter.out.r2dbc;

import static org.assertj.core.api.Assertions.assertThat;

import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.ParticipantId;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ConversationDBOMappingTest {

  @Test
  void DBOToDomainIsMappedCorrectly() {
    ConversationDBO conversationDBO = new ConversationDBO();
    conversationDBO.setId(999L);
    conversationDBO.setParticipantIds(Set.of(1L, 2L));
    conversationDBO.setMessageIds(Set.of(1L, 2L, 3L));

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
    assertThat(conversationDBO.getParticipantIds()).containsOnly(2L);
    assertThat(conversationDBO.getMessageIds()).containsOnly(3L, 4L);
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
    assertThat(conversationDBO.getParticipantIds()).containsOnly(2L);
    assertThat(conversationDBO.getMessageIds()).containsOnly(3L, 4L);
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