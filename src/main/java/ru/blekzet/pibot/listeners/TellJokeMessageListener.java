package ru.blekzet.pibot.listeners;

import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.blekzet.pibot.service.CollectListenersService;
import ru.blekzet.pibot.service.JoyreactorParser;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TellJokeMessageListener implements MessageCreateListener {

    @Value("${joyUrl}")
    private String joyReactorUrl;
    private final CollectListenersService collectListenersService;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getServerTextChannel().isPresent() || messageCreateEvent.isPrivateMessage()){
            if((messageCreateEvent.isPrivateMessage() || messageCreateEvent.getServerTextChannel().get().getName().equals("pibot-home")) && messageCreateEvent.getMessageContent().startsWith("!tellmeajoke")){
                JoyreactorParser joyreactorParser = new JoyreactorParser(joyReactorUrl);
                for (String picUrl: joyreactorParser.getRandomPictures()) {
                    messageCreateEvent.getChannel().sendMessage(new EmbedBuilder().setImage(picUrl));
                }
            }
        }
    }

    @PostConstruct
    public void addListenerToContext() {
        collectListenersService.addListenerToContext(this);
    }
}
