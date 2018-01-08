package com.example.demo.model;

public class ChatFromBot {
    protected String recipient;

    public ChatFromBot() {
    }

    public ChatFromBot(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
