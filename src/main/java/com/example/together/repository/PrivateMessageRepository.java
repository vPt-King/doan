package com.example.together.repository;

import com.example.together.model.PrivateMessage;
import com.example.together.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateMessageRepository extends JpaRepository<PrivateMessage,Long> {
    List<PrivateMessage> findBySender(User sender);
    List<PrivateMessage> findBySenderAndReceiverAndContentContainingIgnoreCase(User sender, User receiver, String content);

    Page<PrivateMessage> findBySenderAndReceiver(User sender, User receiver, Pageable pageable);

    @Query(value = """
            SELECT pm.* 
            FROM private_message pm
            INNER JOIN (
                SELECT sender_id, MAX(sent_at) AS latest_sent_at
                FROM private_message
                WHERE receiver_id = :receiverId
                GROUP BY sender_id
            ) grouped_pm
            ON pm.sender_id = grouped_pm.sender_id AND pm.sent_at = grouped_pm.latest_sent_at
            ORDER BY pm.sent_at DESC
            """, nativeQuery = true)
    List<PrivateMessage> findMessagesByReceiverIdOrderBySentAt(@Param("receiverId") String receiverId);
}