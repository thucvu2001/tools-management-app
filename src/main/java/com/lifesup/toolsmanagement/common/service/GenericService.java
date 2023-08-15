package com.lifesup.toolsmanagement.common.service;

import com.lifesup.toolsmanagement.common.util.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface GenericService<T, D, I> {
    // T: entity, D: DTO, I: Id
    JpaRepository<T, I> getRepository();

    Mapper getMapper();

    default List<D> findAllDto(Class<D> dClass) {
        return getRepository().findAll()
                .stream()
                .map(model -> getMapper().map(model, dClass))
                .toList();
    }

    default Optional<D> findById(Class<D> dClass, I id) {
        return getRepository().findById(id)
                .map(model -> getMapper().map(model, dClass));
    }

    default D saveWithDTO(D dto, Class<T> tClass, Class<D> dClass) {
        T entity = getMapper().map(dto, tClass);
        getRepository().save(entity);
        return getMapper().map(entity, dClass);
    }

    default D saveWithEntity(T entity, Class<D> dClass) {
        getRepository().save(entity);
        return getMapper().map(entity, dClass);
    }

    default void deleteById(I id) {
        getRepository().deleteById(id);
    }
}
