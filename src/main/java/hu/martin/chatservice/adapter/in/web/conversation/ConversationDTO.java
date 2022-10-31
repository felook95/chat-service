package hu.martin.chatservice.adapter.in.web.conversation;

import hu.martin.chatservice.domain.Conversation;
import hu.martin.chatservice.domain.ConversationId;
import hu.martin.chatservice.domain.MessageId;
import hu.martin.chatservice.domain.ParticipantId;
import java.util.Set;
import java.util.stream.Collectors;

public record ConversationDTO(Long id, Set<Long> participantIds, Set<Long> messageIds) {

  public static ConversationDTO from(Conversation conversation) {
    return new ConversationDTO(conversation.getId() == null ? null : conversation.getId().id(),
        conversation.participants().stream().map(ParticipantId::id).collect(Collectors.toSet()),
        conversation.messages().stream().map(MessageId::id).collect(Collectors.toSet()));
  }

  public Conversation asConversation() {
    Conversation conversation = new Conversation();
    if (id != null) {
      conversation.setId(ConversationId.of(id));
    }
    participantIds().stream().map(ParticipantId::of).forEach(conversation::joinedBy);
    messageIds().stream().map(MessageId::of).forEach(conversation::messageSent);
    return conversation;
  }
}
