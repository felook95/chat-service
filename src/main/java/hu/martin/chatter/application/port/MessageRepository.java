package hu.martin.chatter.application.port;

import hu.martin.chatter.application.paging.SortablePageProperties;
import hu.martin.chatter.domain.message.Message;
import hu.martin.chatter.domain.message.MessageId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public interface MessageRepository {

    Mono<Message> save(Message message);

    Mono<Message> findById(MessageId messageId);

    Flux<Message> findByIds(Set<MessageId> messageIdsToLookFor);

    Flux<Message> findByIdsPageable(List<MessageId> messageIdsToLookFor, SortablePageProperties pageProperties);
}
