package com.example.together.repository;

import com.example.together.model.GroupChat;
import com.example.together.model.GroupMessage;
import com.example.together.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMessageRepository extends JpaRepository<GroupMessage,Long> {

    Page<GroupMessage> findByGroup(GroupChat groupChat, Pageable pageable);
    @Query(value = """
            SELECT gm.* 
            FROM group_message gm
            INNER JOIN (
                SELECT group_id, MAX(sent_at) AS latest_sent_at
                FROM group_message
                WHERE group_id IN (
                    SELECT ug.group_id 
                    FROM user_group_chat ug 
                    WHERE ug.user_id = :userId
                )
                GROUP BY group_id
            ) grouped_gm
            ON gm.group_id = grouped_gm.group_id AND gm.sent_at = grouped_gm.latest_sent_at
            ORDER BY gm.sent_at DESC
            """, nativeQuery = true)
    List<GroupMessage> findMessagesByGroupsForUser(@Param("userId") String userId);
}