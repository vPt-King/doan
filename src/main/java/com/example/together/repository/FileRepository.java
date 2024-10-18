package com.example.together.repository;

import com.example.together.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File, String> {

    @Query("SELECT a.url FROM File a WHERE a.article_id = :id and a.file_type='IMAGE'")
    List<String> findUrlByArticle_id(@Param("id") String id);

    @Query("SELECT a.url FROM File a WHERE a.article_id = :id and a.file_type='VIDEO'")
    String findVideo_articleByArticle_id(@Param("id") String id);
}
