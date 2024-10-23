package com.example.together.dto.response;

import com.example.together.enumconfig.AccessStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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

    public ArticleResponse(String id, String user_id, String username, String user_avatar, String content, AccessStatus access_status, LocalDateTime time) {
        this.id = id;
        this.user_id = user_id;
        this.username = username;
        this.user_avatar = user_avatar;
        this.content = content;
        this.access_status = access_status;
        this.time = time;
    }
}
