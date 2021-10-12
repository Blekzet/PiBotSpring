package ru.blekzet.pibot.listeners.direct;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import ru.blekzet.pibot.listeners.PictureAttachmentMessageListener;
import ru.blekzet.pibot.sender.PictureSenderToRecipientInterface;
import ru.blekzet.pibot.service.CollectListenersService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class PictureAttachmentOnPrivateMessageListener extends PictureAttachmentMessageListener {

    public PictureAttachmentOnPrivateMessageListener(PictureSenderToRecipientInterface pictureUrlToRecipientSender, CollectListenersService collectListenersService) {
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
            Server confirmedServer;
            try {
                confirmedServer = confirmEnteredServerNameAndGetServerRecipientId(messageCreateEvent, enteredServerName);
            } catch (Exception e) {
                messageCreateEvent.getChannel().sendMessage(e.getMessage());
                return;
            }

            execute(messageCreateEvent, confirmedServer, pictureUrl);

        }
    }

    private String buildServerNameByVariant(String[] separatedCommand, int variant) {
        StringBuilder serverNameBuilder = new StringBuilder();
        for(int i = 1; i <= separatedCommand.length-variant; i++){
            serverNameBuilder.append(separatedCommand[i]);
        }
        return serverNameBuilder.toString();
    }

    private Server confirmEnteredServerNameAndGetServerRecipientId(MessageCreateEvent messageCreateEvent, String enteredServerName) throws Exception{
        if(messageCreateEvent.getMessageAuthor().asUser().isPresent()) {
            List<Server> userServers = new ArrayList<>(messageCreateEvent.getApi().getServers());
            long senderId = messageCreateEvent.getMessageAuthor().getId();
            for(Server server: userServers){
                if(server.getName().replace(" ", "").equals(enteredServerName) && (server.getMemberById(senderId).isPresent() || server.getOwnerId() == senderId)){
                    return server;
                }
            }
        }
        throw new Exception("Бот не член, введенного вами, сервера");
    }

    @Override
    @PostConstruct
    public void addListenerToContext() {
        collectListenersService.addListenerToContext(this);
    }
}
