package com.synthese.repository;

import com.synthese.model.Establishment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstablishmentRepository extends MongoRepository<Establishment, ObjectId> {
    public Optional<Establishment> findById(ObjectId id);

    List<Establishment> findByAdministratorsContaining(ObjectId adminId);
}