package hu.martin.chatter.adapter.out.cassandra;

import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationFactory;
import hu.martin.chatter.domain.ConversationId;
import hu.martin.chatter.domain.ParticipantId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;


@Import(ConversationR2DBCRepositoryAdapter.class)
@DataCassandraTest
class ConversationR2DBCRepositoryAdapterTest {

    @Autowired
    ConversationR2DBCRepositoryAdapter conversationR2DBCRepositoryAdapter;

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