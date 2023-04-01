package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.application.paging.SortablePageProperties;
import hu.martin.chatter.application.port.MessageRepository;
import hu.martin.chatter.domain.message.Message;
import hu.martin.chatter.domain.message.MessageId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class MessageR2DBCRepositoryAdapter implements MessageRepository {

    private final MessageReactiveCrudRepository messageRepository;

    public MessageR2DBCRepositoryAdapter(MessageReactiveCrudRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Mono<Message> save(Message message) {
        MessageDBO messageDBO = MessageDBO.from(message);
        return messageRepository.save(messageDBO).map(MessageDBO::asMessage);
    }

    @Override
    public Mono<Message> findById(MessageId messageId) {
        return messageRepository.findById(messageId.id()).map(MessageDBO::asMessage);
    }

    @Override
    public Flux<Message> findByIds(Set<MessageId> messageIdsToLookFor) {
        Collection<BigInteger> messageIds = messageIdsToLookFor.stream().map(MessageId::id).collect(Collectors.toSet());
        return messageRepository.findAllById(messageIds).map(MessageDBO::asMessage);
    }

    @Override
    public Flux<Message> findByIdsPageable(List<MessageId> messageIdsToLookFor, SortablePageProperties pageProperties) {
        Collection<BigInteger> messageIds = messageIdsToLookFor.stream().map(MessageId::id).collect(Collectors.toSet());
        PageRequest pageRequest = parseToPageRequest(pageProperties);
        return messageRepository.findByIdIn(messageIds, pageRequest)
                .map(MessageDBO::asMessage);
    }

    private static PageRequest parseToPageRequest(SortablePageProperties pageProperties) {
        return PageRequest.of(pageProperties.pageIndex(), pageProperties.pageSize(), Sort.by(pageProperties.sortBy()));
    }
}
