package ru.blekzet.pibot.sender;

import java.net.URL;

public interface PictureSenderInterface{
    void send(long recipientId, String author, URL picture);
}
