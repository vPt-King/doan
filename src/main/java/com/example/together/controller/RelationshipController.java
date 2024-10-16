package com.example.together.controller;

import com.example.together.dto.request.RelationshipRequest;
import com.example.together.dto.response.ApiResponse;
import com.example.together.dto.response.UserResponse;
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
        if(relationshipService.checkRelationship(request.getSenderId(),request.getReceiverId()) == null)
        {
            mess = "NONE";
        }
        else
        {
            mess = relationshipService.checkRelationship(request.getSenderId(),request.getReceiverId()).getStatus().name();
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
    ApiResponse<String> acceptFirendRequest(@RequestBody RelationshipRequest request){
        return ApiResponse.<String>builder()
                .result(relationshipService.acceptFriendRequest(request.getSenderId(),request.getReceiverId()))
                .build();
    }

    @PostMapping("/reject-request")
    ApiResponse<String> rejectFirendRequest(@RequestBody RelationshipRequest request){
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
    ApiResponse<String> unFirend(@RequestBody RelationshipRequest request){
        return ApiResponse.<String>builder()
                .result(relationshipService.unfriend(request.getSenderId(),request.getReceiverId()))
                .build();
    }



}
