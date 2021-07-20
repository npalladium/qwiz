package com.quantumdisruption.qwiz.QWiz.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "quizzes")
public class Quiz {


    public Quiz(String name, Date date, List<Integer> intro, List<Question> questions) {
        this.name = name;
        this.date = date;
        this.intro = intro;
        this.questions = questions;
    }

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Id
    private String id;

    private String name;
    private Date date;
    private List<Integer> intro;
    private List<Question> questions;
}

