package hu.martin.chatter.application;

import hu.martin.chatter.application.port.MessageRepository;
import hu.martin.chatter.domain.message.Message;
import hu.martin.chatter.domain.message.MessageContent;
import hu.martin.chatter.domain.message.MessageId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
    public Flux<Message> findAllByIdOrderedByCreatedDateTime(List<MessageId> messageIdsToLookFor, PageRequest pageRequest) {
        pageRequest = decoratePageRequestWithSortingByCreatedAt(pageRequest);
        return messageRepository.findByIdsPageable(messageIdsToLookFor, pageRequest);
    }

    private static PageRequest decoratePageRequestWithSortingByCreatedAt(PageRequest pageRequest) {
        return pageRequest.withSort(Sort.by("createdAt"));
    }
}
