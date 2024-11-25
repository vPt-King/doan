package com.example.together.model;

import com.example.together.enumconfig.RelationshipStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String user1_id;
    private String user2_id;
    @Enumerated(EnumType.STRING)
    private RelationshipStatus status;

    @Enumerated(EnumType.STRING)
    private RelationshipStatus status_2;
    private LocalDateTime createdAt;
}
