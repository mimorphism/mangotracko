package com.mimorphism.mangotracko.appuser.email;

public interface EmailSender {
    void send(String to, String email);
}