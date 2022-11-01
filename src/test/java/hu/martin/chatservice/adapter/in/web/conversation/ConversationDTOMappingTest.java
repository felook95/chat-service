package hu.martin.chatservice.adapter.in.web.conversation;

import static org.assertj.core.api.Assertions.assertThat;

import hu.martin.chatservice.domain.Conversation;
import hu.martin.chatservice.domain.ConversationId;
import hu.martin.chatservice.domain.MessageId;
import hu.martin.chatservice.domain.ParticipantId;
import java.util.Set;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class ConversationDTOMappingTest {

  @Test
  void DTOToDomainIsMappedCorrectly() {
    ConversationDTO conversationDTO = new ConversationDTO(1_1L, Set.of(2_1L, 2_2L),
        Set.of(3_1L, 3_2L));

    Conversation conversation = conversationDTO.asConversation();

    assertThat(conversation.getId()).isEqualTo(ConversationId.of(1_1L));
    assertThat(conversation.participants()).extracting(ParticipantId::id).contains(2_1L, 2_2L);
    assertThat(conversation.messages()).extracting(MessageId::id).contains(3_1L, 3_2L);
  }

  @Test
  void domainToDTOIsMappedCorrectly() {
    Conversation conversationToTransform = new Conversation();
    conversationToTransform.setId(ConversationId.of(1_1L));
    conversationToTransform.joinedBy(ParticipantId.of(2_1L));
    conversationToTransform.messageSent(MessageId.of(3_1L));
    conversationToTransform.messageSent(MessageId.of(3_2L));

    ConversationDTO conversationDTO = ConversationDTO.from(conversationToTransform);

    Conversation mappedConversation = conversationDTO.asConversation();
    assertThat(mappedConversation.getId()).isEqualTo(ConversationId.of(1_1L));
    assertThat(mappedConversation.participants()).containsOnly(ParticipantId.of(2_1L));
    assertThat(mappedConversation.messages()).containsOnly(MessageId.of(3_1L), MessageId.of(3_2L));
  }

  @Test
  void domainToDTOIsMappedCorrectlyWithNullId() {
    Conversation conversationToTransform = new Conversation();
    conversationToTransform.setId(null);
    conversationToTransform.joinedBy(ParticipantId.of(2_1L));
    conversationToTransform.messageSent(MessageId.of(3_1L));
    conversationToTransform.messageSent(MessageId.of(3_2L));

    ConversationDTO conversationDTO = ConversationDTO.from(conversationToTransform);

    Conversation mappedConversation = conversationDTO.asConversation();
    assertThat(mappedConversation.getId()).isNull();
    assertThat(mappedConversation.participants()).containsOnly(ParticipantId.of(2_1L));
    assertThat(mappedConversation.messages()).containsOnly(MessageId.of(3_1L), MessageId.of(3_2L));
  }

  @Test
  void DTOTODomainMappingWithNullMessageIdsMapsToEmptySet() {
    ConversationDTO conversationDTO = new ConversationDTO(null, null, null);

    Conversation conversation = conversationDTO.asConversation();

    assertThat(conversation.messages()).isEmpty();
  }

  @Test
  void DTOTODomainMappingWithNullParticipantIdsMapsToEmptySet() {
    ConversationDTO conversationDTO = new ConversationDTO(null, null, null);

    Conversation conversation = conversationDTO.asConversation();

    assertThat(conversation.participants()).isEmpty();
  }
}
