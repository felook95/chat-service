package hu.martin.chatter.application.port;

import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import java.util.Optional;

public interface ConversationRepository {

  Optional<Conversation> findById(ConversationId id);

  Conversation save(Conversation conversation);
}
