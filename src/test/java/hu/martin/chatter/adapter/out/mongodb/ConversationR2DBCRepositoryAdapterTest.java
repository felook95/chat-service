package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.domain.conversation.ConversationFactory;
import hu.martin.chatter.domain.conversation.Conversation;
import hu.martin.chatter.domain.conversation.ConversationId;
import hu.martin.chatter.domain.participant.ParticipantId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigInteger;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@DataMongoTest
@Import(ConversationR2DBCRepositoryAdapter.class)
class ConversationR2DBCRepositoryAdapterTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest").withMinimumRunningDuration(Duration.ofSeconds(20));

    @Autowired
    ConversationR2DBCRepositoryAdapter conversationR2DBCRepositoryAdapter;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void savedConversationCanBeFoundByItsId() {
        Conversation conversationToSave = ConversationFactory.withParticipants(ParticipantId.of(BigInteger.valueOf(1L)),
                ParticipantId.of(BigInteger.valueOf(2L)));

        ConversationId conversationId = conversationR2DBCRepositoryAdapter.save(conversationToSave)
                .block().getId();

        Conversation savedConversation = conversationR2DBCRepositoryAdapter.findById(conversationId).block();
        assertThat(savedConversation.getId()).isEqualTo(conversationId);
    }

}