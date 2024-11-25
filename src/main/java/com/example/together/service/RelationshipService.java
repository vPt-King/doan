package com.example.together.service;

import com.example.together.dto.response.UserResponse;
import com.example.together.enumconfig.RelationshipStatus;
import com.example.together.model.Relationship;
import com.example.together.repository.RelationshipRepository;
import com.example.together.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RelationshipService {
    UserRepository userRepository;
    final RelationshipRepository relationshipRepository;

    public Relationship checkRelationship(String senderId, String receiverId)
    {
        Optional<Relationship> relationshipOptional = relationshipRepository.findRelationshipByUserIds(senderId, receiverId);
        if(relationshipOptional.isPresent())
        {
            return relationshipOptional.get();
        }
        return null;
    }

    public String sendFriendRequest(String senderId, String receiverId)
    {
        Relationship relationship = checkRelationship(senderId, receiverId);
        if(relationship != null)
        {
            if(relationship.getUser1_id().equals(senderId))
            {
                relationship.setStatus(RelationshipStatus.REQUEST);
                relationship.setStatus_2(RelationshipStatus.REQUESTED);
            }
            else{
                relationship.setStatus(RelationshipStatus.REQUESTED);
                relationship.setStatus_2(RelationshipStatus.REQUEST);
            }
            relationship.setCreatedAt(LocalDateTime.now());
            relationshipRepository.save(relationship);
        }
        else
        {
            Relationship relationship1 = Relationship.builder()
                    .user1_id(senderId)
                    .user2_id(receiverId)
                    .status(RelationshipStatus.REQUEST)
                    .status_2(RelationshipStatus.REQUESTED)
                    .createdAt(LocalDateTime.now())
                    .build();
            relationshipRepository.save(relationship1);
        }

        return "Đã gửi kết bạn";
    }

    public String acceptFriendRequest(String senderId, String receiverId) {
        Relationship relationship = relationshipRepository.findRelationshipByUserIds(senderId, receiverId)
                .orElseThrow(() -> new RuntimeException("Không có lời mời kết bạn từ sender ID"));
        relationship.setStatus(RelationshipStatus.FRIEND);
        relationship.setStatus_2(RelationshipStatus.FRIEND);
        relationship.setCreatedAt(LocalDateTime.now());
        relationshipRepository.save(relationship);
        return "Chấp nhận kết bạn";
    }

    public String rejectFriendRequest(String senderId, String receiverId) {
        Relationship relationship = relationshipRepository.findRelationshipByUserIds(senderId, receiverId)
                .orElseThrow(() -> new RuntimeException("Không có lời mời kết bạn từ sender ID"));
        relationshipRepository.deleteById(relationship.getId());
        return "Đã từ chối kết bạn";
    }

    public String blockUser(String senderId,String blockUserId){
        Relationship relationship = checkRelationship(senderId, blockUserId);
        if(relationship != null)
        {
            if(relationship.getUser1_id().equals(senderId)){
                relationship.setStatus(RelationshipStatus.BLOCK);
                relationship.setStatus_2(RelationshipStatus.BLOCKED);
            }
            else{
                relationship.setStatus(RelationshipStatus.BLOCKED);
                relationship.setStatus_2(RelationshipStatus.BLOCK);
            }
            relationship.setCreatedAt(LocalDateTime.now());
            relationshipRepository.save(relationship);
        }
        else {
            Relationship relationship1 = Relationship.builder()
                    .user1_id(senderId)
                    .user2_id(blockUserId)
                    .status(RelationshipStatus.BLOCK)
                    .status_2(RelationshipStatus.BLOCKED)
                    .createdAt(LocalDateTime.now())
                    .build();
            relationshipRepository.save(relationship1);
        }
        return "Đã chặn người dùng";
    }

    public String unfriend(String senderId, String receiverId){
        Relationship relationship = relationshipRepository.findRelationshipByUserIds(senderId, receiverId)
                .orElseThrow(() -> new RuntimeException("Relationship not found"));
        relationshipRepository.deleteById(relationship.getId());
        return "Hủy kết bạn thành công";
    }

    public String unblock(String senderId, String receiverId){
        Relationship relationship = relationshipRepository.findRelationshipByUserIds(senderId, receiverId)
                .orElseThrow(() -> new RuntimeException("Relationship not found"));
        relationshipRepository.deleteById(relationship.getId());
        return "Hủy block thành công";
    }


}
