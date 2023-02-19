package hu.martin.chatter.adapter.out.inmemory;

import hu.martin.chatter.application.port.MessageRepository;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class InMemoryMessageRepository implements MessageRepository {

    private final Map<MessageId, Message> messages = new HashMap<>();

    private final AtomicReference<BigInteger> sequence = new AtomicReference<>(BigInteger.ONE);

    @Override
    public Mono<Message> save(Message message) {
        if (message.id() == null) {
            BigInteger id = getNextId();
            message.setId(MessageId.of(id));
        }
        messages.put(message.id(), message);
        return Mono.just(message);
    }

    private BigInteger getNextId() {
        return sequence.accumulateAndGet(BigInteger.ONE, BigInteger::add);
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
