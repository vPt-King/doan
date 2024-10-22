package com.example.together.dto.request;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentEditRequest {
    private String article_id;
    private String content;
    private String comment_id;
}
