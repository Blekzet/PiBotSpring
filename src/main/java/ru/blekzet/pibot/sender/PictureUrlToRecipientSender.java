package ru.blekzet.pibot.sender;

import lombok.RequiredArgsConstructor;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PictureUrlToRecipientSender implements PictureSenderToRecipientInterface {

    private final DiscordApi discordApi;

    public void send(long recipientId, String author, String picture) throws NullPointerException {
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(author)
                .setImage(picture);
        discordApi.getUserById(recipientId).join().sendMessage(embed);
    }
}
