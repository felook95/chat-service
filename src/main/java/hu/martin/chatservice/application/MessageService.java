package hu.martin.chatservice.application;

import hu.martin.chatservice.domain.Message;
import hu.martin.chatservice.domain.MessageContent;
import hu.martin.chatservice.domain.MessageId;

public interface MessageService {

  Message receiveMessage(Message message);

  Message editMessageContent(MessageId id, MessageContent newContent);
}
