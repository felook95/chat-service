package hu.martin.chatter.application;

import hu.martin.chatter.domain.conversation.Conversation;
import hu.martin.chatter.domain.conversation.ConversationId;
import hu.martin.chatter.domain.message.Message;
import hu.martin.chatter.domain.message.MessageId;
import hu.martin.chatter.domain.participant.ParticipantId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ConversationService {

    Mono<Conversation> startConversation();

    Mono<Conversation> findConversationById(ConversationId id);

    Mono<Message> findMessageById(MessageId id);

    Mono<Conversation> joinParticipantTo(ConversationId conversationId, ParticipantId participantId);

    Mono<Message> receiveMessage(Message message);

    Mono<Conversation> sendMessageTo(MessageId messageId, ConversationId conversationId);

    Mono<Message> receiveAndSendMessageTo(ConversationId conversationId, Message message);

    Mono<Void> deleteMessage(MessageId messageId);

    Flux<Message> messagesFrom(ConversationId conversationId);

    Flux<Message> messagesByChronologicalOrderFrom(ConversationId conversationId);

    Mono<Void> removeFromConversation(ConversationId conversationId, ParticipantId participantId);
}
