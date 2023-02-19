package hu.martin.chatter.adapter.in.web.conversation;

import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.ParticipantId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unitTest")
class ConversationDTOMappingTest {

  @Test
  void DTOToDomainIsMappedCorrectly() {
    ConversationDTO conversationDTO = new ConversationDTO(BigInteger.valueOf(1_1L), Set.of(BigInteger.valueOf(2_1L), BigInteger.valueOf(2_2L)),
        Set.of(BigInteger.valueOf(3_1L), BigInteger.valueOf(3_2L)));

    Conversation conversation = conversationDTO.asConversation();

    assertThat(conversation.getId()).isEqualTo(ConversationId.of(BigInteger.valueOf(1_1L)));
    assertThat(conversation.participants()).extracting(ParticipantId::id).contains(BigInteger.valueOf(2_1L), BigInteger.valueOf(2_2L));
    assertThat(conversation.messages()).extracting(MessageId::id).contains(BigInteger.valueOf(3_1L), BigInteger.valueOf(3_2L));
  }

  @Test
  void domainToDTOIsMappedCorrectly() {
    Conversation conversationToTransform = new Conversation();
    conversationToTransform.setId(ConversationId.of(BigInteger.valueOf(1_1L)));
    conversationToTransform.joinedBy(ParticipantId.of(BigInteger.valueOf(2_1L)));
    conversationToTransform.messageSent(MessageId.of(BigInteger.valueOf(3_1L)));
    conversationToTransform.messageSent(MessageId.of(BigInteger.valueOf(3_2L)));

    ConversationDTO conversationDTO = ConversationDTO.from(conversationToTransform);

    Conversation mappedConversation = conversationDTO.asConversation();
    assertThat(mappedConversation.getId()).isEqualTo(ConversationId.of(BigInteger.valueOf(1_1L)));
    assertThat(mappedConversation.participants()).containsOnly(ParticipantId.of(BigInteger.valueOf(2_1L)));
    assertThat(mappedConversation.messages()).containsOnly(MessageId.of(BigInteger.valueOf(3_1L)), MessageId.of(BigInteger.valueOf(3_2L)));
  }

  @Test
  void domainToDTOIsMappedCorrectlyWithNullId() {
    Conversation conversationToTransform = new Conversation();
    conversationToTransform.setId(null);
    conversationToTransform.joinedBy(ParticipantId.of(BigInteger.valueOf(2_1L)));
    conversationToTransform.messageSent(MessageId.of(BigInteger.valueOf(3_1L)));
    conversationToTransform.messageSent(MessageId.of(BigInteger.valueOf(3_2L)));

    ConversationDTO conversationDTO = ConversationDTO.from(conversationToTransform);

    Conversation mappedConversation = conversationDTO.asConversation();
    assertThat(mappedConversation.getId()).isNull();
    assertThat(mappedConversation.participants()).containsOnly(ParticipantId.of(BigInteger.valueOf(2_1L)));
    assertThat(mappedConversation.messages()).containsOnly(MessageId.of(BigInteger.valueOf(3_1L)), MessageId.of(BigInteger.valueOf(3_2L)));
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
