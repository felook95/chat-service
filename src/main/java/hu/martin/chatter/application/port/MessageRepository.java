package hu.martin.chatter.application.port;

import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageId;
import java.util.Optional;
import java.util.Set;

public interface MessageRepository {

  Message save(Message message);

  Optional<Message> findById(MessageId messageId);

  Set<Message> findByIds(Set<MessageId> messageIdsToLookFor);
}
