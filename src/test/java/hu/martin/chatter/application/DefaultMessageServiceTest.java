package hu.martin.chatter.application;

import static org.assertj.core.api.Assertions.assertThat;

import hu.martin.chatter.domain.Message;
import hu.martin.chatter.domain.MessageContent;
import hu.martin.chatter.domain.MessageFactory;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class DefaultMessageServiceTest {

  @Test
  void receiveMessageReturnsSavedMessage() {
    MessageService messageService = MessageServiceFactory.withDefaults();
    Message messageToReceive = MessageFactory.defaultWIthIdOf(null);

    Message savedMessage = messageService.receiveMessage(messageToReceive);

    assertThat(savedMessage.id()).isNotNull();
    assertThat(savedMessage.sender()).isEqualTo(messageToReceive.sender());
    assertThat(savedMessage.content()).isEqualTo(messageToReceive.content());
    assertThat(savedMessage.statusFlag()).isEqualTo(messageToReceive.statusFlag());
  }

  @Test
  void editingMessageContentReturnsSameMessageWithUpdatedContent() {
    MessageService messageService = MessageServiceFactory.withDefaults();
    Message messageToReceive = MessageFactory.defaultWIthIdOf(null);
    Message savedMessage = messageService.receiveMessage(messageToReceive);

    MessageContent newContent = MessageContent.of("Edited message");
    Message editedMessage = messageService.editMessageContent(savedMessage.id(), newContent);

    assertThat(editedMessage.id()).isEqualTo(savedMessage.id());
    assertThat(editedMessage.content()).isEqualTo(newContent);
  }
}