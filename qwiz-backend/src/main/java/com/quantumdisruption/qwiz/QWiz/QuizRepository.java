package com.quantumdisruption.qwiz.QWiz;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "quizzes", path = "quizzes")
public interface QuizRepository extends MongoRepository<Quiz, String> {
}
