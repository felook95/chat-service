package hu.martin.chatservice.application;

import hu.martin.chatservice.application.port.ConversationRepository;
import hu.martin.chatservice.application.port.MessageRepository;
import hu.martin.chatservice.domain.Conversation;
import hu.martin.chatservice.domain.ConversationId;
import hu.martin.chatservice.domain.CreatedDateTime;
import hu.martin.chatservice.domain.Message;
import hu.martin.chatservice.domain.MessageContent;
import hu.martin.chatservice.domain.MessageId;
import hu.martin.chatservice.domain.ParticipantId;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

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
  public void deleteMessage(MessageId messageId) {
    Message foundMessage = findMessageById(messageId);
    foundMessage.deleted();
    messageRepository.save(foundMessage);
  }

  @Override
  public Message receiveMessage(MessageContent messageContent, CreatedDateTime createdDateTime) {
    return messageRepository.save(new Message(messageContent, createdDateTime));
  }

  @Override
  public Collection<Message> messagesByChronologicalOrderFrom(ConversationId conversationId) {
    return messagesFrom(conversationId).stream()
        .sorted(Comparator.comparing(Message::createdDateTime))
        .collect(Collectors.toList());
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
