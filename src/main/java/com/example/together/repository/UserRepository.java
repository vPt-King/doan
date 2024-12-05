package com.example.together.repository;

import com.example.together.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = (SELECT r.user2_id FROM Relationship r WHERE r.user1_id = :userId AND r.status = 'REQUEST') " +
            "OR u.id = (SELECT r.user1_id FROM Relationship r WHERE r.user2_id = :userId AND r.status = 'REQUESTED')")
    List<User> getListSendUserFriendRequests(@Param("userId") String id);

    @Query("SELECT u FROM User u WHERE u.id = (SELECT r.user1_id FROM Relationship r WHERE r.user2_id = :userId AND r.status = 'REQUEST') " +
            "OR u.id = (SELECT r.user2_id FROM Relationship r WHERE r.user1_id = :userId AND r.status = 'REQUESTED')")
    List<User> getListSendedUserFriendRequests(@Param("userId") String id);

    @Query("SELECT u FROM User u WHERE u.id = (SELECT r.user2_id FROM Relationship r WHERE r.user1_id = :userId AND r.status = 'BLOCK') " +
            "OR u.id = (SELECT r.user1_id FROM Relationship r WHERE r.user2_id = :userId AND r.status = 'BLOCKED')")
    List<User> getListBlockUsers(@Param("userId") String id);

    @Query("SELECT u FROM User u WHERE u.id IN " +
            "(SELECT r.user2_id FROM Relationship r WHERE r.user1_id = :userId AND r.status = 'FRIEND') " +
            "OR u.id IN " +
            "(SELECT r.user1_id FROM Relationship r WHERE r.user2_id = :userId AND r.status = 'FRIEND')")
    List<User> getListFriends(@Param("userId") String userId);

    @Query(value = "SELECT * FROM user u WHERE u.username LIKE %:name% LIMIT 5", nativeQuery = true)
    List<User> searchPeopleOnKeyBoard(@Param("name") String keyword);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:name%")
    List<User> searchPeople(@Param("name") String keyword);

}
