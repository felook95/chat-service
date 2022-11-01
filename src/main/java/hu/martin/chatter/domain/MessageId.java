package hu.martin.chatter.domain;

public record MessageId(Long id) {

  public static MessageId of(Long id) {
    return new MessageId(id);
  }
}
