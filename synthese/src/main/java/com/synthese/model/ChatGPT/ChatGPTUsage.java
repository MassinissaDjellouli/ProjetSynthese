package com.synthese.model.ChatGPT;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatGPTUsage {
    int prompt_tokens;
    int completion_tokens;
    int total_tokens;
}