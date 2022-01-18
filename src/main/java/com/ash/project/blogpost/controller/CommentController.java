package com.ash.project.blogpost.controller;

import com.ash.project.blogpost.model.Comment;
import com.ash.project.blogpost.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/addComment/{postId}")
    public String addComment(@ModelAttribute("comment") Comment comment, @PathVariable(value = "postId") int id) {
        commentService.saveComment(comment,id);
        return "redirect:/read/" +id;
    }
    @RequestMapping("/read/delete/{postId}/{id}")
    public String deleteComment(@PathVariable(value = "id") int id, @PathVariable(value = "postId") int postId) {
        commentService.deleteComment(id);
        return "redirect:/read/"+postId;
    }
    @PostMapping("/addComment/updateComment/{postId}/{id}")
    public String updateComment(@PathVariable(value = "id") int id, @PathVariable(value = "postId") int postId, @ModelAttribute Comment comment) {
        commentService.updateComment(comment);
        return "redirect:/read/"+postId;
    }
    @RequestMapping("/updateCommentPage/{postId}/{id}")
    public String updateCommentPage(Model model, @PathVariable int id, @PathVariable int postId) {
        Comment comment = commentService.getComment(id);
        model.addAttribute("comment",comment);
        model.addAttribute("postId",postId);
        return "updateComment";
    }
}
