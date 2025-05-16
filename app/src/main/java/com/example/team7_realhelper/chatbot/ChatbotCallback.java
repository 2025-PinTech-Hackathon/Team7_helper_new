package com.example.team7_realhelper.chatbot;

public interface ChatbotCallback {
    void onResponse(String response);
    void onError(String error);
}
