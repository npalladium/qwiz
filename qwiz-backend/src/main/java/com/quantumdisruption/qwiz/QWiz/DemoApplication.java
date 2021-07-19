package com.quantumdisruption.qwiz.QWiz;

import com.quantumdisruption.qwiz.QWiz.listeners.JoinListener;
import com.quantumdisruption.qwiz.QWiz.listeners.PounceListener;
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

import java.util.Arrays;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class DemoApplication {

    @Value("${bot.token}")
    String token;

    @Autowired
    PounceListener pounceListener;

    @Autowired
    JoinListener joinListener;


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    private static void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageContent().equalsIgnoreCase(".ping")) {
            event.getChannel().sendMessage("Pong!");
        }
    }

    private static void getAllListeners(DiscordApi api) {
        api.getListeners().values().forEach(value -> value.forEach(list -> System.out.println(list.toString())));
    }

    @Bean
    @ConfigurationProperties("discord-api")
    public DiscordApi discordApi() {
        DiscordApiBuilder builder = new DiscordApiBuilder().setToken(token).setAllIntents();
        DiscordApi api = builder.login().join();
        // Optional will always contain the value.
        Server server = api.getServerById("866228863247319081").get();
        api.addMessageCreateListener(DemoApplication::onMessageCreate);
        getAllListeners(api);
        setSlashCommands(server, api);

        return api;
    }

    private void setSlashCommands(Server server, DiscordApi api) {
        SlashCommand.with("startquiz", "Start the quiz").createForServer(server).join();
        SlashCommand.with("endquiz", "End the quiz").createForServer(server).join();
        SlashCommand.with("jointeam", "Join this team",
                Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.INTEGER,
                        "team",
                        "team which you want to join"))
        ).createForServer(server).join();
        SlashCommand.with("pounce", "Secret attempt",
                Arrays.asList(SlashCommandOption.create(SlashCommandOptionType.STRING,
                        "answer",
                        "Answer"))
        ).createForServer(server).join();
        api.addSlashCommandCreateListener(pounceListener);
        api.addSlashCommandCreateListener(joinListener);
        getAllListeners(api);
    }

}
