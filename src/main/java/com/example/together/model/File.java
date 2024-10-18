package com.example.together.model;

import com.example.together.enumconfig.FileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String article_id;
    private String message_id;
    private String url;
    private String path;
    @Enumerated(EnumType.STRING)
    private FileType file_type;


    public File(String articleId, String message_id, String fileSaved, String filePath, FileType image) {
        this.article_id = articleId;
        this.message_id = message_id;
        this.url = fileSaved;
        this.path = filePath;
        this.file_type = image;
    }
}
