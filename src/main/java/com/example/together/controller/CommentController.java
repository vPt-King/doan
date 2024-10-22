package com.example.together.controller;

import com.example.together.dto.request.CommentEditRequest;
import com.example.together.dto.request.CommentRequest;
import com.example.together.dto.response.ApiResponse;
import com.example.together.service.CommentService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/edit-comment")
    ApiResponse<String> editComment(@RequestBody CommentEditRequest request)
    {
        return ApiResponse.<String>builder()
                .result(commentService.editComment(request))
                .build();
    }
}
