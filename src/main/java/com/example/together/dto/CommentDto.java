package com.example.together.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String comment_id;
    private String user_id;
    private String username;
    private String avatar_path;
    private String content;
    private LocalDateTime created_at;
    private String parent_comment_id;
    private List<CommentDto> child_comments;
}
