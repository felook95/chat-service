package hu.martin.chatservice.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Conversation {

  private final Set<ParticipantId> participants = new HashSet<>();
  private final Set<MessageId> messages = new HashSet<>();
  private ConversationId conversationId;

  public void joinedBy(ParticipantId participantId) {
    assertNotNull(participantId, "participantId must not be null!");
    participants.add(participantId);
  }

  private void assertNotNull(Object object, String message) {
    if (object == null) {
      throw new IllegalArgumentException(message);
    }
  }

  public boolean hasParticipant(ParticipantId participantId) {
    return participants.contains(participantId);
  }

  public Set<ParticipantId> participants() {
    return Collections.unmodifiableSet(participants);
  }

  public void leftBy(ParticipantId participantId) {
    participants.remove(participantId);
  }

  public boolean isJoined(ParticipantId participantId) {
    return participants.contains(participantId);
  }

  public Set<MessageId> messages() {
    return messages;
  }

  public void messageSent(MessageId messageId) {
    assertNotNull(messageId, "messageId must not be null!");
    messages.add(messageId);
  }

  public ConversationId id() {
    return conversationId;
  }

  public void setId(ConversationId conversationId) {
    this.conversationId = conversationId;
  }
}
