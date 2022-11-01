package hu.martin.chatservice.application;

import hu.martin.chatservice.application.port.ConversationRepository;
import hu.martin.chatservice.application.port.MessageRepository;
import hu.martin.chatservice.domain.Conversation;
import hu.martin.chatservice.domain.ConversationId;
import hu.martin.chatservice.domain.Message;
import hu.martin.chatservice.domain.MessageId;
import hu.martin.chatservice.domain.ParticipantId;
import java.util.Collection;
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

  @Override
  public Conversation startConversation() {
    Conversation conversation = new Conversation();
    return saveConversation(conversation);
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
    saveConversation(conversation);
  }

  @Override
  public void sendMessageTo(MessageId messageId, ConversationId conversationId) {
    Conversation conversation = findConversationById(conversationId);
    Message message = findMessageById(messageId);
    ParticipantId senderId = message.sender();
    assertConversationHasParticipant(conversation, senderId);
    conversation.messageSent(messageId);
    saveConversation(conversation);
  }

  private static void assertConversationHasParticipant(Conversation conversation,
      ParticipantId senderId) {
    if (conversation.hasParticipant(senderId)) {
      return;
    }
    throw new IllegalArgumentException("Sender is not a participant of the conversation");
  }

  @Override
  public void deleteMessage(MessageId messageId) {
    Message foundMessage = findMessageById(messageId);
    foundMessage.delete();
    messageRepository.save(foundMessage);
  }

  @Override
  public Message receiveMessage(Message message) {
    return messageRepository.save(message);
  }

  @Override
  public Collection<Message> messagesByChronologicalOrderFrom(ConversationId conversationId) {
    return messagesFrom(conversationId).stream()
        .sorted(Comparator.comparing(Message::createdDateTime))
        .toList();
  }

  @Override
  public void removeFromConversation(ConversationId conversationId, ParticipantId participantId) {
    Conversation conversation = findConversationById(conversationId);
    conversation.leftBy(participantId);
    saveConversation(conversation);
  }

  private Conversation saveConversation(Conversation conversation) {
    return conversationRepository.save(conversation);
  }

  @Override
  public Set<Message> messagesFrom(ConversationId conversationId) {
    Set<MessageId> messageIdsInConversation = findConversationById(conversationId).messages();
    return messagesByIds(messageIdsInConversation);
  }

  private Set<Message> messagesByIds(Set<MessageId> messageIdsInConversation) {
    return messageRepository.findByIds(messageIdsInConversation);
  }
}
