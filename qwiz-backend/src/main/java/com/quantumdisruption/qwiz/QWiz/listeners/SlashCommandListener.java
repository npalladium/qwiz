package com.quantumdisruption.qwiz.QWiz.listeners;

import com.quantumdisruption.qwiz.QWiz.containers.EmitterContainer;
import com.quantumdisruption.qwiz.QWiz.messages.EventMessage;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class SlashCommandListener implements SlashCommandCreateListener {

    @Autowired
    EmitterContainer emitterContainer;

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
        User user = slashCommandInteraction.getUser();
        Server server = slashCommandInteraction.getServer().get();
        String command = slashCommandInteraction.getCommandName();
        if (command.equals("jointeam")) {
            onJoinTeam(slashCommandInteraction, server, user);
        }
        if (command.equals("pounce")) {
            onPounce(slashCommandInteraction, server, user, emitterContainer);
        }
        if (command.equals("bounce")) {
            onBounce(slashCommandInteraction, server, user, emitterContainer);
        }
        if (command.equals("startquiz")) {
            onStartQuiz(slashCommandInteraction, server, user);
        }
        if (command.equals("endquiz")) {
            onEndQuiz(slashCommandInteraction, server, user);
        }

    }

    private static void onEndQuiz(SlashCommandInteraction slashCommandInteraction, Server server, User user) {
        slashCommandInteraction.createImmediateResponder()
                .setContent("Ended!").respond();
    }

    private static void onStartQuiz(SlashCommandInteraction slashCommandInteraction, Server server, User user) {
        slashCommandInteraction.createImmediateResponder()
                .setContent("Started!").respond();
    }

    private static void onBounce(SlashCommandInteraction slashCommandInteraction, Server server, User user, EmitterContainer emitterContainer) {
        slashCommandInteraction.createImmediateResponder()
                .setContent("Bounced!").respond();
    }

    private static void onJoinTeam(SlashCommandInteraction slashCommandInteraction, Server server, User user) {
        Integer option = Integer.valueOf(slashCommandInteraction.getFirstOptionStringValue().orElseThrow());
        List<Role> roles = server.getRolesByName(String.format("Team %d", option));
        for (Role role : roles) {
            user.addRole(role);
        }
        slashCommandInteraction.createImmediateResponder()
                .setContent("Joined!").respond();
        log.info(roles.toString());
        log.info(String.valueOf(user.getRoles(slashCommandInteraction.getServer().get())));
    }

    private static void onPounce(SlashCommandInteraction slashCommandInteraction, Server server, User user, EmitterContainer emitterContainer) {
        String c = slashCommandInteraction.getChannel().get().getIdAsString();
        String channel = server.getChannelById(c).get().getName();
        String option = slashCommandInteraction.getFirstOptionStringValue().orElseThrow();
        slashCommandInteraction.createImmediateResponder()
                .setContent("Pounced!").respond();
        constructAndSendEvent(emitterContainer, user.getName(), channel, option);
    }

    private static void constructAndSendEvent(EmitterContainer emitterContainer, String user, String channel, String option) {
        EventMessage message = new EventMessage("pounce", option, channel, user);
        SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event();
        eventBuilder.data(message.toString(), MediaType.APPLICATION_JSON).name("pounce").id(String.valueOf(message.hashCode()));
        sendEvent(emitterContainer, eventBuilder);
    }

    private static void sendEvent(EmitterContainer emitterContainer, SseEmitter.SseEventBuilder eventBuilder) {
        for (SseEmitter emitter : new ArrayList<>(emitterContainer.getPounceSubscribers())) {
            try {
                emitter.send(eventBuilder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}