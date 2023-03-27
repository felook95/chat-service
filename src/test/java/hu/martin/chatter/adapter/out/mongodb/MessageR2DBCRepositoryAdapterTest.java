package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.domain.message.Message;
import hu.martin.chatter.domain.message.MessageFactory;
import hu.martin.chatter.domain.message.MessageId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        Set<MessageId> savedMessageIds = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            Message messageToSave = MessageFactory.defaultWIthIdOf(null);
            MessageId savedMessageId = messageR2DBCRepositoryAdapter.save(messageToSave).block().id();
            savedMessageIds.add(savedMessageId);
        }

        List<Message> retrievedMessages = messageR2DBCRepositoryAdapter.findByIds(savedMessageIds).collectList().block();

        assertThat(retrievedMessages.size()).isEqualTo(savedMessageIds.size());
    }
}