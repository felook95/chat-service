package hu.martin.chatter.application.port;

import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryConversationRepository implements ConversationRepository {

  private final Map<ConversationId, Conversation> conversations = new HashMap<>();
  private final AtomicLong sequence = new AtomicLong(1);

  @Override
  public Conversation save(Conversation conversation) {
    if (conversation.getId() == null) {
      conversation.setId(ConversationId.of(sequence.getAndIncrement()));
    }
    conversations.put(conversation.getId(), conversation);
    return conversation;
  }

  @Override
  public Optional<Conversation> findById(ConversationId id) {
    return Optional.ofNullable(conversations.get(id));
  }
}