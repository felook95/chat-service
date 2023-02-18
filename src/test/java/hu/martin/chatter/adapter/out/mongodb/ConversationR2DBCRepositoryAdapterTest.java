package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationFactory;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.ParticipantId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@SpringBootTest
class ConversationR2DBCRepositoryAdapterTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    ConversationR2DBCRepositoryAdapter conversationR2DBCRepositoryAdapter;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void savedConversationCanBeFoundByItsId() {
        Conversation conversationToSave = ConversationFactory.withParticipants(ParticipantId.of(1L),
                ParticipantId.of(2L));

        ConversationId conversationId = conversationR2DBCRepositoryAdapter.save(conversationToSave)
                .block().getId();

        Conversation savedConversation = conversationR2DBCRepositoryAdapter.findById(conversationId)
                .block();
        assertThat(savedConversation.getId()).isEqualTo(conversationId);
        assertThat(savedConversation.participants()).containsExactly(ParticipantId.of(1L));
    }

}