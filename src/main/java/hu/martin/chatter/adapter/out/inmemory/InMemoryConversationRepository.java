package hu.martin.chatter.adapter.out.inmemory;

import hu.martin.chatter.application.port.ConversationRepository;
import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class InMemoryConversationRepository implements ConversationRepository {

    private final Map<ConversationId, Conversation> conversations = new HashMap<>();
    private final AtomicReference<BigInteger> sequence = new AtomicReference<>(BigInteger.ONE);

    @Override
    public Mono<Conversation> save(Conversation conversation) {
        if (conversation.getId() == null) {
            BigInteger id = getNextId();
            conversation.setId(ConversationId.of(id));
        }
        conversations.put(conversation.getId(), conversation);
        return Mono.just(conversation);
    }

    private BigInteger getNextId() {
        return sequence.accumulateAndGet(BigInteger.ONE, BigInteger::add);
    }

    @Override
    public Mono<Conversation> findById(ConversationId id) {
        return Mono.justOrEmpty(conversations.get(id));
    }
}
