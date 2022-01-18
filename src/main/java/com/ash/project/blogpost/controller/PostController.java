package com.ash.project.blogpost.controller;

import com.ash.project.blogpost.model.Comment;
import com.ash.project.blogpost.model.Post;
import com.ash.project.blogpost.repository.PostRepository;
import com.ash.project.blogpost.security.MyUserDetails;
import com.ash.project.blogpost.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TagService tagService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @RequestMapping("/dashboard")
    public String dashboard(@RequestParam(value = "search", required = false) String search, @RequestParam(value = "filters", required = false) List<String> filters,
                            @RequestParam(value = "pageNo", required = false) Integer pageNo, @RequestParam(value = "dateFrom", required = false) String dateFrom,
                            @RequestParam(value = "dateTo", required = false) String dateTo, Model model,
                            @RequestParam(value = "sortField",required = false) String sortField,
                            @RequestParam(value = "sortDirection",required = false) String sortDirection)  {
        if(pageNo==null) {
            pageNo=1;
        }
        if(sortField==null) {
            sortField="author";
        }
        if(sortDirection==null) {
            sortDirection="asc";
        }
        if (filters == null && search == null && (dateFrom == null || dateTo == null)) {
            return findPaginated(1, "author", "asc", model);
        } else if ((search == null && filters != null) ||(dateFrom != null && dateTo != null )) {
            List<Post> filteredPosts = new ArrayList<>();
            Timestamp dateF;
            Timestamp dateT;
            filteredPosts.clear();
            List<Post> postTemp = new ArrayList<>();
            if (filters==null) {
                filters = new ArrayList<>();
                filteredPosts.addAll(postService.findAllPosts());
            }
            else {
                for (String eachFilter : filters) {
                    postTemp.addAll(postService.findBySearchKeywordForFilters(eachFilter));
                }
                filteredPosts.addAll(postTemp);
                Set<Post> setOfPosts = new HashSet<>(filteredPosts);
                filteredPosts.clear();
                List<Post> listOfPosts = new ArrayList<>(setOfPosts);
                filteredPosts.addAll(listOfPosts);
            }
            if (!(dateFrom == null) && !(dateTo == null)) {
                dateF = Timestamp.from(Instant.parse(dateFrom + ":00.000Z"));
                dateT = Timestamp.from(Instant.parse(dateTo + ":00.000Z"));
                List<Post> posts;
                List<Post> filterPosts = new ArrayList<>();
                if (!filteredPosts.isEmpty()) {
                    posts = filteredPosts;
                    for (Post post : posts) {
                        if (post.getPublishedAt().compareTo(dateF) >= 0 && post.getPublishedAt().compareTo(dateT) <= 0) {
                            filterPosts.add(post);
                        }
                    }
                    filteredPosts.clear();
                    filteredPosts.addAll(filterPosts);
                } else {
                    posts = postRepository.findAll();
                    for (Post post : posts) {
                        if (post.getPublishedAt().compareTo(dateF) >= 0 && post.getPublishedAt().compareTo(dateT) <= 0) {
                            filterPosts.add(post);
                        }
                    }
                }
                filteredPosts.clear();
                filteredPosts.addAll(filterPosts);
            }
            return getFilteredPaginatedResults(filteredPosts, 1, "author", "asc", model);
        } else if (search != null && filters == null) {
            model.addAttribute("search", search);
            List<Post> searchedFilters = postService.findBySearchKeyword(search);
            Timestamp dateF;
            Timestamp dateT;
            if (!(dateFrom == null) && !(dateTo == null)) {
                dateF = Timestamp.from(Instant.parse(dateFrom + ":00.000Z"));
                dateT = Timestamp.from(Instant.parse(dateTo + ":00.000Z"));
                List<Post> posts;
                List<Post> filterPosts = new ArrayList<>();
                if (!searchedFilters.isEmpty()) {
                    posts = searchedFilters;
                    for (Post post : posts) {
                        if (post.getPublishedAt().compareTo(dateF) >= 0 && post.getPublishedAt().compareTo(dateT) <= 0) {
                            filterPosts.add(post);
                        }
                    }
                    searchedFilters.clear();
                    searchedFilters.addAll(filterPosts);
                } else {
                    posts = postRepository.findAll();
                    for (Post post : posts) {
                        if (post.getPublishedAt().compareTo(dateF) >= 0 && post.getPublishedAt().compareTo(dateT) <= 0) {
                            filterPosts.add(post);
                        }
                    }
                }
                searchedFilters.clear();
                searchedFilters.addAll(filterPosts);
            }
            return getFilteredPaginatedResults(searchedFilters, pageNo, sortField, sortDirection, model);
        } else {
            List<Post> filteredPosts = new ArrayList<>();
            Timestamp dateF;
            Timestamp dateT;
            filteredPosts.clear();
            List<Post> postTemp = new ArrayList<>();
            for (String eachFilter : filters) {
                postTemp.addAll(postService.findBySearchKeywordForFilters(eachFilter));
            }
            filteredPosts.addAll(postTemp);
            Set<Post> setOfPosts = new HashSet<>(filteredPosts);
            filteredPosts.clear();
            List<Post> listOfPosts = new ArrayList<>(setOfPosts);
            filteredPosts.addAll(listOfPosts);
            if (!(dateFrom == null) && !(dateTo == null)) {
                dateF = Timestamp.from(Instant.parse(dateFrom + ":00.000Z"));
                dateT = Timestamp.from(Instant.parse(dateTo + ":00.000Z"));
                List<Post> posts;
                List<Post> filterPosts = new ArrayList<>();
                if (!filteredPosts.isEmpty()) {
                    posts = filteredPosts;
                    for (Post post : posts) {
                        if (post.getPublishedAt().compareTo(dateF) >= 0 && post.getPublishedAt().compareTo(dateT) <= 0) {
                            filterPosts.add(post);
                        }
                    }
                    filteredPosts.clear();
                    filteredPosts.addAll(filterPosts);
                } else {
                    posts = postRepository.findAll();
                    for (Post post : posts) {
                        if (post.getPublishedAt().compareTo(dateF) >= 0 && post.getPublishedAt().compareTo(dateT) <= 0) {
                            filterPosts.add(post);
                        }
                    }
                    filteredPosts.clear();
                    filteredPosts.addAll(filterPosts);
                }
            }
            model.addAttribute("search", search);
            List<Post> posts = postService.findBySearchKeyword(search);
            List<Post> tempListOfPosts = new ArrayList<>();
            List<Integer> idList = new ArrayList<>();
            for (Post post : posts) {
                idList.add(post.getId());
            }
            for (Post post : filteredPosts) {
                if (idList.contains(post.getId())) {
                    tempListOfPosts.add(post);
                }
            }
            return getFilteredPaginatedResults(tempListOfPosts, 1, "author", "asc", model);
        }
    }

    @GetMapping("/addPost")
    public String addPost(Model model) {
        MyUserDetails myUserDetails = (MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = myUserDetails.getName();
        Post post = new Post();
        post.setAuthor(userName);
        model.addAttribute("post", post);
        return "addPost";
    }

    @RequestMapping("/read/{id}")
    public String readPost(@PathVariable(value = "id") int id, Model model) {
        Post post = postService.readPost(id);
        Comment comment = new Comment();
        String userName = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            userName = getCurrentUsername();
        }
        model.addAttribute("tag",tagService.findTagByPostId(id));
        model.addAttribute("username",userName);
        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        List<Comment> comments = commentService.allComments(id);
        model.addAttribute("comments", comments);
        return "readPost";
    }

    @PostMapping("/savePost")
    public String savePost(@ModelAttribute("post") Post post, @RequestParam("helperTags") String helperTags) {
        postService.savePost(post, helperTags);
        return "redirect:/dashboard";
    }

    @RequestMapping("/delete/{id}")
    public String deletePost(@PathVariable(value = "id") int id) {
        postService.deletePost(id);
        return "redirect:/dashboard";
    }

    @RequestMapping("/update/{id}")
    public String updatePost(@PathVariable(value = "id") int id, Model model) {
        Post post = postService.updatePost(id);
        String tags = tagService.findTagByPostId(id);
        model.addAttribute("helperTags",tags);
        model.addAttribute("post", post);
        return "addPost";
    }

    @RequestMapping("/search")
    public String searchPost(@RequestParam("search") String search, Model model) {
        List<Post> posts = postService.findBySearchKeyword(search);
        model.addAttribute("posts", posts);
        return "dashboard";
    }

    @GetMapping("/reset")
    public String resetDashboard() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard/{pageNo}")
    public String findPaginated(@PathVariable int pageNo, @RequestParam("sortField") String sortField,
                                @RequestParam("sortDirection") String sortDirection, Model model) {
        String userName = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            userName = getCurrentUsername();
        }
        int pageSize = 10;
        Page<Post> page = postService.findPaginated(pageNo, pageSize, sortField, sortDirection);
        List<Post> posts = page.getContent();
        model.addAttribute("admin","Ashwini");
        model.addAttribute("username",userName);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("tags", tagService.findAllTags());
        model.addAttribute("authors", postService.findAllAuthors());
        model.addAttribute("posts", posts);
        return "dashboard";
    }

    @GetMapping("/filter/{pageNo}")
    public String getFilteredPaginatedResults(List<Post> filteredPost, @PathVariable int pageNo, @RequestParam("sortField") String sortField,
                                              @RequestParam("sortDirection") String sortDirection, Model model) {
        int pageSize = 10;
        List<Post> filteredList = new ArrayList<>(filteredPost);
        if (sortField.equalsIgnoreCase("publishedAt")) {
            if (sortDirection.equalsIgnoreCase("asc")) {
                Collections.sort(filteredList);
            }
            else {
                Collections.sort(filteredList,Collections.reverseOrder());
            }
        }
        PagedListHolder<Post> pageList = postService.findPaginatedForFilters(filteredList, pageNo, pageSize, sortField, sortDirection);
        List<Post> postsFiltered;
        if (pageNo == 1) {
            pageList.setPageSize(pageSize);
            pageList.setPage(0);
            postsFiltered = pageList.getPageList();
        } else {
            pageList.setPageSize(pageSize);
            pageList.setPage(pageNo - 1);
            postsFiltered = pageList.getPageList();
        }
        String userName = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            userName = getCurrentUsername();
        }
        model.addAttribute("admin","Ashwini");
        model.addAttribute("username",userName);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", pageList.getPageCount());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("posts", postsFiltered);
        model.addAttribute("tags", tagService.findAllTags());
        model.addAttribute("authors", postService.findAllAuthors());
        return "filterPage";
    }
    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((MyUserDetails) principal).getName();
        }
        return String.valueOf(principal);
    }
}


