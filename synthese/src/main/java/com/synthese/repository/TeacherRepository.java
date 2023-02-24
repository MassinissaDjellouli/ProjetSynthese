package com.synthese.repository;

import com.synthese.model.Teacher;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface TeacherRepository extends MongoRepository<Teacher, ObjectId> {
    List<Teacher> findByUserId(ObjectId id);

    List<Teacher> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Teacher> findByUserIdAndEstablishment(ObjectId id, ObjectId establishmentId);
}