package com.example.together.dto.request;

import com.example.together.enumconfig.NotifyEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private String receiver;
    private String sender;
    private String message;
    private boolean isRead;
    private NotifyEnum status;
    private LocalDateTime createdAt;
}
