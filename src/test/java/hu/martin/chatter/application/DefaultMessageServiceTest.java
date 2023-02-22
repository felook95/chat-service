package hu.martin.chatter.application;

import hu.martin.chatter.domain.message.MessageFactory;
import hu.martin.chatter.domain.message.Message;
import hu.martin.chatter.domain.message.MessageContent;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unitTest")
class DefaultMessageServiceTest {

    @Test
    void receiveMessageReturnsSavedMessage() {
        MessageService messageService = MessageServiceFactory.withDefaults();
        Message messageToReceive = MessageFactory.defaultWIthIdOf(null);

        Mono<Message> savedMessageMono = messageService.receiveMessage(messageToReceive);

        StepVerifier.create(savedMessageMono).consumeNextWith(savedMessage -> {
            assertThat(savedMessage.id()).isNotNull();
            assertThat(savedMessage.sender()).isEqualTo(messageToReceive.sender());
            assertThat(savedMessage.content()).isEqualTo(messageToReceive.content());
            assertThat(savedMessage.statusFlag()).isEqualTo(messageToReceive.statusFlag());
        }).expectComplete().verify();
    }

    @Test
    void editingMessageContentReturnsSameMessageWithUpdatedContent() {
        MessageService messageService = MessageServiceFactory.withDefaults();
        Message messageToReceive = MessageFactory.defaultWIthIdOf(null);
        Mono<Message> savedMessageMono = messageService.receiveMessage(messageToReceive);

        MessageContent newContent = MessageContent.of("Edited message");
        Mono<Message> editedMessageMono = savedMessageMono.flatMap(
                savedMessage -> messageService.editMessageContent(savedMessage.id(), newContent));

        StepVerifier.create(editedMessageMono).consumeNextWith(
                        editedMessage -> assertThat(editedMessage.content()).isEqualTo(newContent)).expectComplete()
                .verify();

    }
}