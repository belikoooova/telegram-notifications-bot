package edu.java.bot.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    private Long id;
    private ChatState state = ChatState.NONE;

    public Chat(Long id) {
        this.id = id;
    }
}
