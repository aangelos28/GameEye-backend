package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Override
    void deleteById(String id);

    void deleteByFirebaseId(String firebaseId);

    @Override
    boolean existsById(String id);

    boolean existsByFirebaseId(String firebaseId);

    User findUserById(String id);

    User findUserByFirebaseId(String firebaseId);

}
