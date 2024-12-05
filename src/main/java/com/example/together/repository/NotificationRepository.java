package com.example.together.repository;

import com.example.together.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByReceiver_IdOrderByCreatedAtDesc(String receiverId);
    // Lấy tất cả thông báo theo id người nhận và chưa đọc
    @Query("SELECT n FROM Notification n WHERE n.receiver.id = :receiverId AND n.isRead = false")
    List<Notification> findUnreadNotificationsByReceiverId(@Param("receiverId") String receiverId);
}
