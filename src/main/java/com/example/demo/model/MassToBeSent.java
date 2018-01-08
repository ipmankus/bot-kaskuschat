package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MassToBeSent {
    private String id;
    @JsonProperty("sendList")
    private List<ChatFromBot> responses;

    public MassToBeSent() {
    }

    public MassToBeSent(String id, List<ChatFromBot> responses) {
        this.id = id;
        this.responses = responses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ChatFromBot> getResponses() {
        return responses;
    }

    public void setResponses(List<ChatFromBot> responses) {
        this.responses = responses;
    }

    @Override
    public String toString() {
        return "MassToBeSent{" +
                "id='" + id + '\'' +
                ", responses=" + responses +
                '}';
    }
}
