package hu.martin.chatter.adapter.out.mongodb;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ConversationReactiveCrudRepository extends
        ReactiveMongoRepository<ConversationDBO, BigInteger> {

}
