package hu.martin.chatter.application.port;

import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface MessageRepository {

    Mono<Message> save(Message message);

    Mono<Message> findById(MessageId messageId);

    Flux<Message> findByIds(Set<MessageId> messageIdsToLookFor);
}
