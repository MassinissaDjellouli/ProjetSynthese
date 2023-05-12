package com.synthese.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synthese.enums.GenerationCycle;
import com.synthese.exceptions.ChatGPTException;
import com.synthese.model.ChatGPT.ChatGPTMessage;
import com.synthese.model.ChatGPT.ChatGPTResponse;
import com.synthese.model.GenerationFailureInfo;
import com.synthese.repository.GenerationFailureInfoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatGPTService {


    private final String API_KEY;
    private static final ObjectMapper mapper = new ObjectMapper();
    private final GenerationFailureInfoRepository generationFailureInfoRepository;

    public ChatGPTService(@Value("${openai.api.key}") String API_KEY, GenerationFailureInfoRepository generationFailureInfoRepository) {
        this.generationFailureInfoRepository = generationFailureInfoRepository;
        this.API_KEY = API_KEY;
    }


    //Inspiration: https://gist.github.com/gantoin/190684c344bb70e5c5f9f2339c7be6ed
    private void createGenerationFailureInfo(String generationId, GenerationCycle cycle, int correctionCount, String message) {
        generationFailureInfoRepository.save(new GenerationFailureInfo(generationId, cycle, correctionCount, message));
    }

    public ChatGPTResponse chatGPT(List<ChatGPTMessage> messages, String generationId) throws ChatGPTException {
        try {
            String url = "https://api.openai.com/v1/chat/completions";
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + API_KEY);
            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-3.5-turbo");
            body.put("messages", messages);
            body.put("temperature", 0.5f);
            System.out.println(mapper.writeValueAsString(body));
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(mapper.writeValueAsString(body).getBytes());
            os.flush();
            os.close();
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("OK");
            } else {
                System.out.println("ERROR");
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return mapper.readValue(response.toString(), ChatGPTResponse.class);
        } catch (IOException e) {
            createGenerationFailureInfo(generationId, GenerationCycle.GENERATION, 0, "Erreur lors de la communication avec l'API de chatGPT");
            throw new ChatGPTException();
        }
    }

    public List<ChatGPTMessage> generateMessages(String prompt) {
        String sysPrompt = "You are a system that responds only in json files while following given constraints.";
        ChatGPTMessage systemMessage = ChatGPTMessage.builder()
                .role("system")
                .content(sysPrompt)
                .build();
        ChatGPTMessage userMessage = ChatGPTMessage.builder()
                .role("user")
                .content(prompt)
                .build();
        return List.of(systemMessage, userMessage);
    }
}