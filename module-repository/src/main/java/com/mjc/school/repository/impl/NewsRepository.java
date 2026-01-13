package com.mjc.school.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;

@Repository
public class NewsRepository extends AbstractDBRepository<News, Long> {

    @Override
    void update(News prevState, News nextState) {
        if (nextState.getTitle() != null && !nextState.getTitle().isBlank()) {
            prevState.setTitle(nextState.getTitle());
        }
        if (nextState.getContent() != null && !nextState.getContent().isBlank()) {
            prevState.setContent(nextState.getContent());
        }
        Author author = nextState.getAuthor();
        if (author != null && !author.getName().isBlank()) {
            prevState.setAuthor(nextState.getAuthor());
        }
        List<Tag> tags = nextState.getTags();
        if (tags != null && !tags.isEmpty()) {
            prevState.setTags(tags);
        }
    }
}
