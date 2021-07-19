package com.quantumdisruption.qwiz.QWiz.listeners;

import com.quantumdisruption.qwiz.QWiz.EmitterContainer;
import com.quantumdisruption.qwiz.QWiz.EventMessage;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import java.io.IOException;

@Component
public class PounceListener implements SlashCommandCreateListener {

    @Autowired
    EmitterContainer emitterContainer;

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
        String user = slashCommandInteraction.getUser().getName();
        String c= slashCommandInteraction.getChannel().get().getIdAsString();
        String channel  = slashCommandInteraction.getServer().get().getChannelById(c).get().getName();
        String option = slashCommandInteraction.getFirstOptionStringValue().orElseThrow();
        if (slashCommandInteraction.getCommandName().equals("pounce")) {
            slashCommandInteraction.createImmediateResponder()
                    .setContent("Pounced!").respond();

            emitterContainer.getPounceSubscribers().forEach(emitter -> {
                EventMessage message = new EventMessage("pounce", option, channel, user);
                SseEventBuilder eventBuilder = SseEmitter.event();
                eventBuilder.data(message.toString(), MediaType.APPLICATION_JSON).name("pounce").id(String.valueOf(message.hashCode()));
                try {
                    emitter.send(eventBuilder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }
}