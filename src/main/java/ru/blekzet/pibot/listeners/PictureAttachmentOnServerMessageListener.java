package ru.blekzet.pibot.listeners;

import lombok.*;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.blekzet.pibot.sender.PictureSenderInterface;
import ru.blekzet.pibot.service.CollectListenersService;

import javax.annotation.PostConstruct;

@Component
public class PictureAttachmentOnServerMessageListener extends PictureAttachmentMessageListener{
    private final CollectListenersService collectListenersService;

    @Autowired
    public PictureAttachmentOnServerMessageListener(PictureSenderInterface pictureUrlToRecipientSender, CollectListenersService collectListenersService) {
        super(pictureUrlToRecipientSender);
        this.collectListenersService = collectListenersService;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getServerTextChannel().isPresent() && messageCreateEvent.getServer().isPresent()){
            if(messageCreateEvent.getServerTextChannel().get().getName().equals("pibot-home") && messageCreateEvent.getMessageContent().startsWith("!pic")){
                long recipientUserId = messageCreateEvent.getServer().get().getOwnerId();
                execute(messageCreateEvent, recipientUserId);
            }
        }
    }


    @Override
    @PostConstruct
    public void addListenerToContext() {
        collectListenersService.addListenerToContext(this);
    }
}
