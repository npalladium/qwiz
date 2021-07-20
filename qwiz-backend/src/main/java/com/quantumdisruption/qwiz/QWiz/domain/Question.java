package com.quantumdisruption.qwiz.QWiz.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Question {
    private List<Integer> que;
    private List<Integer> hint;
    private List<Integer> ans;
}
