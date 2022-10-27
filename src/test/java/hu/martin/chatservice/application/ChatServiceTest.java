package hu.martin.chatservice.application;

import hu.martin.chatservice.domain.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unitTest")
public class ChatServiceTest {

    @Test
    void createConversation() {
        ChatService chatService = ChatServiceFactory.withDefaults();

        Conversation conversation = chatService.createConversation();

        assertThat(conversation).isNotNull();
    }

    @Test
    void createConversationReturnsConversationWithId() {
        ChatService chatService = ChatServiceFactory.withDefaults();

        Conversation conversation = chatService.createConversation();

        assertThat(conversation.id()).isNotNull();
    }

    @Test
    void multipleCreateConversationCallReturnsConversationsWithDifferentId() {
        ChatService chatService = ChatServiceFactory.withDefaults();

        Conversation conversation1 = chatService.createConversation();
        Conversation conversation2 = chatService.createConversation();

        assertThat(conversation1.id()).isNotEqualTo(conversation2.id());
    }

    @Test
    void conversationServiceFindsCreatedConversation() {
        ChatService chatService = ChatServiceFactory.withDefaults();
        Conversation createdConversation = chatService.createConversation();

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
        Conversation conversation = chatService.createConversation();
        ParticipantId participantId = ParticipantId.of(1L);

        chatService.joinParticipantTo(conversation.id(), participantId);

        Conversation foundConversation = chatService.findConversationById(conversation.id());
        assertThat(foundConversation.participants()).containsOnly(participantId);
    }

    @Test
    void createMessageWithContentStoresMessage() {
        ChatService chatService = ChatServiceFactory.withDefaults();

        Message message = chatService.createMessageWith(MessageContent.of(""));

        Message foundMessage = chatService.findMessageById(message.id());
        assertThat(message).isEqualTo(foundMessage);
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
        Conversation conversation = chatService.createConversation();
        Message message = chatService.createMessageWith(MessageContent.of(""));

        chatService.sendMessageTo(message.id(), conversation.id());

        Conversation foundConversation = chatService.findConversationById(conversation.id());
        assertThat(foundConversation.messages()).containsOnly(message.id());
    }

    @Test
    void deletedMessageAppearsInDeletedMessages() {
        ChatService chatService = ChatServiceFactory.withDefaults();
        Conversation conversation = chatService.createConversation();
        Message message = chatService.createMessageWith(MessageContent.of(""));
        chatService.sendMessageTo(message.id(), conversation.id());

        chatService.deleteMessageFrom(message.id(), conversation.id());

        Conversation foundConversation = chatService.findConversationById(conversation.id());
        assertThat(foundConversation.messages()).doesNotContain(message.id());
    }
}
