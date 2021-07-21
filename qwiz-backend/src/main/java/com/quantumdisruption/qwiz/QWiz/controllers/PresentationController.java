package com.quantumdisruption.qwiz.QWiz.controllers;

import com.quantumdisruption.qwiz.QWiz.containers.PresentationState;
import com.quantumdisruption.qwiz.QWiz.domain.Question;
import com.quantumdisruption.qwiz.QWiz.domain.Quiz;
import com.quantumdisruption.qwiz.QWiz.repositories.QuizRepository;
import com.quantumdisruption.qwiz.QWiz.responses.ActionResult;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("presentation")
@CrossOrigin
public class PresentationController {

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    DiscordApi discordApi;

    @Value("${app.uploads.location}")
    private String uploadFolder;
    @Value("${app.outputs.location}")
    private String outputFolder;

    private List<Question> questionsLeft;
    private Question currentQuestion;

    private PresentationState presentationState = PresentationState.Intro;

    private Server server;
    private List<ServerTextChannel> teams;

    private boolean firstTime = true;

    @PostConstruct
    public void init() {
        this.server = discordApi.getServerById(866228863247319081L).get();
        this.teams = getTeams(this.server);
        this.quizRepository.deleteAll();
    }


    @PostMapping("/nextquestion")
    public ActionResult nextQuestion() {
        if (this.firstTime) {
            List<Quiz> quizzes = this.quizRepository.findAll();
            this.questionsLeft = quizzes.get(quizzes.size() - 1).getQuestions();
            this.firstTime = false;
        }
        this.currentQuestion = questionsLeft.remove(0);
        sendSlides(this.currentQuestion.getQue(), outputFolder, discordApi, teams);
        return new ActionResult("success");
    }

    @PostMapping("/hint")
    public ActionResult hint() {
        sendSlides(this.currentQuestion.getHint(), outputFolder, discordApi, teams);
        return new ActionResult("success");
    }

    @PostMapping("/answer")
    public ActionResult answer() {
        sendSlides(this.currentQuestion.getAns(), outputFolder, discordApi, teams);
        return new ActionResult("success");
    }

    private static void sendSlides(){
    }


    private static void sendSlides(List<Integer> slides, String outputFolder, DiscordApi api, List<ServerTextChannel> teams){
        List<File> images =  new ArrayList<>();
        for (Integer s : slides) {
            Path p = Paths.get(outputFolder, String.format("pdf-%03d.jpg", s));
            images.add(p.toFile());
        }
        MessageBuilder messageBuilder = new MessageBuilder().append("Look at these");
        for (File f: images) {
            messageBuilder = messageBuilder.addAttachment(f);
        }
        for (ServerTextChannel team : teams) {
            messageBuilder.send(team).join();
        }
    }

    private static List<ServerTextChannel> getTeams(Server server) {
        List<ServerTextChannel> channels = server.getTextChannels();
        List<ServerTextChannel> teams = new ArrayList<>();
        for ( ServerTextChannel s: channels) {
            if(s.getName().startsWith("team")) {
                teams.add(s);
            }
        }
        return teams;
    }
}
