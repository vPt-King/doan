
package com.example.together.service;

import com.example.together.dto.request.NotificationRequest;
import com.example.together.dto.response.GroupChatResponse;
import com.example.together.dto.response.NotificationResponse;
import com.example.together.exception.AppException;
import com.example.together.exception.ErrorCode;
import com.example.together.mapper.NotificationMapper;
import com.example.together.model.Notification;
import com.example.together.model.User;
import com.example.together.repository.NotificationRepository;
import com.example.together.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {
    NotificationRepository notificationRepository;
    NotificationMapper notificationMapper;
    UserRepository userRepository;

    public NotificationResponse createNotify(NotificationRequest request){
        Notification notification = notificationMapper.toNotification(request);
        User sender = userRepository.findById(request.getSender())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_USER));
        User receiver = userRepository.findById(request.getReceiver())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_USER));

        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        return notificationMapper.toNotificationResponse(notificationRepository.save(notification));
    }


    public List<NotificationResponse> getNotification(){
        return notificationRepository.findAll().stream().map(notificationMapper::toNotificationResponse).toList();
    }

    public String markAsRead(Long id){
        Notification notification=notificationRepository.findById(id).orElseThrow(()
                -> new AppException(ErrorCode.INVALID_NOTIFY));
        notification.setRead(true);
        notificationRepository.save(notification);
        return "Mark As Read";
    }

    public List<NotificationResponse> getNotificationByReceiverId(String id){
        return notificationRepository.findByReceiver_IdOrderByCreatedAtDesc(id).stream().map(notificationMapper::toNotificationResponse).toList();
    }
    public List<NotificationResponse> getNotificationByReceiverIdAndReadFalse(String id){
        return notificationRepository.findUnreadNotificationsByReceiverId(id).stream().map(notificationMapper::toNotificationResponse).toList();
    }

    public String markAsReadAll(String id){
        List<Notification> list=notificationRepository.findUnreadNotificationsByReceiverId(id);
        for(int i=0;i<list.size();i++){
            list.get(i).setRead(true);
            notificationRepository.save(list.get(i));
        }
        return "Mark As Read All";
    }
}
