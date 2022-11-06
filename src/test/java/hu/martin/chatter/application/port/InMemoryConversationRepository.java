package hu.martin.chatter.application.port;

import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import reactor.core.publisher.Mono;

public class InMemoryConversationRepository implements ConversationRepository {

  private final Map<ConversationId, Conversation> conversations = new HashMap<>();
  private final AtomicLong sequence = new AtomicLong(1);

  @Override
  public Mono<Conversation> save(Conversation conversation) {
    if (conversation.getId() == null) {
      conversation.setId(ConversationId.of(sequence.getAndIncrement()));
    }
    conversations.put(conversation.getId(), conversation);
    return Mono.just(conversation);
  }

  @Override
  public Mono<Conversation> findById(ConversationId id) {
    return Mono.just(conversations.get(id));
  }
}
