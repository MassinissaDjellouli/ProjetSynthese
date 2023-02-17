package com.synthese.repository;

import com.synthese.model.User;
import com.synthese.security.Roles;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByUsernameAndRole(String username, Roles role);
}