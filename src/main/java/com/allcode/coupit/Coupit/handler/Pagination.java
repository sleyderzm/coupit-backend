package com.allcode.coupit.Coupit.handler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

public class Pagination {

    private Integer currentPage;
    private Integer perPage;
    private Integer totalEntries;
    private Integer totalPages;
    private Integer offset;
    private List<?> data;

    public Pagination(Integer currentPage, Integer perPage) {
        if(currentPage == null) currentPage = 1;
        if(perPage == null) perPage = 10;
        this.currentPage = currentPage;
        this.perPage = perPage;
        this.data = new ArrayList();
        this.offset = 0;
        this.totalEntries = 0;
        this.totalPages = 0;
    }

    public Pagination() {
        this.currentPage = 1;
        this.perPage = 10;
        this.data = new ArrayList();
        this.offset = 0;
        this.totalEntries = 0;
        this.totalPages = 0;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public Integer getTotalEntries() {
        return totalEntries;
    }

    public void setTotalEntries(Integer totalEntries) {
        this.totalEntries = totalEntries;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    @JsonIgnore
    public Integer getFirstResult(){
        return (currentPage - 1)*perPage;
    }

    public void calculate(List<?> data, Integer count){
        this.data = data;
        this.totalEntries = count;
        this.totalPages = (int) Math.ceil((float)count/this.perPage);
        this.offset = (currentPage - 1)*perPage + data.size();
    }
}
