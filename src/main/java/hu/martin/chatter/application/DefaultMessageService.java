package hu.martin.chatter.application;

import hu.martin.chatter.application.paging.PageProperties;
import hu.martin.chatter.application.paging.SortablePageProperties;
import hu.martin.chatter.application.port.MessageRepository;
import hu.martin.chatter.domain.message.Message;
import hu.martin.chatter.domain.message.MessageContent;
import hu.martin.chatter.domain.message.MessageId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DefaultMessageService implements MessageService {

    private final MessageRepository messageRepository;

    public DefaultMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Mono<Message> receiveMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Mono<Message> editMessageContent(MessageId id, MessageContent newContent) {
        return messageRepository.findById(id).switchIfEmpty(Mono.error(MessageNotFoundException::new))
                .map(message -> {
                    message.changeContentTo(newContent);
                    return message;
                }).flatMap(messageRepository::save);
    }

    @Override
    public Flux<Message> findAllByIdOrderedByCreatedDateTime(List<MessageId> messageIdsToLookFor, PageProperties pageProperties) {
        SortablePageProperties sortablePageProperties = decoratePagePropertiesWithSortingByCreatedAt(pageProperties);
        return messageRepository.findByIdsPageable(messageIdsToLookFor, sortablePageProperties);
    }

    private static SortablePageProperties decoratePagePropertiesWithSortingByCreatedAt(PageProperties pageRequest) {
        return new SortablePageProperties(pageRequest.pageIndex(), pageRequest.pageSize(), "createdAt");
    }
}
