package hu.martin.chatservice.application;

import hu.martin.chatservice.application.port.ConversationRepository;
import hu.martin.chatservice.application.port.MessageRepository;
import hu.martin.chatservice.domain.*;

public class DefaultChatService implements ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public DefaultChatService(ConversationRepository conversationRepository, MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public Conversation createConversation() {
        Conversation conversation = new Conversation();
        return conversationRepository.save(conversation);
    }

    @Override
    public Conversation findConversationById(ConversationId id) {
        return conversationRepository.findById(id).orElseThrow(ConversationNotFoundException::new);
    }

    @Override
    public Message findMessageById(MessageId id) {
        return messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
    }

    @Override
    public void joinParticipantTo(ConversationId conversationId, ParticipantId participantId) {
        Conversation conversation = findConversationById(conversationId);
        conversation.joinedBy(participantId);
        conversationRepository.save(conversation);
    }

    @Override
    public void sendMessageTo(MessageId messageId, ConversationId conversationId) {
        Conversation conversation = findConversationById(conversationId);
        conversation.messageSent(messageId);
        conversationRepository.save(conversation);
    }

    @Override
    public void deleteMessageFrom(MessageId messageId, ConversationId conversationId) {
        Conversation conversation = findConversationById(conversationId);
        conversation.deleteMessage(messageId);
        Message foundMessage = findMessageById(messageId);
        foundMessage.deleted();
        conversationRepository.save(conversation);
    }

    @Override
    public Message createMessageWith(MessageContent messageContent) {
        return messageRepository.save(new Message(messageContent));
    }
}
