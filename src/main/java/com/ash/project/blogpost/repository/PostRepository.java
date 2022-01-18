package com.ash.project.blogpost.repository;

import com.ash.project.blogpost.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
    @Query(value ="SELECT * FROM post p INNER JOIN post_tags pt ON p.id = pt.post_id " +
            "INNER JOIN tag t ON pt.tag_id = t.id " +
            "where p.title like %?1% or p.author like %?1% or p.content like %?1% or t.name like %?1%",nativeQuery = true)
    List<Post> findAllBySearch(String search);
    @Query(value = "SELECT * FROM post p INNER JOIN post_tags pt ON p.id = pt.post_id INNER JOIN tag t ON pt.tag_id = t.id " +
            "where p.author like %?1% or t.name like %?1%",nativeQuery = true)
    List<Post> findAllBySearchForFilters(String search);
    @Query(value = "Select distinct(p.author) from post p",nativeQuery = true)
    List<String> findAllAuthors();
}
