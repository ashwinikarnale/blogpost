package com.ash.project.blogpost.restapi;

import com.ash.project.blogpost.model.AuthRequest;
import com.ash.project.blogpost.model.Post;
import com.ash.project.blogpost.repository.PostRepository;
import com.ash.project.blogpost.service.PostService;
import com.ash.project.blogpost.service.TagService;
import com.ash.project.blogpost.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api")
public class restController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @Autowired
    TagService tagService;

    @GetMapping("/posts")
    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/post/{id}")
    public Post findPostById(@PathVariable int id) {
        return postRepository.findById(id).get();
    }

    @GetMapping("/")
    public List<Post> dashboard(@RequestParam(value = "search", required = false) String search,
                                @RequestParam(value = "filters", required = false) List<String> filters,
                                @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                @RequestParam(value = "dateFrom", required = false) String dateFrom,
                                @RequestParam(value = "dateTo", required = false) String dateTo,
                                @RequestParam(value = "sortField", required = false) String sortField,
                                @RequestParam(value = "sortDirection", required = false) String sortDirection, Model model) {
        if (pageNo == null) {
            pageNo = 1;
        }
        if (sortField == null) {
            sortField = "author";
        }
        if (sortDirection == null) {
            sortDirection = "asc";
        }
        if (filters == null && search == null && (dateFrom == null && dateTo == null)) {
            return findPaginated(pageNo, sortField, sortDirection, model);
        } else if ((search == null && filters != null) || (dateFrom != null && dateTo != null)) {
            List<Post> postsFiltered = new ArrayList<>();
            Timestamp dateF;
            Timestamp dateT;
            postsFiltered.clear();
            List<Post> postTemp = new ArrayList<>();
            if (filters == null) {
                filters = new ArrayList<>();
                postsFiltered.addAll(postService.findAllPosts());
            } else {
                for (String eachFilter : filters) {
                    postTemp.addAll(postService.findBySearchKeywordForFilters(eachFilter));
                }
                postsFiltered.addAll(postTemp);
                Set<Post> setOfPosts = new HashSet<>(postsFiltered);
                postsFiltered.clear();
                List<Post> listOfPosts = new ArrayList<>(setOfPosts);
                postsFiltered.addAll(listOfPosts);
            }
            if (!(dateFrom == null) && !(dateTo == null)) {
                dateF = Timestamp.from(Instant.parse(dateFrom + ":00.000Z"));
                dateT = Timestamp.from(Instant.parse(dateTo + ":00.000Z"));
                List<Post> posts;
                List<Post> filterPosts = new ArrayList<>();
                if (!postsFiltered.isEmpty()) {
                    posts = postsFiltered;
                    for (Post post : posts) {
                        if (post.getPublishedAt().compareTo(dateF) >= 0 && post.getPublishedAt().compareTo(dateT) <= 0) {
                            filterPosts.add(post);
                        }
                    }
                    postsFiltered.clear();
                    postsFiltered.addAll(filterPosts);
                } else {
                    posts = postRepository.findAll();
                    for (Post post : posts) {
                        if (post.getPublishedAt().compareTo(dateF) >= 0 && post.getPublishedAt().compareTo(dateT) <= 0) {
                            filterPosts.add(post);
                        }
                    }
                }
                postsFiltered.clear();
                postsFiltered.addAll(filterPosts);
            }
            return getFilteredPaginatedResults(postsFiltered, pageNo, sortField, sortDirection, model);
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
            List<Post> searchedAndFilteredPosts = new ArrayList<>();
            Timestamp dateF;
            Timestamp dateT;
            searchedAndFilteredPosts.clear();
            List<Post> postTemp = new ArrayList<>();
            for (String eachFilter : filters) {
                postTemp.addAll(postService.findBySearchKeywordForFilters(eachFilter));
            }
            searchedAndFilteredPosts.addAll(postTemp);
            Set<Post> setOfPosts = new HashSet<>(searchedAndFilteredPosts);
            searchedAndFilteredPosts.clear();
            List<Post> listOfPosts = new ArrayList<>(setOfPosts);
            searchedAndFilteredPosts.addAll(listOfPosts);
            if (!(dateFrom == null) && !(dateTo == null)) {
                dateF = Timestamp.from(Instant.parse(dateFrom + ":00.000Z"));
                dateT = Timestamp.from(Instant.parse(dateTo + ":00.000Z"));
                List<Post> posts;
                List<Post> filterPosts = new ArrayList<>();
                if (!searchedAndFilteredPosts.isEmpty()) {
                    posts = searchedAndFilteredPosts;
                    for (Post post : posts) {
                        if (post.getPublishedAt().compareTo(dateF) >= 0 && post.getPublishedAt().compareTo(dateT) <= 0) {
                            filterPosts.add(post);
                        }
                    }
                    searchedAndFilteredPosts.clear();
                    searchedAndFilteredPosts.addAll(filterPosts);
                } else {
                    posts = postRepository.findAll();
                    for (Post post : posts) {
                        if (post.getPublishedAt().compareTo(dateF) >= 0 && post.getPublishedAt().compareTo(dateT) <= 0) {
                            filterPosts.add(post);
                        }
                    }
                }
                searchedAndFilteredPosts.clear();
                searchedAndFilteredPosts.addAll(filterPosts);
            }
            model.addAttribute("search", search);
            List<Post> posts = postService.findBySearchKeyword(search);
            List<Post> tempListOfPosts = new ArrayList<>();
            List<Integer> idList = new ArrayList<>();
            for (Post post : posts) {
                idList.add(post.getId());
            }
            for (Post post : searchedAndFilteredPosts) {
                if (idList.contains(post.getId())) {
                    tempListOfPosts.add(post);
                }
            }
            return getFilteredPaginatedResults(tempListOfPosts, 1, "author", "asc", model);
        }
    }

    public List<Post> findPaginated(int pageNo, String sortField, String sortDirection, Model model) {
        int pageSize = 10;
        Page<Post> page = postService.findPaginated(pageNo, pageSize, sortField, sortDirection);
        List<Post> posts = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("posts", posts);
        return posts;
    }

    public List<Post> getFilteredPaginatedResults(List<Post> filteredPosts,
                                                  @PathVariable int pageNo,
                                                  @RequestParam("sortField") String sortField,
                                                  @RequestParam("sortDirection") String sortDirection, Model model) {
        int pageSize = 10;
        String reverseSortDirection=sortDirection.equals("asc") ? "desc" : "asc";
        List<Post> tempList = new ArrayList<>(filteredPosts);
        if (sortField.equalsIgnoreCase("publishedAt")) {
            if (sortDirection.equalsIgnoreCase("asc")) {
                Collections.sort(tempList);
            } else {
                Collections.sort(tempList, Collections.reverseOrder());
            }
        }
        PagedListHolder<Post> pageList = postService.
                findPaginatedForFilters(tempList, pageNo, pageSize, sortField, sortDirection);
        List<Post> postsFiltered;
        if (pageNo == 1) {
            pageList.setPageSize(pageSize);
            pageList.setPage(0);
            postsFiltered = pageList.getPageList();
        } else {
            pageList.setPageSize(pageSize);
            pageList.setPage(pageNo - 1);
            postsFiltered = pageList.getPageList();
            System.out.println("hello");
        }
        model.addAttribute("totalPages", pageList.getPageCount());
        return postsFiltered;
    }

    @PostMapping("/post")
    public String createPost (@RequestBody Post post) {
        postRepository.save(post);
        return "Post created successfully";
    }

    @PostMapping("/post/{id}")
    public String updatePost (@RequestBody Post post, @PathVariable(value = "id") int id) {
        postRepository.save(post);
        return "Post updated successfully";
    }

    @DeleteMapping ("/post/{id}")
    public String deletePost ( @PathVariable(value = "id")int id) {
        postRepository.deleteById(id);
        return "Post deleted successfully";
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getEmail());
    }
}
