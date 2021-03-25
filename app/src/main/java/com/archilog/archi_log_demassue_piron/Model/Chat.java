package com.archilog.archi_log_demassue_piron.Model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;

    public Chat() {

    }

    public Chat(String sender, String receiver, String message) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }
}
