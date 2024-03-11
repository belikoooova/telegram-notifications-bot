package edu.java.bot.entity.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Chat {
    @Id
    private Long id;
    private ChatState state = ChatState.NONE;

    public Chat(Long id) {
        this.id = id;
    }
}
