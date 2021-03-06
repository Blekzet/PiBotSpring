package ru.blekzet.pibot.listeners;

import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import ru.blekzet.pibot.sender.PictureSenderToRecipientInterface;
import ru.blekzet.pibot.service.CollectListenersService;

import java.util.List;

@RequiredArgsConstructor
public abstract class PictureAttachmentMessageListener implements MessageCreateListener {
    protected String pictureUrl;
    private final PictureSenderToRecipientInterface pictureUrlToRecipientSender;
    protected final CollectListenersService collectListenersService;

    public void execute(MessageCreateEvent messageCreateEvent, Server server, String pictureUrl){
        String authorNickname = messageCreateEvent.getMessage().getAuthor().getDisplayName();
        long recipientId = server.getOwnerId();
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
                "\n Команды бота на сервере:" +
                "\n 1) !pic {Url картинки без скобочек} или {вложить в сообщение картинку} отправить картинку владельцу сервера на проверку" +
                "\n 2) !tellmeajoke бот покажет случайный мем" +
                "\n Команды бота в приватном чате:"+
                "\n 1) !pic {Имя сервера без скобочек} {Url картинки без скобочек} или {вложить в сообщение картинку} отправить картинку владельцу сервера на проверку" +
                "\n 2) !tellmeajoke бот покажет случайный мем");
    }
    public abstract void addListenerToContext();
}
