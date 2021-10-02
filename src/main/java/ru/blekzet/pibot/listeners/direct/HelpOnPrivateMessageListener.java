package ru.blekzet.pibot.listeners.direct;

import lombok.RequiredArgsConstructor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.stereotype.Component;
import ru.blekzet.pibot.service.CollectListenersService;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class HelpOnPrivateMessageListener implements MessageCreateListener  {

    private final CollectListenersService collectListenersService;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if(messageCreateEvent.isPrivateMessage() && messageCreateEvent.getMessageContent().equals("!help")){
            messageCreateEvent.getChannel().sendMessage("Команды бота:" +
                    "\n 1) !pic {Имя сервера без ковычек} {Url картинки без скобочек}" +
                    "\n 2) !pic {Имя сервера без ковычек} {вложить в сообщение картинку}");
        }
    }

    @PostConstruct
    public void addListenerToContext() {
        collectListenersService.addListenerToContext(this);
    }
}