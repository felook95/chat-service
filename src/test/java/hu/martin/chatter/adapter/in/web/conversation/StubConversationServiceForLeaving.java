package hu.martin.chatter.adapter.in.web.conversation;

import hu.martin.chatter.application.ConversationService;
import hu.martin.chatter.domain.conversation.Conversation;
import hu.martin.chatter.domain.conversation.ConversationId;
import hu.martin.chatter.domain.message.Message;
import hu.martin.chatter.domain.message.MessageId;
import hu.martin.chatter.domain.participant.ParticipantId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class StubConversationServiceForLeaving implements ConversationService {

    private final Conversation conversation = new Conversation();

    public StubConversationServiceForLeaving(ParticipantId initialParticipant) {
        conversation.joinedBy(initialParticipant);
    }

    @Override

    public Mono<Conversation> startConversation() {
        return null;
    }

    @Override
    public Mono<Conversation> findConversationById(ConversationId id) {
        return Mono.just(conversation);
    }

    @Override
    public Mono<Message> findMessageById(MessageId id) {
        return null;
    }

    @Override
    public Mono<Conversation> joinParticipantTo(ConversationId conversationId,
                                                ParticipantId participantId) {

        return null;
    }

    @Override
    public Mono<Message> receiveMessage(Message message) {
        return null;
    }

    @Override
    public Mono<Void> sendMessageTo(MessageId messageId, ConversationId conversationId) {
        return null;
    }

    @Override
    public Mono<Message> receiveAndSendMessageTo(ConversationId conversationId, Message message) {
        return null;
    }

    @Override
    public Mono<Void> deleteMessage(MessageId messageId) {
        return null;
    }

    @Override
    public Flux<Message> messagesFrom(ConversationId conversationId) {
        return null;
    }

    @Override
    public Flux<Message> messagesByChronologicalOrderFrom(ConversationId conversationId) {
        return null;
    }

    @Override
    public Mono<Void> removeFromConversation(ConversationId conversationId,
                                             ParticipantId participantId) {
        conversation.leftBy(participantId);
        return null;
    }
}
