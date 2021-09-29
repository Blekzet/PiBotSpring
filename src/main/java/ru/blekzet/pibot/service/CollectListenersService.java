package ru.blekzet.pibot.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.javacord.api.DiscordApi;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class CollectListenersService {
    private final DiscordApi discordApi;

    public void addListenerToContext(MessageCreateListener listener){
        discordApi.addListener(listener);
    }
}
