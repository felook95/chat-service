package hu.martin.chatservice.application.port;

import hu.martin.chatservice.domain.Message;
import hu.martin.chatservice.domain.MessageId;

import java.util.Optional;

public interface MessageRepository {

    Message save(Message message);

    Optional<Message> findById(MessageId messageId);
}
