package com.telus.starter.springboot.model;

public class Greeting {

    private final long greetingId;
    private final String greetingContent;

    public Greeting(long id, String content) {
        this.greetingId = id;
        this.greetingContent = content;
    }

    public long getGreetingId() {
        return greetingId;
    }

    public String getContent() {
        return greetingContent;
    }
}
