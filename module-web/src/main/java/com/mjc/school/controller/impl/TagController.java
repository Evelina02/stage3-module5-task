package com.mjc.school.controller.impl;

import static com.mjc.school.controller.RestApiConst.TAG_API_ROOT_PATH;

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
import com.mjc.school.service.dto.PageDtoResponse;
import com.mjc.school.service.dto.ResourceSearchFilterRequestDTO;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.versioning.ApiVersion;

/**
 *
 * All the class methods except readById() are under the version 1
 * You can hit the methods by URI: http://host:port/api/v1/tags
 */
@ApiVersion(1)
@RestController
@RequestMapping(value = TAG_API_ROOT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController
    implements BaseController<TagDtoRequest, TagDtoResponse, Long, ResourceSearchFilterRequestDTO, TagDtoRequest> {

    private final BaseService<TagDtoRequest, TagDtoResponse, Long, ResourceSearchFilterRequestDTO, TagDtoRequest> tagService;


    @Autowired
    public TagController(final BaseService<TagDtoRequest, TagDtoResponse, Long, ResourceSearchFilterRequestDTO, TagDtoRequest> tagService) {
        this.tagService = tagService;
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDtoResponse<TagDtoResponse> readAll(ResourceSearchFilterRequestDTO searchFilterRequest) {
        return tagService.readAll(searchFilterRequest);
    }

    /**
     * The method is under the api version 2
     * You can hit the method by URI: http://host:port/api/v2/tags/{id}
     * @param id
     * @return
     */
    @Override
    @ApiVersion(2)
    @GetMapping("/{id}")
    public TagDtoResponse readById(@PathVariable Long id) {
        return tagService.readById(id);
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TagDtoResponse create(@RequestBody TagDtoRequest dtoRequest) {
        return tagService.create(dtoRequest);
    }

    @Override
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TagDtoResponse update(@PathVariable Long id, @RequestBody TagDtoRequest dtoRequest) {
        return tagService.update(id, dtoRequest);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
         tagService.deleteById(id);
    }
}
