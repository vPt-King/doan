package com.example.together.controller;

import com.example.together.dto.request.NotificationRequest;
import com.example.together.dto.response.ApiResponse;
import com.example.together.dto.response.NotificationResponse;
import com.example.together.model.Notification;
import com.example.together.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    NotificationService notificationService;
    @PostMapping
    public ApiResponse<NotificationResponse> createNotification(@RequestBody NotificationRequest notificationRequest){
        return ApiResponse.<NotificationResponse>builder()
                .result(notificationService.createNotify(notificationRequest))
                .build();
    }

    @GetMapping
    public ApiResponse<List<NotificationResponse>> getAllNotification(){
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.getNotification())
                .build();
    }

    @PostMapping("/mark/{id}")
    public ApiResponse<String> markAsRead(@PathVariable Long id){
        return ApiResponse.<String>builder()
                .result(notificationService.markAsRead(id))
                .build();
    }

    @GetMapping("/all/{id}")
    public ApiResponse<List<NotificationResponse>> getAllNotificationByReceiverId(@PathVariable String id){
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.getNotificationByReceiverId(id))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<List<NotificationResponse>> getAllNotificationByReceiverIdAndReadFalse(@PathVariable String id){
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.getNotificationByReceiverIdAndReadFalse(id))
                .build();
    }
}