package org.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.api.metadata.Message;
import org.api.metadata.RequestData;
import org.api.metadata.ResponseData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenAIGPT {
    private String modelName;
    private List<String> keys;
    private Random random;
    private OkHttpClient client;
    private ObjectMapper mapper;

    public OpenAIGPT(String modelName, String keysAPI) {
        this.modelName = modelName;
        this.random = new Random();
        this.client = new OkHttpClient();
        this.mapper = new ObjectMapper();
        this.keys = Collections.singletonList(keysAPI);
    }

    private String postProcess(String responseJson) throws IOException {
        ResponseData response = mapper.readValue(responseJson, ResponseData.class);
        return response.getChoices().get(0).getMessage().getContent();
    }

    public String call(List<Message> npcMessageHistory) {
        if (this.keys.isEmpty()) {
            System.err.println("No API keys available.");
            return "Error: API key not available.";
        }
        try {

            String currentKey = this.keys.get(random.nextInt(this.keys.size()));
            String json = mapper.writeValueAsString(new RequestData(this.modelName, npcMessageHistory, 0.6, 0.8, 0.6, 0.8, 1));
            RequestBody body = RequestBody.create(json, okhttp3.MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/chat/" + "/completions")
                    .header("Authorization", "Bearer " + currentKey)
                    .header("Content-Type", "application/json")

                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String responseBodyStr = response.body().string();
                if (response.isSuccessful()) {
                    return postProcess(responseBodyStr);
                } else {
                    System.err.println("Server returned error: " + response.code() + " " + response.message());
                    return "Server error: " + response.message() + " with body: " + responseBodyStr;
                }
            } catch (JsonProcessingException e) {
                System.err.println("JSON processing error: " + e.getMessage());
                return "JSON processing error: " + e.getMessage();
            }
        } catch (IOException e) {
            System.err.println("Failed to generate response from OpenAI: " + e.getMessage());
            return "Failed to generate response.";
        }
    }

}