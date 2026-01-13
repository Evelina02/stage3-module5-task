package com.mjc.school.repository;

import java.util.Optional;

import com.mjc.school.repository.filter.pagination.Page;
import com.mjc.school.repository.filter.specification.EntitySearchSpecification;
import com.mjc.school.repository.model.BaseEntity;

public interface BaseRepository<T extends BaseEntity<K>, K> {

    Page<T> readAll(final EntitySearchSpecification searchSpecification);

    Optional<T> readById(K id);

    T create(T entity);

    T update(T entity);

    void deleteById(K id);

    boolean existById(K id);

    T getReference(K id);
}
