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

    @Query("SELECT u FROM User u, Relationship r WHERE u.id = r.user2_id and r.user1_id=:userId and r.status = 'REQUEST'")
    List<User> getListSendUser_1FriendRequests(@Param("userId") String id);

    @Query("SELECT u FROM User u, Relationship r WHERE u.id = r.user1_id and r.user2_id=:userId and r.status_2 = 'REQUEST'")
    List<User> getListSendUser_2FriendRequests(@Param("userId") String id);

    @Query("SELECT u FROM User u, Relationship r WHERE u.id = r.user2_id and r.user1_id=:userId and r.status = 'REQUESTED'")
    List<User> getListSendedUser_1FriendRequests(@Param("userId") String id);

    @Query("SELECT u FROM User u, Relationship r WHERE u.id = r.user1_id and r.user2_id=:userId and r.status_2 = 'REQUESTED'")
    List<User> getListSendedUser_2FriendRequests(@Param("userId") String id);

    @Query("SELECT u FROM User u, Relationship r WHERE u.id = r.user2_id and r.user1_id=:userId and r.status = 'BLOCK'")
    List<User> getListBlockUsers_1(@Param("userId") String id);

    @Query("SELECT u FROM User u, Relationship r WHERE u.id = r.user1_id and r.user2_id=:userId and r.status = 'BLOCKED'")
    List<User> getListBlockUsers_2(@Param("userId") String id);

    @Query("SELECT u FROM User u, Relationship r WHERE u.id = r.user2_id and r.user1_id=:userId and r.status = 'FRIEND'")
    List<User> getListFriend_1(@Param("userId") String id);

    @Query("SELECT u FROM User u, Relationship r WHERE u.id = r.user1_id and r.user2_id=:userId and r.status = 'FRIEND'")
    List<User> getListFriend_2(@Param("userId") String id);

    @Query(value = "SELECT * FROM user u WHERE u.username LIKE %:name% LIMIT 5", nativeQuery = true)
    List<User> searchPeopleOnKeyBoard(@Param("name") String keyword);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:name%")
    List<User> searchPeople(@Param("name") String keyword);

}
