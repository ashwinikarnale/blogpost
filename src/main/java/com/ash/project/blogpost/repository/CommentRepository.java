package com.ash.project.blogpost.repository;

import com.ash.project.blogpost.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer>{

    @Query(value = "SELECT * FROM comments WHERE post_id=?1", nativeQuery = true)
    List<Comment> findAllCommentsByPostId(int postId);
}
