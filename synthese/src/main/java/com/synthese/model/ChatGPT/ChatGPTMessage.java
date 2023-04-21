package com.synthese.model.ChatGPT;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ChatGPTMessage {
    private String role;
    private String content;

}