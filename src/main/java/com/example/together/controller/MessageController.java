package com.example.together.controller;

import com.example.together.dto.response.ApiResponse;
import com.example.together.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("message")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {
    MessageService messageService;

    @GetMapping("/user/{userId}")
    ApiResponse<List<Object>> getListSender(
            @PathVariable String userId
    ){
        return ApiResponse.<List<Object>>builder()
                .result(messageService.getSortedMessagesForUser(userId))
                .build();
    }
}