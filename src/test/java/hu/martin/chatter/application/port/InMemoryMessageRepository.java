package hu.martin.chatter.application.port;

import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageId;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class InMemoryMessageRepository implements MessageRepository {

  private final Map<MessageId, Message> messages = new HashMap<>();

  private final AtomicLong sequence = new AtomicLong(1);

  @Override
  public Mono<Message> save(Message message) {
    if (message.id() == null) {
      message.setId(MessageId.of(sequence.getAndIncrement()));
    }
    messages.put(message.id(), message);
    return Mono.just(message);
  }

  @Override
  public Mono<Message> findById(MessageId messageId) {
    return Mono.justOrEmpty(messages.get(messageId));
  }

  @Override
  public Flux<Message> findByIds(Set<MessageId> messageIdsToLookFor) {
    Stream<Message> messageStream = messages.values().stream()
        .filter(message -> messageIdsToLookFor.contains(message.id()));
    return Flux.fromStream(messageStream);
  }
}
