package hu.martin.chatservice.application.port;

import hu.martin.chatservice.domain.Message;
import hu.martin.chatservice.domain.MessageId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryMessageRepository implements MessageRepository {

    private final Map<MessageId, Message> messages = new HashMap<>();

    private final AtomicLong sequence = new AtomicLong();

    @Override
    public Message save(Message message) {
        if (message.id() == null) {
            message.setId(MessageId.of(sequence.getAndIncrement()));
        }
        messages.put(message.id(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(MessageId messageId) {
        return Optional.ofNullable(messages.get(messageId));
    }

    @Override
    public Set<Message> findByIds(Set<MessageId> messageIdsToLookFor) {
        return messages.values().stream()
                .filter(message -> messageIdsToLookFor.contains(message.id()))
                .collect(Collectors.toSet());
    }
}
