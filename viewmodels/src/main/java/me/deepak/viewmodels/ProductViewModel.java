package me.deepak.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

import me.deepak.api.exceptions.NoMoreDataException;
import me.deepak.api.search.SearchApi;
import me.deepak.api.search.callbacks.SearchDataObserver;
import me.deepak.api.search.viewmodels.Products;

/**
 * Created by Deepak Mishra
 */

public class ProductViewModel extends ViewModel implements SearchDataObserver{

    private static final String TAG = "ProductViewModel";

    private int nextPageToLoad = 1;
    public static final int DEFAULT_PAGE_SIZE = 15;

    private AtomicBoolean isLoading = new AtomicBoolean(false);

    private MutableLiveData<Integer> selectedIndexAsync = new MutableLiveData<>();
    private MutableLiveData<Products> searchResultsAsync = new MutableLiveData<>();
    private MutableLiveData<Exception> searchExceptionAsync = new MutableLiveData<>();

    private SearchApi searchApi = new SearchApi(this);

    public boolean isLoading() {
        return isLoading.get();
    }

    public MutableLiveData<Products> getSearchResultsAsync() {
        return searchResultsAsync;
    }

    public void loadMoreSearchResults(){
        if(!isLoading()){
            isLoading.set(true);
            if(hasMoreData()){
                Log.d(TAG, "loadMoreSearchResults: hasMoreData");
                Products oldModel = getSearchResults();
                if(oldModel != null && oldModel.productList != null){
                    Products newResults = new Products();
                    newResults.productList.addAll(oldModel.productList);
                    newResults.productList.add(null);
                    searchResultsAsync.postValue(newResults);
                }
                searchApi.searchAsync(nextPageToLoad, DEFAULT_PAGE_SIZE);
            } else {
                Log.e(TAG, "loadMoreSearchResults: no more data");
                onError(new NoMoreDataException(nextPageToLoad, DEFAULT_PAGE_SIZE), nextPageToLoad, DEFAULT_PAGE_SIZE);
            }
        }
    }

    public MutableLiveData<Exception> getSearchExceptionAsync() {
        return searchExceptionAsync;
    }

    @Override
    public void onDataUpdated(Products newData, int pageNumber, int pageSize) {
        Log.d(TAG, "onDataUpdated() called with: newDataSize = [" + newData.productList.size() + "], " +
                "pageNumber = [" + pageNumber + "], pageSize = [" + pageSize + "]" +
                        "totalResults = [" + newData.totalProducts + "]");
        //ALWAYS pass new object in set/postValue, else adapter ref would auto change and we can't diff
        Products oldModel = getSearchResults();
        if(oldModel != null && oldModel.productList != null){
            oldModel.productList.remove(null);
            newData.productList.addAll(0, oldModel.productList);
            Log.d(TAG, "onDataUpdated: removed null. updatedDataSize : " + newData.productList.size());
        }

        searchResultsAsync.postValue(newData);
        nextPageToLoad++;
        isLoading.set(false);
    }

    @Override
    public void onError(Exception exception, int pageNumber, int pageSize) {
        Log.d(TAG, "onError() called with: exception = [" + exception.getMessage() + "], " +
                "pageNumber = [" + pageNumber + "], pageSize = [" + pageSize + "]");
        searchExceptionAsync.postValue(exception);
        isLoading.set(false);
    }

    public void cancelPendingCalls(){
        searchApi.cancelSearch();
        isLoading.set(false);
    }

    @Nullable
    public Products getSearchResults(){
        return searchResultsAsync.getValue();
    }

    public void setSelectedIndexAsync(int newIndex) {
        selectedIndexAsync.setValue(newIndex);
    }

    public MutableLiveData<Integer> getSelectedIndexAsync() {
        return selectedIndexAsync;
    }

    public int getLatestSelectedIndex() {
        Log.d(TAG, "getLatestSelectedIndex: " + selectedIndexAsync.getValue());
        return (selectedIndexAsync.getValue() == null) ? 0 : selectedIndexAsync.getValue();
    }

    public boolean hasMoreData() {
        return getSearchResults() == null ||
                getSearchResults().totalProducts < 0 ||
                getSearchResults().totalProducts > getSearchResults().productList.size();
    }

    public boolean hasProductData(){
        return getSearchResults() != null &&
                getSearchResults().productList != null &&
                getSearchResults().productList.size() >= 0;
    }
}
