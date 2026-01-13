package com.mjc.school.controller.impl;

import static com.mjc.school.controller.RestApiConst.COMMENTS_API_ROOT_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mjc.school.controller.BaseController;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.CommentsDtoRequest;
import com.mjc.school.service.dto.CommentsDtoResponse;
import com.mjc.school.service.dto.PageDtoResponse;
import com.mjc.school.service.dto.ResourceSearchFilterRequestDTO;
import com.mjc.school.versioning.ApiVersion;

@ApiVersion(1)
@RestController
@RequestMapping(value = COMMENTS_API_ROOT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController
    implements BaseController<CommentsDtoRequest, CommentsDtoResponse, Long, ResourceSearchFilterRequestDTO, CommentsDtoRequest> {

    private final BaseService<CommentsDtoRequest, CommentsDtoResponse, Long, ResourceSearchFilterRequestDTO, CommentsDtoRequest> commentsService;


    @Autowired
    public CommentController(
            final BaseService<CommentsDtoRequest, CommentsDtoResponse, Long, ResourceSearchFilterRequestDTO, CommentsDtoRequest> commentsService) {
        this.commentsService = commentsService;
    }

    @Override
    @GetMapping
    public PageDtoResponse<CommentsDtoResponse> readAll(final ResourceSearchFilterRequestDTO searchFilterRequest) {
        return commentsService.readAll(searchFilterRequest);
    }

    /**
     * The method is under the api version 2
     * You can hit the method by URI: http://host:port/api/v2/comments/{id}
     * @param id
     * @return
     */
    @Override
    @ApiVersion(2)
    @GetMapping("/{id}")
    public CommentsDtoResponse readById(@PathVariable Long id) {
        return commentsService.readById(id);
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CommentsDtoResponse create(@RequestBody CommentsDtoRequest dtoRequest) {
        return commentsService.create(dtoRequest);
    }

    @Override
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommentsDtoResponse update(@PathVariable Long id, @RequestBody CommentsDtoRequest dtoRequest) {
        return commentsService.update(id, dtoRequest);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        commentsService.deleteById(id);
    }
}
