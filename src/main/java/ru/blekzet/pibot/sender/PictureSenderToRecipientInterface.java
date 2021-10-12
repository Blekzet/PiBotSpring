package ru.blekzet.pibot.sender;

public interface PictureSenderToRecipientInterface {
    void send(long recipientId, String author, String picture);
}
