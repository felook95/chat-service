package hu.martin.chatter.adapter.in.web.conversation;

import hu.martin.chatter.domain.conversation.Conversation;
import hu.martin.chatter.domain.conversation.ConversationId;
import hu.martin.chatter.domain.message.MessageId;
import hu.martin.chatter.domain.participant.ParticipantId;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public record ConversationDTO(BigInteger id, Set<BigInteger> participantIds, Set<BigInteger> messageIds) {

    public ConversationDTO(BigInteger id, Set<BigInteger> participantIds, Set<BigInteger> messageIds) {
        this.id = id;
        this.participantIds = participantIds == null ? Collections.emptySet() : participantIds;
        this.messageIds = messageIds == null ? Collections.emptySet() : messageIds;
    }

    public static ConversationDTO from(Conversation conversation) {
        return new ConversationDTO(conversation.getId() == null ? null : conversation.getId().id(),
                conversation.participants().stream().map(ParticipantId::id).collect(Collectors.toSet()),
                conversation.messages().stream().map(MessageId::id).collect(Collectors.toSet()));
    }

    public Conversation asConversation() {
        Conversation conversation = new Conversation();
        if (id != null) {
            conversation.setId(ConversationId.of(id));
        }
        participantIds().stream().map(ParticipantId::of).forEach(conversation::joinedBy);
        messageIds().stream().map(MessageId::of).forEach(conversation::messageSent);
        return conversation;
    }
}
