package me.deepak.api.search.callbacks;

import me.deepak.api.search.viewmodels.Products;

/**
 * Created by Deepak Mishra
 */

public interface SearchDataObserver {
    void onDataUpdated(Products newData, int requestedPageNumber, int requestedPageSize);
    void onError(Exception exception, int pageNumber, int pageSize);
}
