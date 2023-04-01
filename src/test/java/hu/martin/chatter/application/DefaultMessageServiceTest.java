package hu.martin.chatter.application;

import hu.martin.chatter.application.paging.PageProperties;
import hu.martin.chatter.domain.message.Message;
import hu.martin.chatter.domain.message.MessageContent;
import hu.martin.chatter.domain.message.MessageFactory;
import hu.martin.chatter.domain.message.MessageId;
import hu.martin.chatter.domain.participant.ParticipantId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    void findMessagesByIdPagedReturnsPagesWithData() {
        MessageService messageService = MessageServiceFactory.withDefaults();
        List<MessageId> savedMessageIds = new ArrayList<>();
        for (BigInteger i = BigInteger.ONE; i.compareTo(BigInteger.TEN) <= 0; i = i.add(BigInteger.ONE)) {
            Message messageToSave = new Message(ParticipantId.of(i), MessageContent.of(i.toString()));
            messageToSave.changeIdTo(MessageId.of(i));
            MessageId savedMessageId = messageService
                    .receiveMessage(messageToSave).block().id();
            savedMessageIds.add(savedMessageId);
        }

        PageProperties pageProperties = new PageProperties(0, 3);
        Flux<Message> messagesFlux = messageService.findAllByIdOrderedByCreatedDateTime(savedMessageIds, pageProperties);

        //Ids should be 8,9,10
        StepVerifier.create(messagesFlux)
                .expectNextMatches(message -> message.id().id().equals(BigInteger.valueOf(10)))
                .expectNextMatches(message -> message.id().id().equals(BigInteger.valueOf(9)))
                .expectNextMatches(message -> message.id().id().equals(BigInteger.valueOf(8)))
                .expectComplete().verify();
    }
}