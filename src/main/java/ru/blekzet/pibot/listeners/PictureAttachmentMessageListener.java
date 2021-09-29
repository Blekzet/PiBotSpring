package ru.blekzet.pibot.listeners;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.blekzet.pibot.sender.PictureSenderInterface;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Getter
@NoArgsConstructor
@Component
public class PictureAttachmentMessageListener implements MessageCreateListener {

    private String authorNickname;
    private URL pictureUrl;

    private PictureSenderInterface pictureUrlToOwnerSender;

    @Autowired
    public PictureAttachmentMessageListener(PictureSenderInterface pictureUrlToOwnerSender){
        this.pictureUrlToOwnerSender = pictureUrlToOwnerSender;
    }
    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if(messageCreateEvent.getMessageContent().startsWith("!pic")){
            authorNickname = messageCreateEvent.getMessage().getAuthor().getDisplayName();
            try {
                if(messageCreateEvent.getMessageAttachments().isEmpty()) {
                    pictureUrl = new URL(messageCreateEvent.getMessageContent().substring(4));
                } else {
                    pictureAsAttachmentHandler(messageCreateEvent);
                }
                pictureUrlToOwnerSender.send(messageCreateEvent.getServer(), authorNickname, pictureUrl);
                messageCreateEvent.getChannel().sendMessage("Картинка принята на рассмотрение!");
            } catch (NullPointerException | MalformedURLException exception){
                messageCreateEvent.getChannel().sendMessage("Неправильный URL изображения");
            }
        } else if (!messageCreateEvent.getMessageAuthor().isYourself()){
            messageCreateEvent.getChannel().sendMessage("Введите верную комманду " +
                    "\n 1) !pic {Url картинки без скобочек} " +
                    "\n 2) !pic {вложить в сообщение картинку}");
        }
    }

    private void pictureAsAttachmentHandler(MessageCreateEvent messageCreateEvent){
        List<MessageAttachment> attachments;
        attachments = messageCreateEvent.getMessageAttachments();

        for(MessageAttachment attachment: attachments){
            if(attachment.isImage()){
                pictureUrl = attachment.getUrl();
            }
        }
    }
}
