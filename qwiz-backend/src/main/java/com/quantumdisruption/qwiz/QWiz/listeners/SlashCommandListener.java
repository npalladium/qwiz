package com.quantumdisruption.qwiz.QWiz.listeners;

import com.quantumdisruption.qwiz.QWiz.EmitterContainer;
import com.quantumdisruption.qwiz.QWiz.EventMessage;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
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
        if (slashCommandInteraction.getCommandName().equals("jointeam")) {
            onJoinTeam(slashCommandInteraction, user);
        }
        if (slashCommandInteraction.getCommandName().equals("pounce")) {
            onPounce(slashCommandInteraction, emitterContainer);
        }

    }

    private static void onJoinTeam(SlashCommandInteraction slashCommandInteraction, User user) {
        Integer option = Integer.valueOf(slashCommandInteraction.getFirstOptionStringValue().orElseThrow());
        List<Role> roles = slashCommandInteraction.getServer().get().getRolesByName(String.format("Team %d", option));
        for (Role role : roles) {
            user.addRole(role);
        }
        log.info(roles.toString());
        log.info(String.valueOf(user.getRoles(slashCommandInteraction.getServer().get())));
    }

    private static void onPounce(SlashCommandInteraction slashCommandInteraction, EmitterContainer emitterContainer) {
        String user = slashCommandInteraction.getUser().getName();
        String c = slashCommandInteraction.getChannel().get().getIdAsString();
        String channel = slashCommandInteraction.getServer().get().getChannelById(c).get().getName();
        String option = slashCommandInteraction.getFirstOptionStringValue().orElseThrow();
        slashCommandInteraction.createImmediateResponder()
                .setContent("Pounced!").respond();

        EventMessage message = new EventMessage("pounce", option, channel, user);
        SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event();
        eventBuilder.data(message.toString(), MediaType.APPLICATION_JSON).name("pounce").id(String.valueOf(message.hashCode()));
        sendEvent(emitterContainer, eventBuilder);
    }

    private static void sendEvent(EmitterContainer emitterContainer, SseEmitter.SseEventBuilder eventBuilder) {
        for (SseEmitter emitter : emitterContainer.getPounceSubscribers()) {
            try {
                emitter.send(eventBuilder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
