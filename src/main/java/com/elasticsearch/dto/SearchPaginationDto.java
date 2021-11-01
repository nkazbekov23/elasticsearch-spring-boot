package com.elasticsearch.dto;

public class SearchPaginationDto {

    private final static int DEFAULT_SIZE = 1000;

    private int page;
    private int size;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size = size != 0 ? size : DEFAULT_SIZE;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
