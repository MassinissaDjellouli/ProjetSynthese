package com.synthese.repository;

import com.synthese.model.Teacher;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface TeacherRepository extends MongoRepository<Teacher, ObjectId> {
    Optional<Teacher> findByUserId(ObjectId id);
}