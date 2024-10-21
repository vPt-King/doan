package com.example.together.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    String article_id;
    String content;
    String user_id;
    String parent_comment_id;
}
