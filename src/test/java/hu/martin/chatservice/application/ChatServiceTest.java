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
public class ChatServiceTest {

  @Test
  void createConversation() {
    ChatService chatService = ChatServiceFactory.withDefaults();

    Conversation conversation = chatService.startConversation();

    assertThat(conversation).isNotNull();
  }

  @Test
  void createConversationReturnsConversationWithId() {
    ChatService chatService = ChatServiceFactory.withDefaults();

    Conversation conversation = chatService.startConversation();

    assertThat(conversation.id()).isNotNull();
  }

  @Test
  void multipleCreateConversationCallReturnsConversationsWithDifferentId() {
    ChatService chatService = ChatServiceFactory.withDefaults();

    Conversation conversation1 = chatService.startConversation();
    Conversation conversation2 = chatService.startConversation();

    assertThat(conversation1.id()).isNotEqualTo(conversation2.id());
  }

  @Test
  void conversationServiceFindsCreatedConversation() {
    ChatService chatService = ChatServiceFactory.withDefaults();
    Conversation createdConversation = chatService.startConversation();

    Conversation foundConversation = chatService.findConversationById(createdConversation.id());

    assertThat(createdConversation).isEqualTo(foundConversation);
  }

  @Test
  void notFoundConversationThrowsConversationNotFoundException() {
    ChatService chatService = ChatServiceFactory.withDefaults();

    assertThatThrownBy(() -> chatService.findConversationById(ConversationId.of(1L)))
        .isInstanceOf(ConversationNotFoundException.class);
  }

  @Test
  void joinParticipantAddsParticipantToConversation() {
    ChatService chatService = ChatServiceFactory.withDefaults();
    Conversation conversation = chatService.startConversation();
    ParticipantId participantId = ParticipantId.of(1L);

    chatService.joinParticipantTo(conversation.id(), participantId);

    Conversation foundConversation = chatService.findConversationById(conversation.id());
    assertThat(foundConversation.participants()).containsOnly(participantId);
  }

  @Test
  void createMessageWithContentStoresMessage() {
    ChatService chatService = ChatServiceFactory.withDefaults();

    Message message = chatService.receiveMessage(
        MessageContent.of(""), CreatedDateTime.of(ZonedDateTime.now().plusDays(0))
    );

    Message foundMessage = chatService.findMessageById(message.id());
    assertThat(message).isEqualTo(foundMessage);
  }

  @Test
  void receiveMessageStoresMessage() {
    ChatService chatService = ChatServiceFactory.withDefaults();
    MessageContent messageContent = MessageContent.of("Test message");
    CreatedDateTime createdDateTime = CreatedDateTime.of(ZonedDateTime.now().plusNanos(123456));

    MessageId storedMessageId = chatService.receiveMessage(messageContent, createdDateTime).id();

    Message foundMessage = chatService.findMessageById(storedMessageId);

    assertThat(foundMessage.createdDateTime()).isEqualTo(createdDateTime);
    assertThat(foundMessage.content()).isEqualTo(messageContent);
  }

  @Test
  void notFoundMessageThrowsMessageNotFoundException() {
    ChatService chatService = ChatServiceFactory.withDefaults();

    assertThatThrownBy(() -> chatService.findMessageById(MessageId.of(1L)))
        .isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  void sentMessageAddingToConversation() {
    ChatService chatService = ChatServiceFactory.withDefaults();
    Conversation conversation = chatService.startConversation();
    Message message = chatService.receiveMessage(
        MessageContent.of(""), CreatedDateTime.of(ZonedDateTime.now().plusDays(0)));

    chatService.sendMessageTo(message.id(), conversation.id());

    Conversation foundConversation = chatService.findConversationById(conversation.id());
    assertThat(foundConversation.messages()).containsOnly(message.id());
  }

  @Test
  void deletedMessageAppearsInDeletedMessages() {
    ChatService chatService = ChatServiceFactory.withDefaults();
    MessageId messageId = chatService.receiveMessage(
        MessageContent.of(""),
        CreatedDateTime.of(ZonedDateTime.now())
    ).id();

    chatService.deleteMessage(messageId);

    Message foundMessage = chatService.findMessageById(messageId);
    assertThat(foundMessage.statusFlag()).isEqualTo(MessageStatus.DELETED);
  }

  @Test
  void messagesForConversationReturnsAllMessages() {
    ChatService chatService = ChatServiceFactory.withDefaults();
    ConversationId conversationId = chatService.startConversation().id();

    MessageId messageId = chatService.receiveMessage(
        MessageContent.of("1"),
        CreatedDateTime.of(ZonedDateTime.now())
    ).id();
    chatService.sendMessageTo(messageId, conversationId);
    messageId = chatService.receiveMessage(
        MessageContent.of("2"),
        CreatedDateTime.of(ZonedDateTime.now())
    ).id();
    chatService.sendMessageTo(messageId, conversationId);

    Set<Message> messagesInConversation = chatService.messagesFrom(conversationId);
    assertThat(messagesInConversation).hasSize(2);
  }

  @Test
  void conversationReturnsMessagesInChronologicalOrder() {
    ChatService chatService = ChatServiceFactory.withDefaults();
    ConversationId conversationId = chatService.startConversation().id();
    CreatedDateTime oldestDateTime = CreatedDateTime.of(ZonedDateTime.now().plusDays(3));
    CreatedDateTime mostRecentDateTime = CreatedDateTime.of(ZonedDateTime.now().plusDays(0));
    CreatedDateTime middleDateTime = CreatedDateTime.of(ZonedDateTime.now().plusDays(2));
    saveRandomMessageToConversationWithCreatedDateTime(chatService, oldestDateTime, conversationId);
    saveRandomMessageToConversationWithCreatedDateTime(chatService, mostRecentDateTime,
        conversationId);
    saveRandomMessageToConversationWithCreatedDateTime(chatService, middleDateTime, conversationId);

    Collection<Message> orderedMessages = chatService.messagesByChronologicalOrderFrom(
        conversationId);

    assertThat(orderedMessages)
        .extracting(Message::createdDateTime)
        .containsExactly(mostRecentDateTime, middleDateTime, oldestDateTime);
  }

  private void saveRandomMessageToConversationWithCreatedDateTime(
      ChatService chatService, CreatedDateTime createdDateTime, ConversationId conversationId) {
    MessageId savedMessageId = chatService.receiveMessage(MessageContent.of(""), createdDateTime)
        .id();
    chatService.sendMessageTo(savedMessageId, conversationId);
  }
}
