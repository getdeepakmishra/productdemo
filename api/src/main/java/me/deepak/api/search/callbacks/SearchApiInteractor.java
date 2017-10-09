package me.deepak.api.search.callbacks;

import java.util.List;

import me.deepak.api.search.viewmodels.Products;

/**
 * Created by Deepak Mishra
 */


public interface SearchApiInteractor {

    void cancelSearch();
    boolean isLoading();
    boolean hasMoreData();
    void loadMoreSearchResults();
    List<Products.Product> getFetchedSearchResults();
}
