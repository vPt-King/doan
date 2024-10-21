package com.example.together.dto.request;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikeArticleRequest {
    String article_id;
    String user_id;
    Integer liked;
}
