package hu.martin.chatservice.application;

import hu.martin.chatservice.application.port.MessageRepository;
import hu.martin.chatservice.domain.Message;
import hu.martin.chatservice.domain.MessageContent;
import hu.martin.chatservice.domain.MessageId;

public class DefaultMessageService implements MessageService {

  private final MessageRepository messageRepository;

  public DefaultMessageService(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  @Override
  public Message receiveMessage(Message message) {
    return messageRepository.save(message);
  }

  @Override
  public Message editMessageContent(MessageId id, MessageContent newContent) {
    Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
    message.changeContentTo(newContent);
    return messageRepository.save(message);
  }
}
