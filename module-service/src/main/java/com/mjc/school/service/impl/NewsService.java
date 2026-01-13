package com.mjc.school.service.impl;

import static com.mjc.school.service.exceptions.ServiceErrorCode.NEWS_CONFLICT;
import static com.mjc.school.service.exceptions.ServiceErrorCode.NEWS_ID_DOES_NOT_EXIST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.exception.EntityConflictRepositoryException;
import com.mjc.school.repository.filter.pagination.Page;
import com.mjc.school.repository.impl.AuthorRepository;
import com.mjc.school.repository.impl.TagRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.CreateNewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.dto.PageDtoResponse;
import com.mjc.school.service.dto.ResourceSearchFilterRequestDTO;
import com.mjc.school.service.dto.UpdateNewsDtoRequest;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.exceptions.ResourceConflictServiceException;
import com.mjc.school.service.filter.ResourceSearchFilter;
import com.mjc.school.service.filter.mapper.NewsSearchFilterMapper;
import com.mjc.school.service.mapper.NewsMapper;
import com.mjc.school.service.validator.Valid;


@Service
public class NewsService
    implements BaseService<CreateNewsDtoRequest, NewsDtoResponse, Long, ResourceSearchFilterRequestDTO, UpdateNewsDtoRequest> {

    private final BaseRepository<News, Long> newsRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;


    private final NewsMapper mapper;
    private final NewsSearchFilterMapper newsSearchFilterMapper;

    @Autowired
    public NewsService(
        final BaseRepository<News, Long> newsRepository,
        final AuthorRepository authorRepository,
        final TagRepository tagRepository,
        final NewsMapper mapper,
        final NewsSearchFilterMapper newsSearchFilterMapper
    ) {
        this.newsRepository = newsRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.mapper = mapper;
        this.newsSearchFilterMapper = newsSearchFilterMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public PageDtoResponse<NewsDtoResponse> readAll(@Valid ResourceSearchFilterRequestDTO searchFilterRequest) {
        final ResourceSearchFilter searchFilter = newsSearchFilterMapper.map(searchFilterRequest);
        Page page = newsRepository.readAll(getEntitySearchSpecification(searchFilter));
        List<NewsDtoResponse> modelDtoList = mapper.modelListToDtoList(page.entities());
        return new PageDtoResponse<>(modelDtoList, page.currentPage(), page.pageCount());
    }

    @Override
    @Transactional(readOnly = true)
    public NewsDtoResponse readById(final Long id) {
        return newsRepository.readById(id)
            .map(mapper::modelToDto)
            .orElseThrow(
                () -> new NotFoundException(
                    String.format(
                        NEWS_ID_DOES_NOT_EXIST.getMessage(),
                        id
                    )
                )
            );
    }

    @Override
    @Transactional
    public NewsDtoResponse create(@Valid CreateNewsDtoRequest createRequest) {
        createNonExistentAuthor(createRequest.author());
        createNonExistentTags(createRequest.tags());
        try {
            News model = mapper.dtoToModel(createRequest);
            model = newsRepository.create(model);
            return mapper.modelToDto(model);
        } catch (EntityConflictRepositoryException exc) {
            throw new ResourceConflictServiceException(NEWS_CONFLICT.getMessage(), NEWS_CONFLICT.getErrorCode(), exc.getMessage());
        }
    }

    @Override
    @Transactional
    public NewsDtoResponse update(Long id, @Valid UpdateNewsDtoRequest updateRequest) {
        if (!newsRepository.existById(id)) {
            throw new NotFoundException(String.format(NEWS_ID_DOES_NOT_EXIST.getMessage(), id));
        }
        createNonExistentAuthor(updateRequest.author());
        createNonExistentTags(updateRequest.tags());

        News model = mapper.dtoToModel(updateRequest);
        model.setId(id);
        model = newsRepository.update(model);
        return mapper.modelToDto(model);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (newsRepository.existById(id)) {
            newsRepository.deleteById(id);
        } else {
            throw new NotFoundException(String.format(NEWS_ID_DOES_NOT_EXIST.getMessage(), id));
        }
    }

    private void createNonExistentAuthor(String authorName) {
        if (authorName != null && !authorName.equals("")) {
            if (authorRepository.readByName(authorName).isEmpty()) {
                Author author = new Author();
                author.setName(authorName);
                authorRepository.create(author);
            }
        }
    }

    private void createNonExistentTags(List<String> tagNames) {
        tagNames.stream()
            .filter(name -> tagRepository.readByName(name).isEmpty())
            .map(name -> {
                Tag tag = new Tag();
                tag.setName(name);
                return tag;
            })
            .forEach(tagRepository::create);
    }
}
