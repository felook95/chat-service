package hu.martin.chatservice.domain;

public record ConversationId(Long id) {

  public static ConversationId of(Long id) {
    return new ConversationId(id);
  }
}
