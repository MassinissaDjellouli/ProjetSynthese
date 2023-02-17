package com.synthese.repository;

import com.synthese.model.Student;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, Long> {
    Optional<Student> getByUserId(ObjectId id);
}