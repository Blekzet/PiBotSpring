package ru.blekzet.pibot.config;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.blekzet.pibot.listeners.PictureAttachmentMessageListener;

import javax.annotation.PostConstruct;

@Configuration
public class BotConfig {

    @Value("${token}")
    private String token;

    @Bean
    public DiscordApi discordApi() {
        return new DiscordApiBuilder().setToken(token).login().join();
    }
}
