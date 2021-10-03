package ru.blekzet.pibot.listeners.server;

import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.blekzet.pibot.listeners.PictureAttachmentCommandListener;
import ru.blekzet.pibot.sender.PictureSenderInterface;
import ru.blekzet.pibot.service.CollectListenersService;

import javax.annotation.PostConstruct;

@Component
public class PictureAttachmentOnServerMessageListener extends PictureAttachmentCommandListener {

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

                if(messageCreateEvent.getMessageAttachments().isEmpty()) {
                    pictureUrl = messageCreateEvent.getMessageContent().substring(4);
                } else {
                    pictureUrl = pictureAsAttachmentHandler(messageCreateEvent);

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
