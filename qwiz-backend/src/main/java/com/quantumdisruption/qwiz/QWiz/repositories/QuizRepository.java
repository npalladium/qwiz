package com.quantumdisruption.qwiz.QWiz.repositories;

import com.quantumdisruption.qwiz.QWiz.domain.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "quizzes", path = "quizzes")
public interface QuizRepository extends MongoRepository<Quiz, String> {
}
