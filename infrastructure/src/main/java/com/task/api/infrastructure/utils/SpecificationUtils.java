package com.task.api.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils {
    private SpecificationUtils() { }

    public static <T>Specification<T> equal(String prop, String term) {
        return (root, cq, cb) -> cb.equal(cb.upper(root.get(prop)), term.toUpperCase());
    }

    public static <T>Specification<T> like(String prop, String term) {
        return (root, cq, cb) -> cb.like(cb.upper(root.get(prop)), "%" + term.toUpperCase() + "%");
    }
}
