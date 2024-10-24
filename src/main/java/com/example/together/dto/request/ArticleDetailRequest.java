package com.example.together.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ArticleDetailRequest {
    private String article_id;
    private String owner_id;
}
