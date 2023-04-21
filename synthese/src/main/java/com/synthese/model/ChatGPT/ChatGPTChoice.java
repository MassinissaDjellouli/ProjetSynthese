package com.synthese.model.ChatGPT;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChatGPTChoice {
    private ChatGPTMessage message;
    private String finish_reason;
    private int index;
}