package com.example.together.repository;

import com.example.together.enumconfig.RelationshipStatus;
import com.example.together.model.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    @Query("SELECT r FROM Relationship r WHERE (r.user1_id = :user1_id AND r.user2_id = :user2_id AND r.status = :status) OR (r.user1_id = :user2_id AND r.user2_id = :user1_id AND r.status = :status)")
    Optional<Relationship> findByUser1_idAndUser2_idAndStatus(@Param("user1_id") String user1_id, @Param("user2_id") String user2_id, @Param("status") RelationshipStatus status);


    @Query("SELECT r FROM Relationship r WHERE (r.user1_id = :user1_id AND r.user2_id = :user2_id) OR (r.user1_id = :user2_id AND r.user2_id = :user1_id)")
    Optional<Relationship> findRelationshipByUserIds(@Param("user1_id") String user1Id, @Param("user2_id") String user2Id);
    @Query("SELECT r FROM Relationship r WHERE (r.user1_id = :id AND r.status = 'REQUEST')")
    List<Relationship> findSendFriendRequests(@Param("id") String id);
}
