package ru.blekzet.pibot.sender;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PictureUrlToOwnerSender implements PictureSenderInterface {

    private final DiscordApi discordApi;

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
