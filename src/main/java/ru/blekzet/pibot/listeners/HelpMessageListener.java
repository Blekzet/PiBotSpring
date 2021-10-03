package ru.blekzet.pibot.listeners;

import lombok.RequiredArgsConstructor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.stereotype.Component;
import ru.blekzet.pibot.service.CollectListenersService;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class HelpMessageListener implements MessageCreateListener {

    private final CollectListenersService collectListenersService;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getServerTextChannel().isPresent() || messageCreateEvent.isPrivateMessage()){
            if((messageCreateEvent.isPrivateMessage() || messageCreateEvent.getServerTextChannel().get().getName().equals("pibot-home")) && messageCreateEvent.getMessageContent().startsWith("!help")){
                messageCreateEvent.getChannel().sendMessage("Команды бота на сервере:" +
                        "\n 1) !pic {Url картинки без скобочек} или {вложить в сообщение картинку} отправить картинку владельцу сервера на проверку" +
                        "\n 2) !tellmeajoke бот покажет случайный мем" +
                        "\n Команды бота в приватном чате:"+
                        "\n 1) !pic {Имя сервера без скобочек} {Url картинки без скобочек} или {вложить в сообщение картинку} отправить картинку владельцу сервера на проверку" +
                        "\n 2) !tellmeajoke бот покажет случайный мем");
            }
        }
    }

    @PostConstruct
    public void addListenerToContext() {
        collectListenersService.addListenerToContext(this);
    }
}
