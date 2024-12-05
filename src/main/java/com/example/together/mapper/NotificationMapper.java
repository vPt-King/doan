package com.example.together.mapper;

import com.example.together.dto.request.NotificationRequest;
import com.example.together.dto.response.NotificationResponse;
import com.example.together.dto.response.PrivateMessageResponse;
import com.example.together.model.Notification;
import com.example.together.model.PrivateMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "sender", expression = "java(notification.getSender().getId())")
    @Mapping(target = "receiver", expression = "java(notification.getReceiver().getId())")
    @Mapping(target = "read", source = "read")
    NotificationResponse toNotificationResponse(Notification notification);

    @Mapping(target = "sender", ignore = true) // Gán thủ công trong service
    @Mapping(target = "receiver", ignore = true) // Gán thủ công trong service
    @Mapping(target = "id", ignore = true) // ID sẽ được tự động tạo bởi JPA
    Notification toNotification(NotificationRequest request);
}
