package com.quantumdisruption.qwiz.QWiz.listeners;

import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JoinListener implements SlashCommandCreateListener {

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
        User user = slashCommandInteraction.getUser();
        if (slashCommandInteraction.getCommandName().equals("jointeam")) {
            Integer option = Integer.valueOf(slashCommandInteraction.getFirstOptionStringValue().orElseThrow());
            Role role = slashCommandInteraction.getServer().get().getRolesByName(String.format("Team %d", option)).get(0);
            log.info(role.getName());
            user.addRole(role);
            log.info(String.valueOf(user.getRoles(slashCommandInteraction.getServer().get())));
        }

    }
}