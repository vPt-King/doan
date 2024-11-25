package com.example.together.controller;

import com.example.together.dto.request.RelationshipRequest;
import com.example.together.dto.response.ApiResponse;
import com.example.together.dto.response.UserResponse;
import com.example.together.model.Relationship;
import com.example.together.service.RelationshipService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RelationshipController {
    RelationshipService relationshipService;

    @PostMapping("/check-relationship")
    ApiResponse<String> checkRelationship(@RequestBody RelationshipRequest request)
    {
        String mess;
        Relationship a = relationshipService.checkRelationship(request.getSenderId(),request.getReceiverId());
        if(a == null)
        {
            mess = "NONE";
        }
        else
        {
            if(a.getUser1_id().equals(request.getSenderId()))
            {
                mess = a.getStatus().toString();
            }
            else mess = a.getStatus_2().toString();
        }
        return ApiResponse.<String>builder()
                .result(mess)
                .build();
    }

    @PostMapping("/send-request")
    ApiResponse<String> sendFriendRequest(@RequestBody RelationshipRequest request)
    {
        return ApiResponse.<String>builder()
                .result(relationshipService.sendFriendRequest(request.getSenderId(),request.getReceiverId()))
                .build();
    }

    @PostMapping("/accept-request")
    ApiResponse<String> acceptFriendRequest(@RequestBody RelationshipRequest request){
        return ApiResponse.<String>builder()
                .result(relationshipService.acceptFriendRequest(request.getSenderId(),request.getReceiverId()))
                .build();
    }

    @PostMapping("/reject-request")
    ApiResponse<String> rejectFriendRequest(@RequestBody RelationshipRequest request){
        return ApiResponse.<String>builder()
                .result(relationshipService.rejectFriendRequest(request.getSenderId(),request.getReceiverId()))
                .build();
    }

    @PostMapping("/block")
    ApiResponse<String> blockRequest(@RequestBody RelationshipRequest request){
        return ApiResponse.<String>builder()
                .result(relationshipService.blockUser(request.getSenderId(),request.getReceiverId()))
                .build();
    }

    @PostMapping("/unfriend")
    ApiResponse<String> unFriend(@RequestBody RelationshipRequest request){
        return ApiResponse.<String>builder()
                .result(relationshipService.unfriend(request.getSenderId(),request.getReceiverId()))
                .build();
    }

    @PostMapping("/unblock")
    ApiResponse<String> unBlock(@RequestBody RelationshipRequest request){
        return ApiResponse.<String>builder()
                .result(relationshipService.unblock(request.getSenderId(),request.getReceiverId()))
                .build();
    }

}
