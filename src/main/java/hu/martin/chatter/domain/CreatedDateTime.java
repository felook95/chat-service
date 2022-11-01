package hu.martin.chatter.domain;

import java.time.ZonedDateTime;

public record CreatedDateTime(ZonedDateTime createdDateTime) implements
    Comparable<CreatedDateTime> {

  public static CreatedDateTime of(ZonedDateTime createdDateTime) {
    return new CreatedDateTime(createdDateTime);
  }

  @Override
  public int compareTo(CreatedDateTime o) {
    return createdDateTime.compareTo(o.createdDateTime);
  }
}
