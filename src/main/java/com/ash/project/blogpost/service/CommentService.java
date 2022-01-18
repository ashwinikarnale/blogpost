package com.ash.project.blogpost.service;

import com.ash.project.blogpost.model.Comment;
import com.ash.project.blogpost.model.Post;
import com.ash.project.blogpost.repository.CommentRepository;
import com.ash.project.blogpost.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    public void saveComment(Comment comment,int id) {
        comment.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        comment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        Post post=postRepository.findById(id).get();
        post.add(comment);
        commentRepository.save(comment);
    }
    public List<Comment> allComments(int postId) {
        return commentRepository.findAllCommentsByPostId(postId);
    }
    public void deleteComment(int id) {
        commentRepository.deleteById(id);
    }
    public void updateComment(Comment comment) {
        comment.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        commentRepository.save(comment);
    }
    public Comment getComment(int id) {
        return commentRepository.getById(id);
    }
}
