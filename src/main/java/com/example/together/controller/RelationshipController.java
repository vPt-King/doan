package com.example.together.controller;

import com.example.together.dto.request.NotificationRequest;
import com.example.together.dto.request.RelationshipRequest;
import com.example.together.dto.response.ApiResponse;
import com.example.together.dto.response.UserResponse;
import com.example.together.enumconfig.NotifyEnum;
import com.example.together.model.Notification;
import com.example.together.service.NotificationService;
import com.example.together.service.RelationshipService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RelationshipController {
    RelationshipService relationshipService;
    NotificationService notificationService;
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/check-relationship")
    ApiResponse<String> checkRelationship(@RequestBody RelationshipRequest request)
    {
        String mess;
        if(relationshipService.checkRelationship(request.getSenderId(),request.getReceiverId()) == null)
        {
            mess = "NONE";
        }
        else
        {
            mess = relationshipService.checkRelationship(request.getSenderId(),request.getReceiverId()).getStatus().name();
        }
        return ApiResponse.<String>builder()
                .result(mess)
                .build();
    }

    @PostMapping("/send-request")
    @Transactional
    ApiResponse<String> sendFriendRequest(@RequestBody RelationshipRequest request)
    {
        NotificationRequest notification = new NotificationRequest();
        notification.setSender(request.getSenderId());
        notification.setReceiver(request.getReceiverId());
        notification.setMessage("Bạn đã nhận được một lời mời kết bạn từ id:"+request.getSenderId());
        notification.setStatus(NotifyEnum.SEND);
        notificationService.createNotify(notification);
        simpMessagingTemplate.convertAndSend(
                "/topic/notifications/" + request.getReceiverId(), // Kênh WebSocket riêng của receiver
                notification
        );
        return ApiResponse.<String>builder()
                .result(relationshipService.sendFriendRequest(request.getSenderId(),request.getReceiverId()))
                .build();
    }

    @PostMapping("/accept-request")
    @Transactional
    public ApiResponse<String> acceptFriendRequest(@RequestBody RelationshipRequest request) {
        NotificationRequest notification = new NotificationRequest();
        notification.setSender(request.getSenderId());
        notification.setReceiver(request.getReceiverId());
        notification.setMessage("Bạn đã được chấp nhận lời mời kết bạn từ id:" + request.getSenderId());
        notification.setStatus(NotifyEnum.ACCEPT);

        // Thực hiện cập nhật trạng thái quan hệ
        String result = relationshipService.acceptFriendRequest(request.getSenderId(), request.getReceiverId());

        // Chỉ tạo thông báo nếu acceptFriendRequest không ném lỗi
        notificationService.createNotify(notification);

        // Gửi thông báo qua WebSocket
        simpMessagingTemplate.convertAndSend(
                "/topic/notifications/" + request.getReceiverId(),
                notification
        );

        return ApiResponse.<String>builder()
                .result(result)
                .build();
    }

    @PostMapping("/reject-request")
    ApiResponse<String> rejectFriendRequest(@RequestBody RelationshipRequest request){
        return ApiResponse.<String>builder()
                .result(relationshipService.rejectFriendRequest(request.getSenderId(),request.getReceiverId()))
                .build();
    }

    @PostMapping("/block")
    ApiResponse<String> blockRequest(@RequestBody RelationshipRequest request){
        return ApiResponse.<String>builder()
                .result(relationshipService.blockUser(request.getSenderId(),request.getReceiverId()))
                .build();
    }

    @PostMapping("/unfriend")
    ApiResponse<String> unFirend(@RequestBody RelationshipRequest request){
        return ApiResponse.<String>builder()
                .result(relationshipService.unfriend(request.getSenderId(),request.getReceiverId()))
                .build();
    }

}
