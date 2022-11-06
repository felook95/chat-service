package hu.martin.chatter.application.port;

import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageId;
import java.util.Set;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageRepository {

  Mono<Message> save(Message message);

  Mono<Message> findById(MessageId messageId);

  Flux<Message> findByIds(Set<MessageId> messageIdsToLookFor);
}
