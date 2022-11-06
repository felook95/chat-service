package hu.martin.chatter.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import hu.martin.chatter.application.port.ConversationRepository;
import hu.martin.chatter.application.port.InMemoryConversationRepository;
import hu.martin.chatter.application.port.InMemoryMessageRepository;
import hu.martin.chatter.application.port.MessageRepository;
import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationFactory;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.CreatedDateTime;
import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageContent;
import hu.martin.chatter.domain.MessageFactory;
import hu.martin.chatter.domain.MessageId;
import hu.martin.chatter.domain.MessageStatus;
import hu.martin.chatter.domain.ParticipantId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Set;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

@Tag("unitTest")
class ConversationServiceTest {

  @Test
  void createConversation() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();

    Mono<Conversation> conversationMono = conversationService.startConversation();

    StepVerifier.create(conversationMono)
        .consumeNextWith(conversation -> assertThat(conversation).isNotNull());

  }

  @Test
  void createConversationReturnsConversationWithId() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();

    Mono<Conversation> conversationMono = conversationService.startConversation();

    StepVerifier.create(conversationMono)
        .consumeNextWith(conversation -> assertThat(conversation.getId()).isNotNull());
  }

  @Test
  void multipleCreateConversationCallReturnsConversationsWithDifferentId() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();

    Mono<Conversation> conversationMono1 = conversationService.startConversation();
    Mono<Conversation> conversationMono2 = conversationService.startConversation();
    Mono<Tuple2<Conversation, Conversation>> zip = Mono.zip(conversationMono1, conversationMono2);

    StepVerifier.create(zip).consumeNextWith(objects -> {
      Conversation conversation1 = objects.getT1();
      Conversation conversation2 = objects.getT2();
      assertThat(conversation1.getId()).isNotEqualTo(conversation2.getId());
    });
  }

  @Test
  void conversationServiceFindsCreatedConversation() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    Mono<Conversation> createdConversationMono = conversationService.startConversation();

    Mono<Tuple2<Conversation, Conversation>> tuple2Mono = createdConversationMono.flatMap(
        createdConversation -> conversationService.findConversationById(createdConversation.getId())
            .zipWith(Mono.just(createdConversation)));

    StepVerifier.create(tuple2Mono).consumeNextWith(objects -> {
      Conversation foundConversation = objects.getT1();
      Conversation createdConversation = objects.getT2();
      assertThat(createdConversation).isEqualTo(foundConversation);
    });
  }

  @Test
  void notFoundConversationThrowsConversationNotFoundException() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    ConversationId conversationId = ConversationId.of(1L);

    assertThatThrownBy(() -> conversationService.findConversationById(conversationId)).isInstanceOf(
        ConversationNotFoundException.class);
  }

  @Test
  void joinParticipantAddsParticipantToConversation() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    Conversation conversation = conversationService.startConversation();
    ParticipantId participantId = ParticipantId.of(1L);

    conversationService.joinParticipantTo(conversation.getId(), participantId);

    Conversation foundConversation = conversationService.findConversationById(conversation.getId());
    assertThat(foundConversation.participants()).containsOnly(participantId);
  }

  @Test
  void receivedMessageWithContentStoresMessage() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();

    Message message = receiveDefaultMessage(conversationService);

    Message foundMessage = conversationService.findMessageById(message.id());
    assertThat(message).isEqualTo(foundMessage);
  }

  @Test
  void receiveMessageStoresMessageContent() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    ParticipantId senderId = ParticipantId.of(1L);
    MessageContent messageContent = MessageContent.of("Test message");
    CreatedDateTime createdDateTime = CreatedDateTime.of(ZonedDateTime.now().plusNanos(123456));

    MessageId storedMessageId = conversationService.receiveMessage(
        new Message(senderId, messageContent, createdDateTime)).id();

    Message foundMessage = conversationService.findMessageById(storedMessageId);

    assertThat(foundMessage.createdDateTime()).isEqualTo(createdDateTime);
    assertThat(foundMessage.content()).isEqualTo(messageContent);
  }

  @Test
  void notFoundMessageThrowsMessageNotFoundException() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    MessageId messageId = MessageId.of(1L);

    assertThatThrownBy(() -> conversationService.findMessageById(messageId)).isInstanceOf(
        MessageNotFoundException.class);
  }

  @Test
  void sentMessageAddingToConversation() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    Conversation conversation = conversationService.startConversation();
    Message message = receiveDefaultMessage(conversationService);
    ParticipantId senderId = ParticipantId.of(1L);
    conversation.joinedBy(senderId);

    conversationService.sendMessageTo(message.id(), conversation.getId());

    Conversation foundConversation = conversationService.findConversationById(conversation.getId());
    assertThat(foundConversation.messages()).containsOnly(message.id());
  }

  @Test
  void verifyReceiveAndSendMessageCallsReceiveMessageAndSendMessageTo() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    ConversationId conversationId = conversationService.startConversation().getId();
    ParticipantId participantId = ParticipantId.of(1L);
    conversationService.joinParticipantTo(conversationId, participantId);
    conversationService = spy(conversationService);
    Message message = MessageFactory.defaultWithSender(participantId);

    conversationService.receiveAndSendMessageTo(conversationId, message);

    verify(conversationService).receiveMessage(any());
    verify(conversationService).sendMessageTo(any(), any());
  }

  @Test
  void receiveAndSendMessageReturnsSavedMessage() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    ConversationId conversationId = conversationService.startConversation().getId();
    ParticipantId participantId = ParticipantId.of(1L);
    conversationService.joinParticipantTo(conversationId, participantId);
    Message message = MessageFactory.defaultWithSender(participantId);
    message.setId(null);

    Message savedMessage = conversationService.receiveAndSendMessageTo(conversationId, message);

    assertThat(savedMessage).isNotNull();
    assertThat(savedMessage.id()).isNotNull();
  }

  private static Message receiveDefaultMessage(ConversationService conversationService) {
    ParticipantId senderId = ParticipantId.of(1L);
    MessageContent messageContent = MessageContent.of("");
    CreatedDateTime createdDateTime = CreatedDateTime.of(ZonedDateTime.now());
    return conversationService.receiveMessage(
        new Message(senderId, messageContent, createdDateTime));
  }

  @Test
  void messageSentByNonParticipantThrowsException() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    Conversation conversation = conversationService.startConversation();
    Message message = receiveDefaultMessage(conversationService);
    MessageId messageId = message.id();
    ConversationId conversationId = conversation.getId();

    assertThatThrownBy(
        () -> conversationService.sendMessageTo(messageId, conversationId)).isInstanceOf(
        IllegalArgumentException.class);
  }

  @Test
  void deletedMessageAppearsInDeletedMessages() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    MessageId messageId = receiveDefaultMessage(conversationService).id();

    conversationService.deleteMessage(messageId);

    Message foundMessage = conversationService.findMessageById(messageId);
    assertThat(foundMessage.statusFlag()).isEqualTo(MessageStatus.DELETED);
  }

  @Test
  void messagesForConversationReturnsAllMessages() {
    ConversationRepository conversationRepository = new InMemoryConversationRepository();
    Conversation conversation = ConversationFactory.withDefaults();
    conversation.messageSent(MessageId.of(1L));
    conversation.messageSent(MessageId.of(2L));
    conversationRepository.save(conversation);
    MessageRepository messageRepository = new InMemoryMessageRepository();
    messageRepository.save(MessageFactory.defaultWIthIdOf(MessageId.of(1L)));
    messageRepository.save(MessageFactory.defaultWIthIdOf(MessageId.of(2L)));
    ConversationService conversationService = ConversationServiceFactory.with(
        conversationRepository, messageRepository);

    Set<Message> messagesInConversation = conversationService.messagesFrom(conversation.getId());

    assertThat(messagesInConversation).hasSize(2);
  }

  @Test
  void conversationReturnsMessagesInChronologicalOrder() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    ConversationId conversationId = conversationService.startConversation().getId();
    CreatedDateTime oldestDateTime = CreatedDateTime.of(ZonedDateTime.now().plusDays(3));
    CreatedDateTime mostRecentDateTime = CreatedDateTime.of(ZonedDateTime.now().plusDays(0));
    CreatedDateTime middleDateTime = CreatedDateTime.of(ZonedDateTime.now().plusDays(2));
    saveRandomMessageToConversationWithCreatedDateTime(conversationService, oldestDateTime,
        conversationId);
    saveRandomMessageToConversationWithCreatedDateTime(conversationService, mostRecentDateTime,
        conversationId);
    saveRandomMessageToConversationWithCreatedDateTime(conversationService, middleDateTime,
        conversationId);

    Collection<Message> orderedMessages = conversationService.messagesByChronologicalOrderFrom(
        conversationId);

    assertThat(orderedMessages).extracting(Message::createdDateTime)
        .containsExactly(mostRecentDateTime, middleDateTime, oldestDateTime);
  }

  private void saveRandomMessageToConversationWithCreatedDateTime(
      ConversationService conversationService, CreatedDateTime createdDateTime,
      ConversationId conversationId) {
    ParticipantId senderId = ParticipantId.of(1L);
    MessageContent messageContent = MessageContent.of("");
    conversationService.joinParticipantTo(conversationId, senderId);
    MessageId savedMessageId = conversationService.receiveMessage(
        new Message(senderId, messageContent, createdDateTime)).id();
    conversationService.sendMessageTo(savedMessageId, conversationId);
  }

  @Test
  void leavingConversationRemovesParticipantFromConversation() {
    ConversationId conversationId = ConversationId.of(1L);
    ParticipantId participantId = ParticipantId.of(1L);
    ConversationService conversationService = conversationServiceWithParticipantInConversation(
        conversationId, participantId);

    conversationService.removeFromConversation(conversationId, participantId);

    Conversation conversation = conversationService.findConversationById(conversationId);
    assertThat(conversation.hasParticipant(participantId)).isFalse();

  }

  private static ConversationService conversationServiceWithParticipantInConversation(
      ConversationId conversationId, ParticipantId participantId) {
    ConversationRepository conversationRepository = new InMemoryConversationRepository();
    Conversation conversation = ConversationFactory.withParticipants(participantId);
    conversation.setId(conversationId);
    conversationRepository.save(conversation);
    return ConversationServiceFactory.withConversationRepository(conversationRepository);
  }
}
