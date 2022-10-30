package hu.martin.chatservice.application.port;

import hu.martin.chatservice.domain.Message;
import hu.martin.chatservice.domain.MessageId;
import java.util.Optional;
import java.util.Set;

public interface MessageRepository {

  Message save(Message message);

  Optional<Message> findById(MessageId messageId);

  Set<Message> findByIds(Set<MessageId> messageIdsToLookFor);
}
