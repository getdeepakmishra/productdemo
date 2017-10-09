package me.deepak.api.search;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

import me.deepak.api.exceptions.InProgressException;
import me.deepak.api.search.callbacks.SearchDataObserver;
import me.deepak.api.search.viewmodels.Products;

/**
 * Created by Deepak Mishra
 */

public class SearchApi {

    private ProductSearchTask searchTask;
    private AtomicBoolean inProgress = new AtomicBoolean(false);

    private WeakReference<SearchDataObserver> searchDataObserver;

    public SearchApi(SearchDataObserver searchDataObserver) {
        this.searchDataObserver = new WeakReference<>(searchDataObserver);
    }

    public void searchAsync(final int pageNumber, int pageSize){

        if(searchDataObserver.get() == null) return;
        if(inProgress.get()) {
            searchDataObserver.get().onError(new InProgressException(), pageNumber, pageSize);
        } else {
            setInProgress(true);
            searchTask = new ProductSearchTask(this, pageNumber, pageSize);
            searchTask.execute();
        }
    }


    public synchronized void cancelSearch(){
        if(searchTask != null) {
            searchTask.cancelCall();
        }
        setInProgress(false);
    }

    void onError(Exception exception, int pageNumber, int pageSize){
        if(searchDataObserver.get() != null){
            searchDataObserver.get().onError(exception, pageNumber, pageSize);
        }
        setInProgress(false);
    }

    void onNewData(Products products, int pageNumber, int pageSize){
        if(searchDataObserver.get() != null){
            searchDataObserver.get().onDataUpdated(products, pageNumber, pageSize);
        }
        setInProgress(false);
    }



    private void setInProgress(boolean inProgress){
        this.inProgress.set(inProgress);
    }

}
