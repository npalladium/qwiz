package com.quantumdisruption.qwiz.QWiz;

import com.quantumdisruption.qwiz.QWiz.containers.EmitterContainer;
import com.quantumdisruption.qwiz.QWiz.listeners.SlashCommandListener;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PreDestroy;
import java.util.Arrays;

@Slf4j
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class QwizApplication {

    @Value("${bot.token}")
    String token;

    @Autowired
    SlashCommandListener slashCommandListener;

    @Autowired
    EmitterContainer emitterContainer;


    public static void main(String[] args) {
        SpringApplication.run(QwizApplication.class, args);
    }

    @PreDestroy
    public void tearDown() {
        log.info("Shutting Down");
        emitterContainer.getPounceSubscribers().forEach(emitter -> {emitter.complete();});
    }

    private static void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageContent().equalsIgnoreCase(".ping")) {
            event.getChannel().sendMessage("Pong!");
        }
    }

    private static void logAllListeners(DiscordApi api) {
        api.getListeners().values().forEach(value -> value.forEach(list -> log.info(list.toString())));
    }

    @Bean
    @ConfigurationProperties("discord-api")
    public DiscordApi discordApi() {
        DiscordApiBuilder builder = new DiscordApiBuilder().setToken(token).setAllIntents();
        DiscordApi api = builder.login().join();
        // Optional will always contain the value.
        Server server = api.getServerById("866228863247319081").get();
        api.addMessageCreateListener(QwizApplication::onMessageCreate);
        setSlashCommands(server, api);
        logAllListeners(api);
        return api;
    }

    private void setSlashCommands(Server server, DiscordApi api) {
        SlashCommand.with("startquiz", "Start the quiz").createForServer(server).join();
        SlashCommand.with("endquiz", "End the quiz").createForServer(server).join();
        SlashCommand.with("jointeam", "Join this team",
                Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.INTEGER,
                        "team",
                        "team which you want to join", true))
        ).createForServer(server).join();
        SlashCommand.with("pounce", "Secret attempt",
                Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.STRING,
                        "answer",
                        "Answer", true))
        ).createForServer(server).join();
        api.addSlashCommandCreateListener(slashCommandListener);
    }

}
