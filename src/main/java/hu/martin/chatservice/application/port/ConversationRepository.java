package hu.martin.chatservice.application.port;

import hu.martin.chatservice.domain.Conversation;
import hu.martin.chatservice.domain.ConversationId;
import java.util.Optional;

public interface ConversationRepository {

  Optional<Conversation> findById(ConversationId id);

  Conversation save(Conversation conversation);
}
