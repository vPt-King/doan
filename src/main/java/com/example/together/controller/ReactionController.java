package com.example.together.controller;

import com.example.together.dto.request.LikeArticleRequest;
import com.example.together.dto.response.ApiResponse;
import com.example.together.service.ReactionService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reaction")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReactionController {
    ReactionService reactionService;
    @PostMapping("/like-article")
    ApiResponse<String> likeArticle(@RequestBody LikeArticleRequest request) {
        return ApiResponse.<String>builder()
                .result(reactionService.likeArticle(request))
                .build();
    }
}
