package com.example.team7_realhelper.chatbot;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.UUID;

import com.google.auth.oauth2.GoogleCredentials;
import okhttp3.*;
import com.google.gson.*;

public class ChatbotService {
    public static String getAccessToken(Context context) throws IOException {
        InputStream stream = context.getAssets().open("chatbot_key.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/cloud-platform"));
        credentials.refreshIfExpired();
        return credentials.getAccessToken().getTokenValue();
    }


    public static void sendMessageToChatbot(Context context, String messageText) {
        new Thread(() -> {
            try {
                String accessToken = getAccessToken(context); // 위에서 만든 메서드

                OkHttpClient client = new OkHttpClient();

                String sessionId = UUID.randomUUID().toString(); // 사용자 고유 식별자나 랜덤 UUID 사용 가능
                String projectId = "helper-chatbot-sysx";

                String URL = "https://dialogflow.googleapis.com/v2/projects/" + projectId +
                        "/agent/sessions/" + sessionId + ":detectIntent";

                JsonObject queryInput = new JsonObject();
                JsonObject text = new JsonObject();
                text.addProperty("text", messageText);
                text.addProperty("languageCode", "ko");

                queryInput.add("text", text);

                JsonObject body = new JsonObject();
                body.add("queryInput", queryInput);

                Request request = new Request.Builder()
                        .url(URL)
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .post(RequestBody.create(body.toString(), MediaType.parse("application/json")))
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("DF", "Response: " + responseBody);
                } else {
                    Log.e("DF", "Error: " + response.code());
                }

            } catch (Exception e) {
                Log.e("DF", "Ep: " + e.toString());
            }
        }).start();
    }

}

