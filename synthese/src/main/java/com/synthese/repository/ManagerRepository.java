package com.synthese.repository;

import com.synthese.model.Manager;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface ManagerRepository extends MongoRepository<Manager, Long> {
    Optional<Manager> getByUserId(ObjectId id);
}