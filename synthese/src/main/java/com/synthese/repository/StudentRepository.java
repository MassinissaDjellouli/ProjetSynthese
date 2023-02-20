package com.synthese.repository;

import com.synthese.model.Student;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, ObjectId> {
    List<Student> findByUserId(ObjectId id);

    Optional<Student> findByUserIdAndEstablishment(ObjectId id, ObjectId establishment);

    List<Student> findByFirstNameAndLastName(String firstName, String lastName);
}