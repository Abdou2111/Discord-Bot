package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OpenAIClient {
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private final OkHttpClient client;
    private final String apiKey;
    private final String model = "meta-llama/llama-3.2-3b-instruct:free";

    public OpenAIClient() {
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("OPENAI_API_KEY");
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        if (this.apiKey == null) {
            throw new IllegalStateException("API key not found in environment variables");
        }
    }

    private List<String> splitContent(String content, int maxLength) {
        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < content.length()) {
            int end = Math.min(content.length(), start + maxLength);
            chunks.add(content.substring(start, end));
            start = end;
        }
        return chunks;
    }

    public String getJsonResponse(String prompt) throws IOException {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                "{\"model\": \"" + model +"\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}], \"max_tokens\": 1000}"
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }

    public List<String> getCompletionContent(String prompt) throws IOException {
        try {
            String response = getJsonResponse(prompt);
            String content = JSONHandler.getCompletionFromJson(response);
            return splitContent(content, 2000);
        } catch (IOException e) {
            //e.printStackTrace();
            return List.of("An error occurred while processing your request.");
        }
    }
}