package ru.blekzet.pibot.listeners.direct;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.blekzet.pibot.listeners.PictureAttachmentMessageListener;
import ru.blekzet.pibot.sender.PictureSenderInterface;
import ru.blekzet.pibot.service.CollectListenersService;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class PictureAttachmentOnPrivateMessageListener extends PictureAttachmentMessageListener {
    private long serverId = 0;
    private long recipientUserId = 0;
    @Autowired
    public PictureAttachmentOnPrivateMessageListener(PictureSenderInterface pictureUrlToRecipientSender, CollectListenersService collectListenersService) {
        super(pictureUrlToRecipientSender, collectListenersService);
    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if(messageCreateEvent.isPrivateMessage() && messageCreateEvent.getMessageContent().startsWith("!pic")){
            String[] separatedCommand = messageCreateEvent.getMessageContent().split(" ");

            int attachmentVariant = 0;

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

            String enteredServerName = buildServerNameByVariant(separatedCommand, attachmentVariant);

            confirmEnteredServerNameAndGetServerRecipientId(messageCreateEvent, enteredServerName);

            if(recipientUserId == 0 || serverId == 0){
                errorMessage(messageCreateEvent);
            } else {
                execute(messageCreateEvent, recipientUserId, serverId, pictureUrl);
            }
        }
    }

    private String buildServerNameByVariant(String[] separatedCommand, int variant) {
        StringBuilder serverNameBuilder = new StringBuilder();
        for(int i = 1; i <= separatedCommand.length-variant; i++){
            serverNameBuilder.append(separatedCommand[i]);
        }
        return serverNameBuilder.toString();
    }

    private void confirmEnteredServerNameAndGetServerRecipientId(MessageCreateEvent messageCreateEvent, String enteredServerName){
        if(messageCreateEvent.getMessageAuthor().asUser().isPresent()) {
            List<Server> userServers = new ArrayList<>(messageCreateEvent.getApi().getServers());
            long senderId = messageCreateEvent.getMessageAuthor().getId();
            for(Server server: userServers){
                if(server.getName().replace(" ", "").equals(enteredServerName) && (server.getMemberById(senderId).isPresent() || server.getOwnerId() == senderId)){
                    serverId = server.getId();
                    recipientUserId = server.getOwnerId();
                    break;
                }
            }
        }
    }

    @Override
    @PostConstruct
    public void addListenerToContext() {
        collectListenersService.addListenerToContext(this);
    }
}
