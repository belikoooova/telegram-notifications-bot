package edu.java.bot.entity.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chat_state")
public class Chat {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private ChatState state = ChatState.NONE;

    public Chat(Long id) {
        this.id = id;
    }
}
