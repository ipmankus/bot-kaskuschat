package com.example.demo.model;

public class ChatFromUser {
    private String from, fromPlain, to, stanza, body, client, timestamp, messageId;

    public ChatFromUser() {
    }

    public ChatFromUser(String from, String fromPlain, String to, String stanza, String body, String client, String timestamp, String messageId) {
        this.from = from;
        this.fromPlain = fromPlain;
        this.to = to;
        this.stanza = stanza;
        this.body = body;
        this.client = client;
        this.timestamp = timestamp;
        this.messageId = messageId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromPlain() {
        return fromPlain;
    }

    public void setFromPlain(String fromPlain) {
        this.fromPlain = fromPlain;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getStanza() {
        return stanza;
    }

    public void setStanza(String stanza) {
        this.stanza = stanza;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
