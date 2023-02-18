package hu.martin.chatter.adapter.out.cassandra;

import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Set;
import java.util.stream.Collectors;

@Table("conversations")
public class ConversationDBO {

    @PrimaryKey
    Long id;

    @Column
    @CassandraType(type = CassandraType.Name.SET, userTypeName = "joined_participant_type", typeArguments = CassandraType.Name.UDT)
    Set<JoinedParticipant> participantIds;

    @Column
    @CassandraType(type = CassandraType.Name.SET, userTypeName = "sent_message_type", typeArguments = CassandraType.Name.UDT)
    Set<SentMessage> messageIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<JoinedParticipant> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(Set<JoinedParticipant> participantIds) {
        this.participantIds = participantIds;
    }

    public Set<SentMessage> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(Set<SentMessage> messageIds) {
        this.messageIds = messageIds;
    }

    public static ConversationDBO from(Conversation conversation) {
        ConversationDBO conversationDBO = new ConversationDBO();
        conversationDBO.setId(conversation.getId() == null ? null : conversation.getId().id());
        Set<JoinedParticipant> mappedParticipantIds = conversation.participants().stream().map(JoinedParticipant::fromParticipantId).collect(Collectors.toSet());
        conversationDBO.setParticipantIds(mappedParticipantIds);
        Set<SentMessage> mappedMessageIds = conversation.messages().stream().map(SentMessage::fromMessageId).collect(Collectors.toSet());
        conversationDBO.setMessageIds(mappedMessageIds);
        return conversationDBO;
    }

    public Conversation asConversation() {
        Conversation conversation = new Conversation();
        if (id != null) {
            conversation.setId(ConversationId.of(id));
        }
        joinAllParticipantsToConversation(conversation);
        sendAllMessagesToConversation(conversation);
        return conversation;
    }

    private void sendAllMessagesToConversation(Conversation conversation) {
        if (messageIds != null) {
            messageIds.stream().map(SentMessage::asMessageId).forEach(conversation::messageSent);
        }
    }

    private void joinAllParticipantsToConversation(Conversation conversation) {
        if (participantIds != null) {
            participantIds.stream().map(JoinedParticipant::asParticipantId).forEach(conversation::joinedBy);
        }
    }
}
