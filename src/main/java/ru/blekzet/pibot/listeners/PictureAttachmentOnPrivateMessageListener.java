package ru.blekzet.pibot.listeners;

import lombok.RequiredArgsConstructor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.blekzet.pibot.sender.PictureSenderInterface;
import ru.blekzet.pibot.service.CollectListenersService;

import javax.annotation.PostConstruct;

@Component
public class PictureAttachmentOnPrivateMessageListener extends PictureAttachmentMessageListener {
    @Value("${recipientUserId}")
    private long recipientUserId;
    private final CollectListenersService collectListenersService;

    @Autowired
    public PictureAttachmentOnPrivateMessageListener(PictureSenderInterface pictureUrlToRecipientSender, CollectListenersService collectListenersService) {
        super(pictureUrlToRecipientSender);
        this.collectListenersService = collectListenersService;
    }


    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if(messageCreateEvent.isPrivateMessage() && messageCreateEvent.getMessageContent().startsWith("!pic")){
            execute(messageCreateEvent,recipientUserId);
        }
    }

    @Override
    @PostConstruct
    public void addListenerToContext() {
        collectListenersService.addListenerToContext(this);
    }
}
