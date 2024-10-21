package com.example.together.repository;

import com.example.together.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FileRepository extends JpaRepository<File, String> {

    @Query("SELECT a.url FROM File a WHERE a.article_id = :id and a.file_type='IMAGE'")
    List<String> findUrlByArticle_id(@Param("id") String id);

    @Query("SELECT a.url FROM File a WHERE a.article_id = :id and a.file_type='VIDEO'")
    String findVideo_articleByArticle_id(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("DELETE FROM File f WHERE f.article_id = :articleId AND f.file_type = 'IMAGE'")
    void deleteImagesByArticleId(@Param("articleId") String articleId);

    @Modifying
    @Transactional
    @Query("DELETE FROM File f WHERE f.article_id = :articleId AND f.file_type = 'VIDEO'")
    void deleteVideoByArticleId(@Param("articleId") String articleId);
}
