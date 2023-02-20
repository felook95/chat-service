package hu.martin.chatter.application;

import hu.martin.chatter.application.port.ConversationRepository;
import hu.martin.chatter.application.port.MessageRepository;
import hu.martin.chatter.domain.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Set;

public class DefaultConversationService implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public DefaultConversationService(ConversationRepository conversationRepository,
                                      MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    private static void assertConversationHasParticipant(Conversation conversation,
                                                         ParticipantId senderId) {
        if (conversation.hasParticipant(senderId)) {
            return;
        }
        throw new IllegalArgumentException("Sender is not a participant of the conversation");
    }

    @Override
    public Mono<Conversation> startConversation() {
        Conversation conversation = new Conversation();
        return saveConversation(conversation);
    }

    @Override
    public Mono<Conversation> findConversationById(ConversationId id) {
        return conversationRepository.findById(id)
                .switchIfEmpty(Mono.error(ConversationNotFoundException::new));
    }

    @Override
    public Mono<Message> findMessageById(MessageId id) {
        return messageRepository.findById(id).switchIfEmpty(Mono.error(MessageNotFoundException::new));
    }

    @Override
    public Mono<Conversation> joinParticipantTo(ConversationId conversationId,
                                                ParticipantId participantId) {
        return findConversationById(conversationId).map(conversation -> {
            conversation.joinedBy(participantId);
            return conversation;
        }).flatMap(this::saveConversation);
    }

    @Override
    public Mono<Void> sendMessageTo(MessageId messageId, ConversationId conversationId) {
        return Mono.zip(findConversationById(conversationId), findMessageById(messageId))
                .map(objects -> {
                    Conversation conversation = objects.getT1();
                    Message message = objects.getT2();
                    ParticipantId senderId = message.sender();
                    assertConversationHasParticipant(conversation, senderId);
                    conversation.messageSent(messageId);
                    return conversation;
                }).map(this::saveConversation).then();
    }

    @Override
    public Mono<Message> receiveAndSendMessageTo(ConversationId conversationId, Message message) {
        return receiveMessage(message)
                .flatMap(receivedMessage -> sendMessageTo(message.id(), conversationId).thenReturn(
                        receivedMessage));
    }

    @Override
    public Mono<Void> deleteMessage(MessageId messageId) {
        return findMessageById(messageId).map(message -> {
            message.delete();
            return messageRepository.save(message);
        }).then();
    }

    @Override
    public Mono<Message> receiveMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Flux<Message> messagesByChronologicalOrderFrom(ConversationId conversationId) {
        return messagesFrom(conversationId).collectSortedList(
                        Comparator.comparing(Message::createdDateTime))
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Void> removeFromConversation(ConversationId conversationId,
                                             ParticipantId participantId) {
        return findConversationById(conversationId).map(conversation -> {
            conversation.leftBy(participantId);
            return conversation;
        }).map(this::saveConversation).then();
    }

    private Mono<Conversation> saveConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    @Override
    public Flux<Message> messagesFrom(ConversationId conversationId) {
        return findConversationById(conversationId).map(Conversation::messages)
                .flatMapMany(this::messagesByIds);
    }

    private Flux<Message> messagesByIds(Set<MessageId> messageIdsInConversation) {
        return messageRepository.findByIds(messageIdsInConversation);
    }
}
