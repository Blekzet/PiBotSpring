package ru.blekzet.pibot.listeners.server;

import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.blekzet.pibot.listeners.PictureAttachmentMessageListener;
import ru.blekzet.pibot.sender.PictureSenderInterface;
import ru.blekzet.pibot.service.CollectListenersService;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class PictureAttachmentOnServerMessageListener extends PictureAttachmentMessageListener {

    @Autowired
    public PictureAttachmentOnServerMessageListener(PictureSenderInterface pictureUrlToRecipientSender, CollectListenersService collectListenersService) {
        super(pictureUrlToRecipientSender, collectListenersService);
    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getServerTextChannel().isPresent() && messageCreateEvent.getServer().isPresent()){
            if(messageCreateEvent.getServerTextChannel().get().getName().equals("pibot-home") && messageCreateEvent.getMessageContent().startsWith("!pic")){
                long recipientUserId = messageCreateEvent.getServer().get().getOwnerId();
                long serverId = messageCreateEvent.getServer().get().getId();
                try {
                    if(messageCreateEvent.getMessageAttachments().isEmpty()) {
                        pictureUrl = new URL(messageCreateEvent.getMessageContent().substring(4));
                    } else {
                        pictureUrl = pictureAsAttachmentHandler(messageCreateEvent);
                    }
                } catch (MalformedURLException e) {
                    errorMessage(messageCreateEvent);
                }
                execute(messageCreateEvent, recipientUserId, serverId, pictureUrl);
            }
        }
    }


    @Override
    @PostConstruct
    public void addListenerToContext() {
        collectListenersService.addListenerToContext(this);
    }
}
