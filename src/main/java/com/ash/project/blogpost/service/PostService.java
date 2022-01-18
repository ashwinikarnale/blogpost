package com.ash.project.blogpost.service;

import com.ash.project.blogpost.model.Post;
import com.ash.project.blogpost.model.Tag;
import com.ash.project.blogpost.repository.PostRepository;
import com.ash.project.blogpost.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    public void savePost(Post post, String helperTags) {
        String[] tagsNames = helperTags.split(",");
        for (String tagName : tagsNames) {
            if (tagRepository.findByName(tagName.toLowerCase().trim()) == null) {
                Tag tag = new Tag();
                tag.setName(tagName.toLowerCase().trim());
                tag.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
                if (tag.getCreatedAt() == null) {
                    tag.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                }
                post.getTags().add(tag);
                tag.getPosts().add(post);
            } else {
                Tag tag = tagRepository.findByName(tagName.toLowerCase().trim());
                tag.setName(tagName.toLowerCase().trim());
                post.getTags().add(tag);
            }
        }
        post.setPublishedAt(Timestamp.valueOf(LocalDateTime.now()));
        post.setPublished(true);
        post.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        post.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        postRepository.save(post);
    }

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    public void deletePost(int id) {
        postRepository.deleteById(id);
    }

    public Post readPost(int id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.get();
    }

    public Post updatePost(int id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        optionalPost.get().setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return optionalPost.get();
    }

    public List<Post> findBySearchKeyword(String search) {
        List<Post> searchResults = postRepository.findAllBySearch(search);
        Set<Post> uniquePosts = new HashSet<>(searchResults);
        List<Post> posts = new ArrayList<>(uniquePosts);
        return posts;
    }

    public List<Post> findBySearchKeywordForFilters(String search) {
        List<Post> searchResults = postRepository.findAllBySearchForFilters(search);
        Set<Post> uniquePosts = new HashSet<>(searchResults);
        List<Post> posts = new ArrayList<>(uniquePosts);
        return posts;
    }

    public Page<Post> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return postRepository.findAll(pageable);
    }

    public List<String> findAllAuthors() {
        return postRepository.findAllAuthors();
    }

    public PagedListHolder<Post> findPaginatedForFilters(List<Post> posts, int pageNo,
                                                         int pageSize, String sortField, String sortDirection) {
        boolean bool;
        if (sortDirection.equalsIgnoreCase("asc")) {
            bool = true;
        } else {
            bool = false;
        }
        MutableSortDefinition mutableSort = new MutableSortDefinition(sortField, true, false);
        PagedListHolder<Post> pageList = new PagedListHolder<Post>(posts, mutableSort);
        return pageList;
    }
}
