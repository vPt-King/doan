package com.example.together.model;

import com.example.together.enumconfig.AccessStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String user_id;
    private String content;
    private LocalDateTime created_at;
    @Enumerated(EnumType.STRING)
    private AccessStatus access;
    private String caption;
}
