package com.synthese.model.ChatGPT;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatGPTResponse {
    String id;
    String object;
    int created;
    String model;
    ChatGPTUsage usage;
    List<ChatGPTChoice> choices;

}