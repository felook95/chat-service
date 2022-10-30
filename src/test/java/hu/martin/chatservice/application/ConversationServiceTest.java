package hu.martin.chatservice.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import hu.martin.chatservice.domain.Conversation;
import hu.martin.chatservice.domain.ConversationId;
import hu.martin.chatservice.domain.CreatedDateTime;
import hu.martin.chatservice.domain.Message;
import hu.martin.chatservice.domain.MessageContent;
import hu.martin.chatservice.domain.MessageId;
import hu.martin.chatservice.domain.MessageStatus;
import hu.martin.chatservice.domain.ParticipantId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Set;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
public class ConversationServiceTest {

  @Test
  void createConversation() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();

    Conversation conversation = conversationService.startConversation();

    assertThat(conversation).isNotNull();
  }

  @Test
  void createConversationReturnsConversationWithId() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();

    Conversation conversation = conversationService.startConversation();

    assertThat(conversation.id()).isNotNull();
  }

  @Test
  void multipleCreateConversationCallReturnsConversationsWithDifferentId() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();

    Conversation conversation1 = conversationService.startConversation();
    Conversation conversation2 = conversationService.startConversation();

    assertThat(conversation1.id()).isNotEqualTo(conversation2.id());
  }

  @Test
  void conversationServiceFindsCreatedConversation() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    Conversation createdConversation = conversationService.startConversation();

    Conversation foundConversation = conversationService.findConversationById(createdConversation.id());

    assertThat(createdConversation).isEqualTo(foundConversation);
  }

  @Test
  void notFoundConversationThrowsConversationNotFoundException() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();

    assertThatThrownBy(() -> conversationService.findConversationById(ConversationId.of(1L)))
        .isInstanceOf(ConversationNotFoundException.class);
  }

  @Test
  void joinParticipantAddsParticipantToConversation() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    Conversation conversation = conversationService.startConversation();
    ParticipantId participantId = ParticipantId.of(1L);

    conversationService.joinParticipantTo(conversation.id(), participantId);

    Conversation foundConversation = conversationService.findConversationById(conversation.id());
    assertThat(foundConversation.participants()).containsOnly(participantId);
  }

  @Test
  void createMessageWithContentStoresMessage() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();

    Message message = conversationService.receiveMessage(
        MessageContent.of(""), CreatedDateTime.of(ZonedDateTime.now().plusDays(0))
    );

    Message foundMessage = conversationService.findMessageById(message.id());
    assertThat(message).isEqualTo(foundMessage);
  }

  @Test
  void receiveMessageStoresMessage() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    MessageContent messageContent = MessageContent.of("Test message");
    CreatedDateTime createdDateTime = CreatedDateTime.of(ZonedDateTime.now().plusNanos(123456));

    MessageId storedMessageId = conversationService.receiveMessage(messageContent, createdDateTime).id();

    Message foundMessage = conversationService.findMessageById(storedMessageId);

    assertThat(foundMessage.createdDateTime()).isEqualTo(createdDateTime);
    assertThat(foundMessage.content()).isEqualTo(messageContent);
  }

  @Test
  void notFoundMessageThrowsMessageNotFoundException() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();

    assertThatThrownBy(() -> conversationService.findMessageById(MessageId.of(1L)))
        .isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  void sentMessageAddingToConversation() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    Conversation conversation = conversationService.startConversation();
    Message message = conversationService.receiveMessage(
        MessageContent.of(""), CreatedDateTime.of(ZonedDateTime.now().plusDays(0)));

    conversationService.sendMessageTo(message.id(), conversation.id());

    Conversation foundConversation = conversationService.findConversationById(conversation.id());
    assertThat(foundConversation.messages()).containsOnly(message.id());
  }

  @Test
  void deletedMessageAppearsInDeletedMessages() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    MessageId messageId = conversationService.receiveMessage(
        MessageContent.of(""),
        CreatedDateTime.of(ZonedDateTime.now())
    ).id();

    conversationService.deleteMessage(messageId);

    Message foundMessage = conversationService.findMessageById(messageId);
    assertThat(foundMessage.statusFlag()).isEqualTo(MessageStatus.DELETED);
  }

  @Test
  void messagesForConversationReturnsAllMessages() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    ConversationId conversationId = conversationService.startConversation().id();

    MessageId messageId = conversationService.receiveMessage(
        MessageContent.of("1"),
        CreatedDateTime.of(ZonedDateTime.now())
    ).id();
    conversationService.sendMessageTo(messageId, conversationId);
    messageId = conversationService.receiveMessage(
        MessageContent.of("2"),
        CreatedDateTime.of(ZonedDateTime.now())
    ).id();
    conversationService.sendMessageTo(messageId, conversationId);

    Set<Message> messagesInConversation = conversationService.messagesFrom(conversationId);
    assertThat(messagesInConversation).hasSize(2);
  }

  @Test
  void conversationReturnsMessagesInChronologicalOrder() {
    ConversationService conversationService = ConversationServiceFactory.withDefaults();
    ConversationId conversationId = conversationService.startConversation().id();
    CreatedDateTime oldestDateTime = CreatedDateTime.of(ZonedDateTime.now().plusDays(3));
    CreatedDateTime mostRecentDateTime = CreatedDateTime.of(ZonedDateTime.now().plusDays(0));
    CreatedDateTime middleDateTime = CreatedDateTime.of(ZonedDateTime.now().plusDays(2));
    saveRandomMessageToConversationWithCreatedDateTime(conversationService, oldestDateTime, conversationId);
    saveRandomMessageToConversationWithCreatedDateTime(conversationService, mostRecentDateTime,
        conversationId);
    saveRandomMessageToConversationWithCreatedDateTime(conversationService, middleDateTime, conversationId);

    Collection<Message> orderedMessages = conversationService.messagesByChronologicalOrderFrom(
        conversationId);

    assertThat(orderedMessages)
        .extracting(Message::createdDateTime)
        .containsExactly(mostRecentDateTime, middleDateTime, oldestDateTime);
  }

  private void saveRandomMessageToConversationWithCreatedDateTime(
      ConversationService conversationService, CreatedDateTime createdDateTime, ConversationId conversationId) {
    MessageId savedMessageId = conversationService.receiveMessage(MessageContent.of(""), createdDateTime)
        .id();
    conversationService.sendMessageTo(savedMessageId, conversationId);
  }
}
