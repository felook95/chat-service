package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.domain.Conversation;
import hu.martin.chatter.domain.ConversationId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

@Document(collection = "conversations")
public class ConversationDBO {

    @Id
    BigInteger id;

    Set<JoinedParticipant> participantIds;

    Set<SentMessage> messageIds;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
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
