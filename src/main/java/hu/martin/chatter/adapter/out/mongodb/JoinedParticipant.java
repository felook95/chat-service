package hu.martin.chatter.adapter.out.mongodb;

import hu.martin.chatter.domain.ParticipantId;

public class JoinedParticipant {

  Long id;

  public JoinedParticipant() {
  }

  public JoinedParticipant(Long id) {
    this.id = id;
  }

  static JoinedParticipant fromParticipantId(ParticipantId participantId) {
    return new JoinedParticipant(participantId.id());
  }

  ParticipantId asParticipantId() {
    return ParticipantId.of(id);
  }

}
