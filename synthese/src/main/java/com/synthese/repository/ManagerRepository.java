package com.synthese.repository;

import com.synthese.model.Manager;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ManagerRepository extends MongoRepository<Manager, ObjectId> {
    List<Manager> findByUserId(ObjectId id);

    List<Manager> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Manager> findByUserIdAndEstablishment(ObjectId id, ObjectId establishment);
}