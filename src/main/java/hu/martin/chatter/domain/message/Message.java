package hu.martin.chatter.domain.message;

import hu.martin.chatter.domain.participant.ParticipantId;

public class Message {

    private CreatedDateTime createdDateTime;
    private final ParticipantId senderId;
    private MessageId id;
    private MessageContent content;
    private MessageStatus statusFlag;

    public Message(ParticipantId senderId, MessageContent content) {
        this.senderId = senderId;
        this.content = content;
        this.statusFlag = MessageStatus.CREATED;
    }

    public Message(ParticipantId senderId, MessageContent content, CreatedDateTime createdDateTime) {
        this(senderId, content);
        this.createdDateTime = createdDateTime;
    }

    public void changeContentTo(MessageContent content) {
        assertNotDeleted();
        this.content = content;
        edited();
    }

    private void assertNotDeleted() {
        if (MessageStatus.DELETED.equals(statusFlag())) {
            throw new IllegalStateException("Deleted message cannot be edited");
        }
    }

    private void edited() {
        changeStatusFlagTo(MessageStatus.EDITED);
    }

    public MessageContent content() {
        return content;
    }

    public void delete() {
        statusFlag = MessageStatus.DELETED;
    }

    public MessageStatus statusFlag() {
        return statusFlag;
    }

    public MessageId id() {
        return id;
    }

    public void setId(MessageId id) {
        this.id = id;
    }

    public CreatedDateTime createdDateTime() {
        return createdDateTime;
    }

    public void changeCreatedDateTimeTo(CreatedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void changeStatusFlagTo(MessageStatus newStatusFlag) {
        this.statusFlag = newStatusFlag;
    }

    public ParticipantId sender() {
        return senderId;
    }
}
