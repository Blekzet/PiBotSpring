package ru.blekzet.pibot.listeners;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.blekzet.pibot.sender.PictureSenderInterface;
import ru.blekzet.pibot.service.CollectListenersService;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PictureAttachmentOnPrivateMessageListener extends PictureAttachmentMessageListener {
    @Autowired
    public PictureAttachmentOnPrivateMessageListener(PictureSenderInterface pictureUrlToRecipientSender, CollectListenersService collectListenersService) {
        super(pictureUrlToRecipientSender, collectListenersService);
    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if(messageCreateEvent.isPrivateMessage() && messageCreateEvent.getMessageContent().startsWith("!pic")){
            String[] separatedCommand = messageCreateEvent.getMessageContent().split(" ");
            StringBuilder serverName = new StringBuilder();
            long serverId = 0;
            long recipientUserId = 0;
            int attachmentVariant = 2;

            try {
                if(messageCreateEvent.getMessageAttachments().isEmpty()) {
                    pictureUrl = new URL(separatedCommand[separatedCommand.length - 1]);
                    attachmentVariant = 2;
                } else {
                    pictureUrl = pictureAsAttachmentHandler(messageCreateEvent);
                    attachmentVariant = 1;
                }
            } catch (MalformedURLException e) {
                errorMessage(messageCreateEvent);
            }
            for(int i = 1; i <= separatedCommand.length-attachmentVariant; i++){
                serverName.append(separatedCommand[i]);
            }

            if(messageCreateEvent.getMessageAuthor().asUser().isPresent()) {
                List<Server> userServers = new ArrayList<>(messageCreateEvent.getApi().getServers());
                long senderId = messageCreateEvent.getMessageAuthor().getId();
                for(Server server: userServers){
                    if(server.getName().replace(" ", "").equals(serverName.toString()) && (server.getMemberById(senderId).isPresent() || server.getOwnerId() == senderId)){
                        serverId = server.getId();
                        recipientUserId = server.getOwnerId();
                        break;
                    }
                }
            }

            if(recipientUserId == 0 || serverId == 0){
                errorMessage(messageCreateEvent);
            } else {
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
