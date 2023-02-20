package hu.martin.chatter.application;

import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageContent;
import hu.martin.chatter.domain.MessageId;
import reactor.core.publisher.Mono;

public interface MessageService {

    Mono<Message> receiveMessage(Message message);

    Mono<Message> editMessageContent(MessageId id, MessageContent newContent);
}
