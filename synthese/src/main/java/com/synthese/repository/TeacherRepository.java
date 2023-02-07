package com.synthese.repository;

import com.synthese.model.Student;
import com.synthese.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TeacherRepository extends MongoRepository<Teacher, Long> {
}
