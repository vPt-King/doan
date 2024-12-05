package com.example.together.model;

import com.example.together.enumconfig.NotifyEnum;
import com.example.together.enumconfig.RelationshipStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotifyEnum status;
    private boolean isRead;
    private LocalDateTime createdAt;

}
