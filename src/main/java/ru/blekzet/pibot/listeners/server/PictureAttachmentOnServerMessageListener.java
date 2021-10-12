package ru.blekzet.pibot.listeners.server;

import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.blekzet.pibot.listeners.PictureAttachmentMessageListener;
import ru.blekzet.pibot.sender.PictureSenderToRecipientInterface;
import ru.blekzet.pibot.service.CollectListenersService;

import javax.annotation.PostConstruct;

@Component
public class PictureAttachmentOnServerMessageListener extends PictureAttachmentMessageListener {

    @Autowired
    public PictureAttachmentOnServerMessageListener(PictureSenderToRecipientInterface pictureUrlToRecipientSender, CollectListenersService collectListenersService) {
        super(pictureUrlToRecipientSender, collectListenersService);
    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getServerTextChannel().isPresent() && messageCreateEvent.getServer().isPresent()){
            if(messageCreateEvent.getServerTextChannel().get().getName().equals("pibot-home") && messageCreateEvent.getMessageContent().startsWith("!pic")){

                if(messageCreateEvent.getMessageAttachments().isEmpty()) {
                    pictureUrl = messageCreateEvent.getMessageContent().substring(4);
                } else {
                    pictureUrl = pictureAsAttachmentHandler(messageCreateEvent);
                }

                execute(messageCreateEvent, messageCreateEvent.getServer().get(), pictureUrl);
                messageCreateEvent.deleteMessage();
            }
        }
    }

    @Override
    @PostConstruct
    public void addListenerToContext() {
        collectListenersService.addListenerToContext(this);
    }
}
