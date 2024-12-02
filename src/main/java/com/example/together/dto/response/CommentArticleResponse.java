package com.example.together.dto.response;


import com.example.together.dto.CommentDto;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentArticleResponse {
    Integer total_comment;
    List<CommentDto> res;
}
