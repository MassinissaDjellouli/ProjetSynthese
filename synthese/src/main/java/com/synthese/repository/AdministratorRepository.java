package com.synthese.repository;

import com.synthese.model.Administrator;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministratorRepository extends MongoRepository<Administrator, Long> {

    Optional<Administrator> getByUserId(ObjectId id);
}