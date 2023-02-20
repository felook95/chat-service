package hu.martin.chatter.application;

import hu.martin.chatter.application.port.MessageRepository;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageContent;
import hu.martin.chatter.domain.MessageId;
import reactor.core.publisher.Mono;

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
}
