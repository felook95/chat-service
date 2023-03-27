package hu.martin.chatter.application;

import hu.martin.chatter.adapter.out.inmemory.InMemoryConversationRepository;
import hu.martin.chatter.adapter.out.inmemory.InMemoryMessageRepository;
import hu.martin.chatter.application.port.ConversationRepository;
import hu.martin.chatter.application.port.MessageRepository;
import hu.martin.chatter.domain.conversation.Conversation;
import hu.martin.chatter.domain.conversation.ConversationFactory;
import hu.martin.chatter.domain.conversation.ConversationId;
import hu.martin.chatter.domain.message.*;
import hu.martin.chatter.domain.participant.ParticipantId;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@Tag("unitTest")
class ConversationServiceTest {

    private static Message receiveDefaultMessage(ConversationService conversationService) {
        ParticipantId senderId = ParticipantId.of(BigInteger.valueOf(1L));
        MessageContent messageContent = MessageContent.of("");
        CreatedDateTime createdDateTime = CreatedDateTime.of(LocalDateTime.now());
        return conversationService.receiveMessage(
                new Message(senderId, messageContent, createdDateTime)).block();
    }

    private static ConversationService conversationServiceWithParticipantInConversation(
            ConversationId conversationId, ParticipantId participantId) {
        ConversationRepository conversationRepository = new InMemoryConversationRepository();
        Conversation conversation = ConversationFactory.withParticipants(participantId);
        conversation.setId(conversationId);
        conversationRepository.save(conversation);
        return ConversationServiceFactory.withConversationRepository(conversationRepository);
    }

    @Test
    void createConversation() {
        ConversationService conversationService = ConversationServiceFactory.withDefaults();

        Conversation conversation = conversationService.startConversation().block();

        assertThat(conversation).isNotNull();
    }

    @Test
    void createConversationReturnsConversationWithId() {
        ConversationService conversationService = ConversationServiceFactory.withDefaults();

        Conversation conversation = conversationService.startConversation().block();

        assertThat(conversation.getId()).isNotNull();
    }

    @Test
    void multipleCreateConversationCallReturnsConversationsWithDifferentId() {
        ConversationService conversationService = ConversationServiceFactory.withDefaults();

        Conversation conversation1 = conversationService.startConversation().block();
        Conversation conversation2 = conversationService.startConversation().block();

        assertThat(conversation1.getId()).isNotEqualTo(conversation2.getId());
    }

    @Test
    void conversationServiceFindsCreatedConversation() {
        ConversationService conversationService = ConversationServiceFactory.withDefaults();
        Conversation createdConversation = conversationService.startConversation().block();

        Conversation foundConversation = conversationService.findConversationById(
                createdConversation.getId()).block();

        assertThat(createdConversation).isEqualTo(foundConversation);
    }

    @Test
    void notFoundConversationThrowsConversationNotFoundException() {
        ConversationService conversationService = ConversationServiceFactory.withDefaults();
        ConversationId conversationId = ConversationId.of(BigInteger.valueOf(1L));

        Mono<Conversation> conversationByIdMono = conversationService.findConversationById(conversationId);
        assertThatThrownBy(conversationByIdMono::block).isInstanceOf(
                ConversationNotFoundException.class);
    }

    @Test
    void joinParticipantAddsParticipantToConversation() {
        ConversationService conversationService = ConversationServiceFactory.withDefaults();
        Conversation conversation = conversationService.startConversation().block();
        ParticipantId participantId = ParticipantId.of(BigInteger.valueOf(1L));

        conversationService.joinParticipantTo(conversation.getId(), participantId).block();

        Conversation foundConversation = conversationService.findConversationById(conversation.getId())
                .block();
        assertThat(foundConversation.participants()).containsOnly(participantId);
    }

    @Test
    void receivedMessageWithContentStoresMessage() {
        ConversationService conversationService = ConversationServiceFactory.withDefaults();

        Message message = receiveDefaultMessage(conversationService);

        Message foundMessage = conversationService.findMessageById(message.id()).block();
        assertThat(message).isEqualTo(foundMessage);
    }

    @Test
    void receiveMessageStoresMessageContent() {
        ConversationService conversationService = ConversationServiceFactory.withDefaults();
        ParticipantId senderId = ParticipantId.of(BigInteger.valueOf(1L));
        MessageContent messageContent = MessageContent.of("Test message");

        MessageId storedMessageId = conversationService.receiveMessage(
                new Message(senderId, messageContent)).block().id();

        Message foundMessage = conversationService.findMessageById(storedMessageId).block();

        assertThat(foundMessage.content()).isEqualTo(messageContent);
    }

    @Test
    void notFoundMessageThrowsMessageNotFoundException() {
        ConversationService conversationService = ConversationServiceFactory.withDefaults();
        MessageId messageId = MessageId.of(BigInteger.valueOf(1L));

        Mono<Message> messageByIdMono = conversationService.findMessageById(messageId);
        assertThatThrownBy(messageByIdMono::block).isInstanceOf(
                MessageNotFoundException.class);
    }

    @Test
    void sentMessageAddingToConversation() {
        ConversationService conversationService = ConversationServiceFactory.withDefaults();
        Conversation conversation = conversationService.startConversation().block();
        Message message = receiveDefaultMessage(conversationService);
        ParticipantId senderId = ParticipantId.of(BigInteger.valueOf(1L));
        conversation.joinedBy(senderId);

        conversationService.sendMessageTo(message.id(), conversation.getId()).block();

        Conversation foundConversation = conversationService.findConversationById(conversation.getId())
                .block();
        assertThat(foundConversation.messages()).containsOnly(message.id());
    }

    @Test
    void verifyReceiveAndSendMessageCallsReceiveMessageAndSendMessageTo() {
        ConversationService conversationService = ConversationServiceFactory.withDefaults();
        ConversationId conversationId = conversationService.startConversation().block().getId();
        ParticipantId participantId = ParticipantId.of(BigInteger.valueOf(1L));
        conversationService.joinParticipantTo(conversationId, participantId).block();
        conversationService = spy(conversationService);
        Message message = MessageFactory.defaultWithSender(participantId);

        conversationService.receiveAndSendMessageTo(conversationId, message).block();

        verify(conversationService).receiveMessage(any());
        verify(conversationService).sendMessageTo(any(), any());
    }

    @Test
    void receiveAndSendMessageReturnsSavedMessage() {
        ConversationService conversationService = ConversationServiceFactory.withDefaults();
        ConversationId conversationId = conversationService.startConversation().block().getId();
        ParticipantId participantId = ParticipantId.of(BigInteger.valueOf(1L));
        conversationService.joinParticipantTo(conversationId, participantId).block();
        Message message = MessageFactory.defaultWithSender(participantId);
        message.setId(null);

        Message savedMessage = conversationService.receiveAndSendMessageTo(conversationId, message)
                .block();

        assertThat(savedMessage).isNotNull();
        assertThat(savedMessage.id()).isNotNull();
    }

    @Test
    void receiveAndSendMessageReturnsSavedMessageWithCreatedDateTime() {
        ConversationService conversationService = ConversationServiceFactory.withDefaults();
        ConversationId conversationId = conversationService.startConversation().block().getId();
        ParticipantId participantId = ParticipantId.of(BigInteger.valueOf(1L));
        conversationService.joinParticipantTo(conversationId, participantId).block();
        Message message = MessageFactory.defaultWithCreatedDateTimeOf(null);
        message.setId(null);

        Message savedMessage = conversationService.receiveAndSendMessageTo(conversationId, message)
                .block();

        assertThat(savedMessage.createdDateTime()).isNotNull();
    }

    @Test
    void messageSentByNonParticipantThrowsException() {
        ConversationService conversationService = ConversationServiceFactory.withDefaults();
        Conversation conversation = conversationService.startConversation().block();
        Message message = receiveDefaultMessage(conversationService);
        MessageId messageId = message.id();
        ConversationId conversationId = conversation.getId();

        Mono<Conversation> sendMessageToMono = conversationService.sendMessageTo(messageId, conversationId);
        assertThatThrownBy(
                sendMessageToMono::block).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void deletedMessageAppearsInDeletedMessages() {
        ConversationService conversationService = ConversationServiceFactory.withDefaults();
        MessageId messageId = receiveDefaultMessage(conversationService).id();

        conversationService.deleteMessage(messageId).block();

        Message foundMessage = conversationService.findMessageById(messageId).block();
        assertThat(foundMessage.statusFlag()).isEqualTo(MessageStatus.DELETED);
    }

    @Test
    void messagesForConversationReturnsAllMessages() {
        ConversationRepository conversationRepository = new InMemoryConversationRepository();
        Conversation conversation = ConversationFactory.withDefaults();
        conversation.messageSent(MessageId.of(BigInteger.valueOf(1L)));
        conversation.messageSent(MessageId.of(BigInteger.valueOf(2L)));
        conversationRepository.save(conversation);
        MessageRepository messageRepository = new InMemoryMessageRepository();
        messageRepository.save(MessageFactory.defaultWIthIdOf(MessageId.of(BigInteger.valueOf(1L))));
        messageRepository.save(MessageFactory.defaultWIthIdOf(MessageId.of(BigInteger.valueOf(2L))));
        ConversationService conversationService = ConversationServiceFactory.with(
                conversationRepository, messageRepository);

        Collection<Message> messagesInConversation = conversationService.messagesFrom(
                conversation.getId()).collectList().block();

        assertThat(messagesInConversation).hasSize(2);
    }

    @Test
    void conversationReturnsMessagesInChronologicalOrder() {
        ConversationService conversationService = getConversationServiceWithTimeProviderInMixedOrder();
        ConversationId conversationId = conversationService.startConversation().block().getId();
        saveNCountRandomMessageToConversation(conversationService, conversationId, 3);

        Collection<Message> orderedMessages = conversationService
                .messagesByChronologicalOrderFrom(conversationId)
                .collectList().block();

        assertThat(orderedMessages)
                .extracting(Message::createdDateTime)
                .extracting(CreatedDateTime::createdDateTime)
                .isSorted();
    }

    @NotNull
    private static ConversationService getConversationServiceWithTimeProviderInMixedOrder() {
        LocalDateTime mostRecentDateTime = LocalDateTime.now().minusDays(0);
        LocalDateTime middleDateTime = LocalDateTime.now().minusDays(1);
        LocalDateTime oldestDateTime = LocalDateTime.now().minusDays(2);

        List<LocalDateTime> dateTimesToReturn = List.of(middleDateTime, mostRecentDateTime, oldestDateTime);
        OrderedTimeProvider orderedTimeProvider = new OrderedTimeProvider(dateTimesToReturn);
        return ConversationServiceFactory.withTimeProvider(orderedTimeProvider);
    }

    private void saveNCountRandomMessageToConversation(ConversationService conversationService,
                                                       ConversationId conversationId, int count) {
        for (int i = 0; i < count; i++) {
            saveRandomMessageToConversation(conversationService, conversationId);
        }
    }

    private void saveRandomMessageToConversation(ConversationService conversationService, ConversationId conversationId) {
        ParticipantId senderId = ParticipantId.of(BigInteger.valueOf(1L));
        MessageContent messageContent = MessageContent.of("");
        conversationService.joinParticipantTo(conversationId, senderId).block();
        MessageId savedMessageId = conversationService.receiveMessage(
                new Message(senderId, messageContent)).block().id();
        conversationService.sendMessageTo(savedMessageId, conversationId).block();
    }

    @Test
    void leavingConversationRemovesParticipantFromConversation() {
        ConversationId conversationId = ConversationId.of(BigInteger.valueOf(1L));
        ParticipantId participantId = ParticipantId.of(BigInteger.valueOf(1L));
        ConversationService conversationService = conversationServiceWithParticipantInConversation(
                conversationId, participantId);

        conversationService.removeFromConversation(conversationId, participantId).block();

        Conversation conversation = conversationService.findConversationById(conversationId).block();
        assertThat(conversation.hasParticipant(participantId)).isFalse();

    }
}
