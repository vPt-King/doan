package com.example.together.controller;

import com.example.together.dto.request.CommentRequest;
import com.example.together.dto.response.ApiResponse;
import com.example.together.service.CommentService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentService commentService;

    @PostMapping("/post-comment")
    ApiResponse<String> postComment(@RequestBody CommentRequest request)
    {
        return ApiResponse.<String>builder()
                .result(commentService.postComment(request))
                .build();
    }
}
