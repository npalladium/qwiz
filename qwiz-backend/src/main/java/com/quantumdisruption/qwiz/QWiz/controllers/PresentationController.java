package com.quantumdisruption.qwiz.QWiz.controllers;

import com.quantumdisruption.qwiz.QWiz.containers.PresentationState;
import com.quantumdisruption.qwiz.QWiz.domain.Question;
import com.quantumdisruption.qwiz.QWiz.repositories.QuizRepository;
import com.quantumdisruption.qwiz.QWiz.responses.RealTime;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.DiscordApi;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("presentation")
public class PresentationController {

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    DiscordApi discordApi;

    private List<Question> questionsLeft;
    private Question currentQuestion;

    private PresentationState presentationState = PresentationState.Intro;

    @PostMapping("/test")
    public JSONObject test() {
        log.info(discordApi.toString());
        return new JSONObject().put("test", discordApi.createBotInvite());
    }

    @PostMapping("/nextquestion")
    public RealTime nextQuestion() {
        validateAndUpdateState(PresentationState.Question);
        return new RealTime();
    }

    private void validateAndUpdateState(PresentationState possibleFuture) {
        if (this.presentationState.nextState() == possibleFuture) {
           sendSlides();
           this.presentationState = this.presentationState.nextState();
        }
    }

    @PostMapping("/hint")
    public RealTime hint() {
        return new RealTime();
    }

    @PostMapping("/answer")
    public RealTime answer() {
        return new RealTime();
    }
    
    private static void sendSlides(){
    }




}
