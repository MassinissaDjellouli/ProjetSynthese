package com.synthese.repository;

import com.synthese.model.Establishment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstablishmentRepository extends MongoRepository<Establishment, ObjectId> {

}