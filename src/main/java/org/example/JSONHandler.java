package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.*;

public class JSONHandler {
    private static final Gson gson = new Gson();

    public static String getCompletionFromJson(String jsonString) {
        JsonReader reader = new JsonReader(new StringReader(jsonString));
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
        return jsonObject.get("choices").getAsJsonArray().get(0).getAsJsonObject()
                .get("message").getAsJsonObject()
                .get("content").getAsString();
    }

}