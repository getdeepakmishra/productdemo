package me.deepak.api.search;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import me.deepak.api.exceptions.NoMoreDataException;
import me.deepak.api.retrofit.ApiUtils;
import me.deepak.api.retrofit.WalmartApiInterface;
import me.deepak.api.search.responses.SearchApiResponse;
import me.deepak.api.search.viewmodels.Products;
import retrofit2.Call;

/**
 * Created by Deepak Mishra
 */


public class ProductSearchTask extends AsyncTask<Void, Void, Products> {

    @NonNull private SearchApi productSearchCallback;
    private Call<SearchApiResponse> apiCall;
    private int pageNumber;
    private int pageSize;
    private Exception exception;

    public ProductSearchTask(@NonNull SearchApi productSearchCallback, int pageNumber, int pageSize) {
        this.productSearchCallback = productSearchCallback;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    @Override
    protected Products doInBackground(Void... voids) {

        try {

//            Thread.sleep((long) (Math.random()*6000));
//            Thread.sleep(6000);
            apiCall = ApiUtils.getWalmartApi().getProducts(WalmartApiInterface.API_KEY, pageNumber, pageSize);
            SearchApiResponse searchApiResponse = apiCall.execute().body();
            Products products = transformModel(searchApiResponse);
            if(products.productList.size() == 0) {
                productSearchCallback.onError(
                        new NoMoreDataException(pageNumber, pageSize), pageNumber, pageSize);
            }
            productSearchCallback.onNewData(products, pageNumber, pageSize);
        } catch (Exception e) {
            this.exception = e;
            productSearchCallback.onError(exception, pageNumber, pageSize);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Products products) {
//        if(products == null
//                || products.productList == null
//                || products.productList.size() == 0) {
//            productSearchCallback.onError(exception);
//        } else {
//            productSearchCallback.onNewData(products);
//        }

    }

    public void cancelCall(){
        try {
            if(apiCall != null) apiCall.cancel();
            cancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private Products transformModel(SearchApiResponse model){
        Products viewModel = new Products();
        viewModel.totalProducts = model.totalProducts;

        List<Products.Product> products = new ArrayList<>();
        for(SearchApiResponse.Product product : model.products){
            Products.Product productModelView  = new Products.Product();
            productModelView.id = product.productId;
            productModelView.imageUrl = product.productImage;
            productModelView.name = product.productName;
            products.add(productModelView);
        }
        viewModel.productList = products;

        return viewModel;
    }

}
