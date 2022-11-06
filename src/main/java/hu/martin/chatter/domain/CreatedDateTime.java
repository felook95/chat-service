package hu.martin.chatter.domain;

import java.time.LocalDateTime;

public record CreatedDateTime(LocalDateTime createdDateTime) implements
    Comparable<CreatedDateTime> {

  public static CreatedDateTime of(LocalDateTime createdDateTime) {
    return new CreatedDateTime(createdDateTime);
  }

  @Override
  public int compareTo(CreatedDateTime o) {
    return createdDateTime.compareTo(o.createdDateTime);
  }
}
