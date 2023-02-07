package com.synthese.repository;

import com.synthese.model.Manager;
import com.synthese.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ManagerRepository extends MongoRepository<Manager, Long> {
}
