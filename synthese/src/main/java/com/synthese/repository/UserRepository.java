package com.synthese.repository;

import com.synthese.enums.Roles;
import com.synthese.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByUsernameAndRole(String username, Roles role);
}