package com.mjc.school.controller.impl;

import static com.mjc.school.controller.RestApiConst.AUTHOR_API_ROOT_PATH;

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
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.dto.PageDtoResponse;
import com.mjc.school.service.dto.ResourceSearchFilterRequestDTO;
import com.mjc.school.versioning.ApiVersion;

/**
 * All the class methods except readById() are under the version 1
 * You can hit the methods by URI: http://host:port/api/v1/authors
 */
@ApiVersion(1)
@RestController
@RequestMapping(value = AUTHOR_API_ROOT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorController
    implements BaseController<AuthorDtoRequest, AuthorDtoResponse, Long, ResourceSearchFilterRequestDTO, AuthorDtoRequest> {

    private final BaseService<AuthorDtoRequest, AuthorDtoResponse, Long, ResourceSearchFilterRequestDTO, AuthorDtoRequest> authorService;

    @Autowired
    public AuthorController(
        final BaseService<AuthorDtoRequest, AuthorDtoResponse, Long, ResourceSearchFilterRequestDTO, AuthorDtoRequest> authorService) {
        this.authorService = authorService;
    }

    @Override
    @GetMapping
    public PageDtoResponse<AuthorDtoResponse> readAll(final ResourceSearchFilterRequestDTO searchRequest) {
        return authorService.readAll(searchRequest);
    }

    @Override
    @ApiVersion(2)
    @GetMapping("/{id}")
    public AuthorDtoResponse readById(@PathVariable Long id) {
        return authorService.readById(id);
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDtoResponse create(@RequestBody AuthorDtoRequest dtoRequest) {
        return authorService.create(dtoRequest);
    }

    @Override
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthorDtoResponse update(@PathVariable Long id, @RequestBody AuthorDtoRequest dtoRequest) {
        return authorService.update(id, dtoRequest);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        authorService.deleteById(id);
    }
}
