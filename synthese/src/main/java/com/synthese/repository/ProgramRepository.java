package com.synthese.repository;

import com.synthese.model.Program;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ProgramRepository extends MongoRepository<Program, ObjectId> {
    List<Program> findByEstablishment(ObjectId establishment);

    Optional<Program> findByEstablishmentAndName(ObjectId establishment, String name);
}