package com.synthese.repository;

import com.synthese.model.Student;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, ObjectId> {
    List<Student> findByUserId(ObjectId id);
}