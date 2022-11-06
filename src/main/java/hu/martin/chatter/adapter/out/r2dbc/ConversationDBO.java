package hu.martin.chatter.adapter.out.r2dbc;

import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.ParticipantId;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("conversations")
public class ConversationDBO {

  @Id
  Long id;

  @Column("participant_ids")
  Set<Long> participantIds;

  @Column("message_ids")
  Set<Long> messageIds;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<Long> getParticipantIds() {
    return participantIds;
  }

  public void setParticipantIds(Set<Long> participantIds) {
    this.participantIds = participantIds;
  }

  public Set<Long> getMessageIds() {
    return messageIds;
  }

  public void setMessageIds(Set<Long> messageIds) {
    this.messageIds = messageIds;
  }

  public static ConversationDBO from(Conversation conversation) {
    ConversationDBO conversationDBO = new ConversationDBO();
    conversationDBO.setId(conversation.getId() == null ? null : conversation.getId().id());
    Set<Long> mappedParticipantIds = conversation.participants().stream().map(ParticipantId::id)
        .collect(Collectors.toSet());
    conversationDBO.setParticipantIds(mappedParticipantIds);
    Set<Long> mappedMessageIds = conversation.messages().stream().map(MessageId::id)
        .collect(Collectors.toSet());
    conversationDBO.setMessageIds(mappedMessageIds);
    return conversationDBO;
  }

  public Conversation asConversation() {
    Conversation conversation = new Conversation();
    if (id != null) {
      conversation.setId(ConversationId.of(id));
    }
    joinAllParticipantsToConversation(conversation);
    sendAllMessagesToConversation(conversation);
    return conversation;
  }

  private void sendAllMessagesToConversation(Conversation conversation) {
    if (messageIds != null) {
      messageIds.stream().map(MessageId::of).forEach(conversation::messageSent);
    }
  }

  private void joinAllParticipantsToConversation(Conversation conversation) {
    if (participantIds != null) {
      participantIds.stream().map(ParticipantId::of).forEach(conversation::joinedBy);
    }
  }
}
