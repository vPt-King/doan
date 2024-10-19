package com.example.together.dto.response;

import com.example.together.enumconfig.AccessStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleResponse {
    String id;
    String user_id;
    String username;
    String user_avatar;
    String content;
    List<String> image_article;
    String video_article;
    Integer number_reaction;
    AccessStatus access_status;
    LocalDateTime time;
    Integer number_comment;
}
