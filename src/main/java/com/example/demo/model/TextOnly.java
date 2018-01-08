package com.example.demo.model;

public class TextOnly extends ChatFromBot {
    private String body, from, placeholder;

    public TextOnly() {
    }

    public TextOnly(String recipient, String body) {
        this.recipient = recipient;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public String toString() {
        return "TextOnly{" +
                "body='" + body + '\'' +
                ", from='" + from + '\'' +
                ", placeholder='" + placeholder + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }
}
