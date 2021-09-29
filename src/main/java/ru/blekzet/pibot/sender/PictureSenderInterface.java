package ru.blekzet.pibot.sender;

import org.javacord.api.entity.server.Server;
import java.net.URL;
import java.util.Optional;

public interface PictureSenderInterface{
    void send(Optional<Server> server, String author, URL picture);
}
