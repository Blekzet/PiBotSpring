package ru.blekzet.pibot.listeners;

import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import ru.blekzet.pibot.sender.PictureSenderInterface;
import ru.blekzet.pibot.service.CollectListenersService;

import java.util.List;

@RequiredArgsConstructor
public abstract class PictureAttachmentCommandListener implements MessageCreateListener {
    protected String pictureUrl;
    private final PictureSenderInterface pictureUrlToRecipientSender;
    protected final CollectListenersService collectListenersService;

    public void execute(MessageCreateEvent messageCreateEvent, long recipientId, long ServerId, String pictureUrl){
        String authorNickname = messageCreateEvent.getMessage().getAuthor().getDisplayName();
        try {
            this.pictureUrl = pictureUrl;
            pictureUrlToRecipientSender.send(recipientId, authorNickname, pictureUrl);
            messageCreateEvent.getChannel().sendMessage("Картинка принята на рассмотрение!");
        } catch (NullPointerException exception){
            errorMessage(messageCreateEvent);
        }
    }
    protected String pictureAsAttachmentHandler(MessageCreateEvent messageCreateEvent){
        List<MessageAttachment> attachments;
        attachments = messageCreateEvent.getMessageAttachments();

        for(MessageAttachment attachment: attachments){
            if(attachment.isImage()){
                pictureUrl = attachment.getUrl().toString();
            }
        }
        return pictureUrl;
    }
    protected void errorMessage(MessageCreateEvent messageCreateEvent){
        messageCreateEvent.getChannel().sendMessage("Введите верную комманду " +
                "\n 1) !pic {Url картинки без скобочек} " +
                "\n 2) !pic {вложить в сообщение картинку}");
    }
    public abstract void addListenerToContext();
}
