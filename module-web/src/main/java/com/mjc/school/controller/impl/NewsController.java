package com.mjc.school.controller.impl;

import static com.mjc.school.controller.RestApiConst.NEWS_API_ROOT_PATH;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mjc.school.controller.BaseController;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.dto.CommentsDtoResponse;
import com.mjc.school.service.dto.CreateNewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.dto.PageDtoResponse;
import com.mjc.school.service.dto.ResourceSearchFilterRequestDTO;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.dto.UpdateNewsDtoRequest;
import com.mjc.school.service.impl.AuthorService;
import com.mjc.school.service.impl.CommentService;
import com.mjc.school.service.impl.TagService;
import com.mjc.school.versioning.ApiVersion;

/**
 *
 * All the class methods except are under the version 1
 * You can hit the methods by URI: http://host:port/api/v1/news
 */
@ApiVersion(1)
@RestController
@RequestMapping(value = NEWS_API_ROOT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class NewsController
    implements BaseController<CreateNewsDtoRequest, NewsDtoResponse, Long, ResourceSearchFilterRequestDTO, UpdateNewsDtoRequest> {

    private final BaseService<CreateNewsDtoRequest, NewsDtoResponse, Long, ResourceSearchFilterRequestDTO, UpdateNewsDtoRequest> newsService;
    private final TagService tagService;
    private final AuthorService authorService;
    private final CommentService commentService;

    @Autowired
    public NewsController(
            final BaseService<CreateNewsDtoRequest, NewsDtoResponse, Long, ResourceSearchFilterRequestDTO, UpdateNewsDtoRequest> newsService,
            final TagService tagService,
            final AuthorService authorService,
            final CommentService commentService) {
        this.newsService = newsService;
        this.tagService = tagService;
        this.authorService = authorService;
        this.commentService = commentService;
    }

    @Override
    @GetMapping
    public PageDtoResponse<NewsDtoResponse> readAll(final ResourceSearchFilterRequestDTO searchRequest) {
        return newsService.readAll(searchRequest);
    }

    @Override
    @GetMapping("/{id}")
    public NewsDtoResponse readById(@PathVariable Long id) {
        return newsService.readById(id);
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public NewsDtoResponse create(@RequestBody CreateNewsDtoRequest dtoRequest) {
        return newsService.create(dtoRequest);
    }

    @Override
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public NewsDtoResponse update(@PathVariable Long id, @RequestBody UpdateNewsDtoRequest dtoRequest) {
        return newsService.update(id, dtoRequest);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        newsService.deleteById(id);
    }

    @GetMapping("/{id}/tags")
    public List<TagDtoResponse> readTagsByNewsId(@PathVariable Long id) {
        return tagService.readByNewsId(id);
    }

    @GetMapping("/{id}/author")
    public AuthorDtoResponse readAuthorByNewsId(@PathVariable Long id) {
        return authorService.readByNewsId(id);
    }

    @GetMapping("/{id}/comments")
    public List<CommentsDtoResponse> readCommentsByNewsId(@PathVariable Long id) {
        return commentService.readByNewsId(id);
    }
}
