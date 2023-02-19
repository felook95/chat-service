package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.domain.ParticipantId;

import java.math.BigInteger;

public class JoinedParticipant {

  BigInteger id;

  public JoinedParticipant() {
  }

  public JoinedParticipant(BigInteger id) {
    this.id = id;
  }

  static JoinedParticipant fromParticipantId(ParticipantId participantId) {
    return new JoinedParticipant(participantId.id());
  }

  ParticipantId asParticipantId() {
    return ParticipantId.of(id);
  }

}
