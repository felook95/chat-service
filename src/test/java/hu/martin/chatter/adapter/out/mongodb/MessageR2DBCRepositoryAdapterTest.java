package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.domain.message.CreatedDateTime;
import hu.martin.chatter.domain.message.Message;
import hu.martin.chatter.domain.message.MessageFactory;
import hu.martin.chatter.domain.message.MessageId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataMongoTest
@Import(MessageR2DBCRepositoryAdapter.class)
class MessageR2DBCRepositoryAdapterTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest").withMinimumRunningDuration(Duration.ofSeconds(20));

    @Autowired
    MessageR2DBCRepositoryAdapter messageR2DBCRepositoryAdapter;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void savingMessageSetsAnIdForTheMessage() {
        Message messageToSave = MessageFactory.defaultWIthIdOf(null);

        MessageId savedMessageId = messageR2DBCRepositoryAdapter.save(messageToSave).block().id();

        assertThat(savedMessageId).isNotNull();
    }

    @Test
    void savedMessageCanBeRetrievedByItsId() {
        Message messageToSave = MessageFactory.defaultWIthIdOf(null);
        MessageId savedMessageId = messageR2DBCRepositoryAdapter.save(messageToSave).block().id();

        Message retrievedMessage = messageR2DBCRepositoryAdapter.findById(savedMessageId).block();

        assertThat(retrievedMessage).isNotNull();
        assertThat(messageToSave.statusFlag()).isEqualTo(retrievedMessage.statusFlag());
        assertThat(messageToSave.sender()).isEqualTo(retrievedMessage.sender());
        assertThat(messageToSave.content()).isEqualTo(retrievedMessage.content());
        assertThat(messageToSave.createdDateTime().createdDateTime().withNano(0))
                .isEqualTo(retrievedMessage.createdDateTime().createdDateTime().withNano(0));
    }

    @Test
    void findByIdsReturnsAllRequestedMessages() {
        Set<MessageId> savedMessageIds = new HashSet<>(saveMessagesThenReturnMessageIdsTimes(3));

        List<Message> retrievedMessages = messageR2DBCRepositoryAdapter.findByIds(savedMessageIds).collectList().block();

        assertThat(retrievedMessages).hasSameSizeAs(savedMessageIds);
    }

    @Test
    void findByIdPageable() {
        List<MessageId> savedMessageIds = saveMessagesThenReturnMessageIdsTimes(10);
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by("createdAt"));

        Collection<Message> pagedMessages = messageR2DBCRepositoryAdapter.findByIdsPageable(savedMessageIds, pageRequest)
                .collectList().block();

        assertThat(pagedMessages).isNotNull();
        assertThat(pagedMessages).hasSameSizeAs(pagedMessages);
        assertThat(pagedMessages)
                .extracting(Message::createdDateTime)
                .extracting(CreatedDateTime::createdDateTime)
                .isSorted();
    }

    private List<MessageId> saveMessagesThenReturnMessageIdsTimes(int times) {
        List<MessageId> savedMessageIds = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < times; i++) {
            Message messageToSave = MessageFactory.defaultWIthIdOf(null);
            messageToSave.changeCreatedDateTimeTo(CreatedDateTime.of(now));
            now = now.plusSeconds(1);
            MessageId savedMessageId = messageR2DBCRepositoryAdapter.save(messageToSave).block().id();
            savedMessageIds.add(savedMessageId);
        }
        return savedMessageIds;
    }
}