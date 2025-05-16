package com.example.team7_realhelper.chatbot;

public interface VoiceListener {
    void onSpeechResult(String result);
    void onSpeechError(String errorMessage);
}
