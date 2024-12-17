package com.example.together.mapper;

import com.example.together.dto.request.NotificationRequest;
import com.example.together.dto.response.NotificationResponse;
import com.example.together.model.Notification;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Amazon.com Inc.)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationResponse toNotificationResponse(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationResponse.NotificationResponseBuilder notificationResponse = NotificationResponse.builder();

        notificationResponse.read( notification.isRead() );
        notificationResponse.id( notification.getId() );
        notificationResponse.message( notification.getMessage() );
        notificationResponse.status( notification.getStatus() );
        notificationResponse.createdAt( notification.getCreatedAt() );

        notificationResponse.sender( notification.getSender().getId() );
        notificationResponse.receiver( notification.getReceiver().getId() );

        return notificationResponse.build();
    }

    @Override
    public Notification toNotification(NotificationRequest request) {
        if ( request == null ) {
            return null;
        }

        Notification notification = new Notification();

        notification.setMessage( request.getMessage() );
        notification.setStatus( request.getStatus() );
        notification.setRead( request.isRead() );
        notification.setCreatedAt( request.getCreatedAt() );

        return notification;
    }
}
