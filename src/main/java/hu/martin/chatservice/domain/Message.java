package hu.martin.chatservice.domain;

public class Message {

  private final CreatedDateTime createdDateTime;
  private MessageId id;
  private final ParticipantId senderId;
  private MessageContent content;
  private MessageStatus statusFlag;


  public Message(ParticipantId senderId, MessageContent content, CreatedDateTime createdDateTime) {
    this.senderId = senderId;
    this.content = content;
    this.statusFlag = MessageStatus.CREATED;
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

  public void deleted() {
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

  public void changeStatusFlagTo(MessageStatus newStatusFlag) {
    this.statusFlag = newStatusFlag;
  }

  public ParticipantId sender() {
    return senderId;
  }
}
