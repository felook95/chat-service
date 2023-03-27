package hu.martin.chatter.adapter.out.mongodb;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.math.BigInteger;

@Repository
public interface MessageReactiveCrudRepository extends
        ReactiveMongoRepository<MessageDBO, BigInteger> {

    Flux<MessageDBO> findByIdIn(Iterable<BigInteger> ids, Pageable pageable);

}
