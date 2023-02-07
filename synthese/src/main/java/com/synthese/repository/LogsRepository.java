package com.synthese.repository;

import com.synthese.model.Log;
import com.synthese.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface LogsRepository extends MongoRepository<Log, Long> {
}
