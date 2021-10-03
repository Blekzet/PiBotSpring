package ru.blekzet.pibot.sender;

public interface PictureSenderInterface{
    void send(long recipientId, String author, String picture);
}
