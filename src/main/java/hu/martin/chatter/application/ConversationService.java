package hu.martin.chatter.application;

import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.ParticipantId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ConversationService {

  Mono<Conversation> startConversation();

  Mono<Conversation> findConversationById(ConversationId id);

  Mono<Message> findMessageById(MessageId id);

  Mono<Conversation> joinParticipantTo(ConversationId conversationId, ParticipantId participantId);

  Mono<Message> receiveMessage(Message message);

  Mono<Void> sendMessageTo(MessageId messageId, ConversationId conversationId);

  Mono<Message> receiveAndSendMessageTo(ConversationId conversationId, Message message);

  Mono<Void> deleteMessage(MessageId messageId);

  Flux<Message> messagesFrom(ConversationId conversationId);

  Flux<Message> messagesByChronologicalOrderFrom(ConversationId conversationId);

  Mono<Void> removeFromConversation(ConversationId conversationId, ParticipantId participantId);
}
