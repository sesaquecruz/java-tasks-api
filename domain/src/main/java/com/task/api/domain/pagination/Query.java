package com.task.api.domain.pagination;

public abstract class Query {
    private static final int MAX_PAGE_SIZE = 100;
    public static final String PAGE = "page";
    public static final String SIZE = "size";
    public static final String TERM = "term";
    public static final String SORT = "sort";
    public static final String DIRECTION = "direction";

    public enum Direction { asc, desc }

    private final int page;
    private final int size;
    private final String term;
    private final String sort;
    private final Direction direction;

    protected Query(
            int page,
            int size,
            String term,
            String sort,
            Direction direction
    ) {
        this.page = page;
        this.size = Math.min(size, MAX_PAGE_SIZE);
        this.term = term;
        this.sort = sort;
        this.direction = direction;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String getTerm() {
        return term;
    }

    public String getSort() {
        return sort;
    }

    public String getDirection() {
        return direction.name();
    }
}
