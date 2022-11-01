package hu.martin.chatter.application;

import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageContent;
import hu.martin.chatter.domain.MessageId;

public interface MessageService {

  Message receiveMessage(Message message);

  Message editMessageContent(MessageId id, MessageContent newContent);
}
