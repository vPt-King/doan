package com.example.together.dto.response;

import com.example.together.enumconfig.NotifyEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
    Long id;
    String receiver;
    String sender;
    String message;
    NotifyEnum status;
    boolean read;
    LocalDateTime createdAt;
}
