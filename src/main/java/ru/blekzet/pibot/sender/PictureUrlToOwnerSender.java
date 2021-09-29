package ru.blekzet.pibot.sender;

import lombok.NoArgsConstructor;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.URL;
import java.util.Optional;

@Component
@NoArgsConstructor
public class PictureUrlToOwnerSender implements PictureSenderInterface {

    private DiscordApi discordApi;

    @Autowired
    PictureUrlToOwnerSender(DiscordApi discordApi){
        this.discordApi = discordApi;
    }

    public void send(Optional<Server> server,String author, URL picture) throws NullPointerException {
        if(server.isPresent()){
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor(author)
                        .setImage(picture.toString());
                long ownerId = server.get().getOwnerId();
                discordApi.getUserById(ownerId).join().sendMessage(embed);
        }
    }
}
