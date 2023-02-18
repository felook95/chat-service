package hu.martin.chatter.adapter.out.cassandra;

import hu.martin.chatter.domain.ParticipantId;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@Table("conversation_participant")
@UserDefinedType("joined_participant_type")
public class JoinedParticipant {

  @PrimaryKey
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
