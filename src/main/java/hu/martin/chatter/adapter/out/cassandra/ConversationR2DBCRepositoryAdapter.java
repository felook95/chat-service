package hu.martin.chatter.adapter.out.cassandra;

import hu.martin.chatter.application.port.ConversationRepository;
import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ConversationR2DBCRepositoryAdapter implements ConversationRepository {

  private final ConversationReactiveCrudRepository conversationRepository;

  public ConversationR2DBCRepositoryAdapter(
      ConversationReactiveCrudRepository conversationRepository) {
    this.conversationRepository = conversationRepository;
  }

  @Override
  public Mono<Conversation> findById(ConversationId id) {
    return Mono.empty();
  }

  @Override
  public Mono<Conversation> save(Conversation conversation) {
    ConversationDBO conversationDBO = ConversationDBO.from(conversation);
    return conversationRepository.save(conversationDBO)
        .map(ConversationDBO::asConversation);
  }
}
