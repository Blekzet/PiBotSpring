package ru.blekzet.pibot.listeners.direct;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.blekzet.pibot.listeners.PictureAttachmentMessageListener;
import ru.blekzet.pibot.sender.PictureSenderInterface;
import ru.blekzet.pibot.service.CollectListenersService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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

            int attachmentVariant;

            if(messageCreateEvent.getMessageAttachments().isEmpty()) {
                pictureUrl = separatedCommand[separatedCommand.length - 1];
                attachmentVariant = 2;
            } else {
                pictureUrl = pictureAsAttachmentHandler(messageCreateEvent);
                attachmentVariant = 1;
            }

            String enteredServerName = buildServerNameByVariant(separatedCommand, attachmentVariant);
            Server confirmedServer = confirmEnteredServerName(messageCreateEvent, enteredServerName);

            long serverId = confirmedServer.getId();
            long recipientUserId = confirmedServer.getOwnerId();

            execute(messageCreateEvent, recipientUserId, serverId, pictureUrl);
        }
    }

    private String buildServerNameByVariant(String[] separatedCommand, int variant) {
        StringBuilder serverNameBuilder = new StringBuilder();
        for(int i = 1; i <= separatedCommand.length-variant; i++){
            serverNameBuilder.append(separatedCommand[i]);
        }
        return serverNameBuilder.toString();
    }

    private Server confirmEnteredServerName(MessageCreateEvent messageCreateEvent, String enteredServerName) throws RuntimeException{
        if(messageCreateEvent.getMessageAuthor().asUser().isPresent()) {
            List<Server> botServers = new ArrayList<>(messageCreateEvent.getApi().getServers());
            long senderId = messageCreateEvent.getMessageAuthor().getId();
            for(Server server: botServers){
                if(server.getName().replace(" ", "").equals(enteredServerName) && (server.getMemberById(senderId).isPresent() || server.getOwnerId() == senderId)){
                    return server;
                }
            }
        }
        throw new RuntimeException();
    }

    @Override
    @PostConstruct
    public void addListenerToContext() {
        collectListenersService.addListenerToContext(this);
    }
}
