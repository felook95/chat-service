package hu.martin.chatter.adapter.out.cassandra;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageReactiveCrudRepository extends
    ReactiveCassandraRepository<MessageDBO, Long> {

}
