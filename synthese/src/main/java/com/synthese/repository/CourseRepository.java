package com.synthese.repository;

import com.synthese.model.Course;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface CourseRepository extends MongoRepository<Course, ObjectId> {
    List<Course> findByProgram(ObjectId programId);

}