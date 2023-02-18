package hu.martin.chatter.adapter.out.mongodb;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageReactiveCrudRepository extends
        ReactiveMongoRepository<MessageDBO, Long> {

}
