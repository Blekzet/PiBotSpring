package ru.blekzet.pibot.config;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-prod.properties")
public class BotConfig {

    @Value("${token}")
    private String token;

    @Bean
    public DiscordApi discordApi() {
        return new DiscordApiBuilder().setToken(token).login().join();
    }
}
