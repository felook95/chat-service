package hu.martin.chatter.application.port;

import hu.martin.chatter.domain.conversation.Conversation;
import hu.martin.chatter.domain.conversation.ConversationId;
import reactor.core.publisher.Mono;

public interface ConversationRepository {

    Mono<Conversation> findById(ConversationId id);

    Mono<Conversation> save(Conversation conversation);
}
